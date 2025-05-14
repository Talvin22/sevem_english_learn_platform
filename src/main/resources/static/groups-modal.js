let currentGroupId = null;
let currentGroupName = null;

document.addEventListener("DOMContentLoaded", () => {
    const role = document.body.getAttribute("data-role");
    if (role !== "TEACHER") return;

    loadGroups();

    const createBtn = document.getElementById("create-group-btn");
    if (createBtn) {
        createBtn.onclick = () => {
            const groupName = prompt("Enter group name:");
            if (groupName) {
                fetch("/api/groups", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ name: groupName })
                })
                    .then(() => loadGroups())
                    .catch(err => alert("Failed to create group: " + err.message));
            }
        };
    }
});

function loadGroups() {
    const container = document.getElementById("teacher-groups-container");
    if (!container) return;

    fetch("/api/groups/teacher")
        .then(res => res.json())
        .then(groups => {
            container.innerHTML = "";

            if (groups.length === 0) {
                container.innerHTML = "<p>No groups found.</p>";
                return;
            }

            groups.forEach(group => {
                const wrapper = document.createElement("div");
                wrapper.className = "card-content group-card";
                wrapper.style.position = "relative";

                const name = document.createElement("div");
                name.textContent = group.name;
                name.style.cursor = "pointer";
                name.onclick = () => openGroupModal(group.id, group.name);

                const deleteBtn = document.createElement("button");
                deleteBtn.textContent = "❌";
                deleteBtn.className = "delete-btn";
                deleteBtn.style.position = "absolute";
                deleteBtn.style.right = "10px";
                deleteBtn.style.top = "10px";
                deleteBtn.onclick = (e) => {
                    e.stopPropagation();
                    if (confirm("Are you sure you want to delete this group?")) {
                        fetch(`/api/groups/${group.id}`, { method: "DELETE" })
                            .then(() => loadGroups())
                            .catch(err => alert("Failed to delete group: " + err.message));
                    }
                };

                wrapper.appendChild(name);
                wrapper.appendChild(deleteBtn);
                container.appendChild(wrapper);
            });
        })
        .catch(err => {
            console.error("Failed to load groups:", err);
            container.innerHTML = `<p style="color: red;">Error loading groups: ${err.message}</p>`;
        });
}

function openGroupModal(groupId, groupName) {
    currentGroupId = groupId;
    currentGroupName = groupName;

    const modal = document.getElementById("groupModal");
    const overlay = document.getElementById("groupModalOverlay");
    const list = document.getElementById("group-student-list");
    const addBtn = document.getElementById("add-student-btn");

    if (!modal || !overlay || !list || !addBtn) return;

    document.getElementById("groupModalTitle").textContent = `Group: ${groupName}`;

    fetch(`/api/groups/${groupId}/students`)
        .then(res => res.json())
        .then(students => {
            list.innerHTML = "";

            students.forEach(s => {
                const div = document.createElement("div");
                div.className = "student-entry";
                div.innerHTML = `${s.firstName} ${s.lastName} <button onclick="removeStudent(${groupId}, ${s.id})">❌</button>`;
                list.appendChild(div);
            });

            addBtn.onclick = () => showAddStudentSelect(groupId);

            modal.style.display = "block";
            overlay.style.display = "block";
        })
        .catch(err => {
            console.error(`❌ Failed to load students for group ${groupId}:`, err);
            alert("Error loading students");
        });
}

function closeGroupModal() {
    const modal = document.getElementById("groupModal");
    const overlay = document.getElementById("groupModalOverlay");
    if (modal) modal.style.display = "none";
    if (overlay) overlay.style.display = "none";
}

function removeStudent(groupId, studentId) {
    fetch(`/api/groups/${groupId}/remove-student?studentId=${studentId}`, { method: "DELETE" })
        .then(() => openGroupModal(currentGroupId, currentGroupName));
}

function showAddStudentSelect(groupId) {
    const addBtn = document.getElementById("add-student-btn");
    const container = document.getElementById("add-student-container");

    if (!addBtn || !container) return;

    addBtn.onclick = null;

    fetch(`/api/groups/${groupId}/students/free`)
        .then(res => res.json())
        .then(students => {
            container.innerHTML = "";

            if (students.length === 0) {
                container.innerHTML = "<p>No available students</p>";
                return;
            }

            const select = document.createElement("select");
            select.id = "studentSelect";
            students.forEach(s => {
                const option = document.createElement("option");
                option.value = s.id;
                option.textContent = `${s.firstName} ${s.lastName}`;
                select.appendChild(option);
            });

            const btn = document.createElement("button");
            btn.textContent = "Add";
            btn.className = "btn";
            btn.onclick = () => addStudentToGroup(groupId, select.value);

            container.appendChild(select);
            container.appendChild(btn);
        });
}

function addStudentToGroup(groupId, studentId) {
    fetch(`/api/groups/${groupId}/add-student`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ studentId })
    })
        .then(res => {
            if (!res.ok) throw new Error("Failed to add student");
            return res.json();
        })
        .then(() => {
            const container = document.getElementById("add-student-container");
            if (container) container.innerHTML = "";
            openGroupModal(currentGroupId, currentGroupName);
        })
        .catch(err => {
            console.error("❌ Error adding student:", err);
            alert("Failed to add student.");
        });
}