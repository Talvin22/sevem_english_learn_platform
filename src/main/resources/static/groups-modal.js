
let currentGroupId = null;
let currentGroupName = null;

document.addEventListener("DOMContentLoaded", () => {
    loadGroups();
});

function loadGroups() {
    fetch("/api/groups/teacher")
        .then(res => res.json())
        .then(groups => {
            const container = document.getElementById("teacher-groups-container");
            container.innerHTML = "";

            if (groups.length === 0) {
                container.innerHTML = "<p>No groups found.</p>";
                return;
            }

            groups.forEach(group => {
                const div = document.createElement("div");
                div.className = "card-content group-card";
                div.textContent = group.name;
                div.style.cursor = "pointer";
                div.onclick = () => openGroupModal(group.id, group.name);
                container.appendChild(div);
            });
        })
        .catch(err => {
            console.error("Failed to load groups:", err);
            document.getElementById("teacher-groups-container").innerHTML =
                `<p style="color: red;">Error loading groups: ${err.message}</p>`;
        });
}

function openGroupModal(groupId, groupName) {
    currentGroupId = groupId;
    currentGroupName = groupName;

    document.getElementById("groupModalTitle").textContent = `Group: ${groupName}`;

    fetch(`/api/groups/${groupId}/students`)
        .then(res => res.json())
        .then(students => {
            const list = document.getElementById("group-student-list");
            list.innerHTML = "";

            students.forEach(s => {
                const div = document.createElement("div");
                div.className = "student-entry";
                div.innerHTML = `${s.firstName} ${s.lastName} <button onclick="removeStudent(${groupId}, ${s.id})">❌</button>`;
                list.appendChild(div);
            });

            document.getElementById("add-student-btn").onclick = () => showAddStudentSelect(groupId);

            document.getElementById("groupModal").style.display = "block";
            document.getElementById("groupModalOverlay").style.display = "block";
        })
        .catch(err => {
            console.error(`❌ Failed to load students for group ${groupId}:`, err);
            alert("Error loading students");
        });
}

function removeStudent(groupId, studentId) {
    fetch(`/api/groups/${groupId}/remove-student?studentId=${studentId}`, { method: "DELETE" })
        .then(() => openGroupModal(currentGroupId, currentGroupName));
}

function showAddStudentSelect(groupId) {
    fetch(`/api/groups/${groupId}/students/free`)
        .then(res => res.json())
        .then(students => {
            const container = document.getElementById("add-student-container");
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
    }).then(() => openGroupModal(currentGroupId, currentGroupName));
}

function closeGroupModal() {
    document.getElementById("groupModal").style.display = "none";
    document.getElementById("groupModalOverlay").style.display = "none";
}