document.addEventListener("DOMContentLoaded", () => {
    loadGroups();
});

function loadGroups() {
    fetch("/api/groups/teacher")
        .then(res => res.json())
        .then(groups => {
            const container = document.getElementById("teacher-groups-container");
            container.innerHTML = "";
            groups.forEach(group => {
                const div = document.createElement("div");
                div.className = "card-content group-card";
                div.textContent = group.name;
                div.onclick = () => openGroupModal(group.id);
                container.appendChild(div);
            });
        });
}

function openGroupModal(groupId) {
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
            document.getElementById("modalOverlay").style.display = "block";
        });
}

function removeStudent(groupId, studentId) {
    fetch(`/api/groups/${groupId}/remove-student?studentId=${studentId}`, { method: "DELETE" })
        .then(() => openGroupModal(groupId));
}

function showAddStudentSelect(groupId) {
    fetch("/api/students/free")
        .then(res => res.json())
        .then(students => {
            const select = document.createElement("select");
            students.forEach(s => {
                const option = document.createElement("option");
                option.value = s.id;
                option.textContent = `${s.firstName} ${s.lastName}`;
                select.appendChild(option);
            });

            const btn = document.createElement("button");
            btn.textContent = "Add";
            btn.onclick = () => addStudentToGroup(groupId, select.value);

            const container = document.getElementById("add-student-container");
            container.innerHTML = "";
            container.appendChild(select);
            container.appendChild(btn);
        });
}

function addStudentToGroup(groupId, studentId) {
    fetch(`/api/groups/${groupId}/add-student`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ studentId })
    }).then(() => openGroupModal(groupId));
}

function closeGroupModal() {
    document.getElementById("groupModal").style.display = "none";
    document.getElementById("modalOverlay").style.display = "none";
}
