document.addEventListener("DOMContentLoaded", () => {
    const role = document.body.getAttribute("data-role");

    if (role === "TEACHER") {
        const createBtn = document.getElementById("create-group-btn");
        if (createBtn) {
            createBtn.onclick = showCreateGroupPrompt;
        }
        loadTeacherStudents();
        loadTeacherGroups();
    }

    if (role === "STUDENT") {
        loadStudentGroups();
    }
});

// ===== TEACHER =====

function loadTeacherStudents() {
    fetch("/api/teacher/students")
        .then(res => res.json())
        .then(students => {
            const container = document.getElementById("teacher-students-container");
            if (!container) return;
            container.innerHTML = "";

            if (students.length === 0) {
                container.innerHTML = "<p>You don't have any students yet.</p>";
                return;
            }

            students.forEach(student => {
                const div = document.createElement("div");
                div.className = "card-content";
                div.innerHTML = `
                    <strong>${student.firstName} ${student.lastName}</strong><br>
                    <small>${student.email}</small>
                    <button class="delete-btn" style="float:right;" onclick="unassignStudent(${student.id})">✖</button>
                `;
                container.appendChild(div);
            });
        })
        .catch(err => {
            const container = document.getElementById("teacher-students-container");
            if (container) {
                container.innerHTML = "<p style='color: red;'>Error loading students.</p>";
            }
        });
}

function loadTeacherGroups() {
    fetch("/api/groups/teacher")
        .then(res => res.json())
        .then(groups => {
            const container = document.getElementById("teacher-groups-container");
            if (!container) return;
            container.innerHTML = "";

            if (groups.length === 0) {
                container.innerHTML = "<p>You don't have any groups yet.</p>";
                return;
            }

            groups.forEach(group => {
                const wrapper = document.createElement("div");
                wrapper.className = "card-content group-card";
                wrapper.style.position = "relative";

                const name = document.createElement("div");
                name.textContent = group.name;
                name.style.cursor = "pointer";
                name.onclick = () => openGroupModal(group.id, group.name); // подключён из groups-modal.js

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
                            .then(() => loadTeacherGroups())
                            .catch(err => alert("Failed to delete group: " + err.message));
                    }
                };

                wrapper.appendChild(name);
                wrapper.appendChild(deleteBtn);
                container.appendChild(wrapper);
            });
        })
        .catch(err => {
            const container = document.getElementById("teacher-groups-container");
            if (container) {
                container.innerHTML = `<p style="color: red;">Error loading groups: ${err.message}</p>`;
            }
        });
}

function showCreateGroupPrompt() {
    const groupName = prompt("Enter group name:");
    if (groupName) {
        fetch("/api/groups", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name: groupName })
        })
            .then(() => loadTeacherGroups())
            .catch(err => alert("Failed to create group: " + err.message));
    }
}

function unassignStudent(studentId) {
    if (confirm("Are you sure you want to unassign this student?")) {
        fetch(`/api/teacher/unassign-student?studentId=${studentId}`, {
            method: "DELETE"
        })
            .then(() => loadTeacherStudents())
            .catch(err => alert("Failed to unassign student: " + err.message));
    }
}

// ===== STUDENT =====

function loadStudentGroups() {
    fetch("/api/student/groups")
        .then(res => {
            if (!res.ok) throw new Error("Network error");
            return res.json();
        })
        .then(groups => {
            const container = document.getElementById("student-group-container");
            if (!container) return;
            container.innerHTML = "";

            if (!groups || groups.length === 0) {
                container.innerHTML = "<p>You are not assigned to any group.</p>";
                return;
            }

            groups.forEach(group => {
                const div = document.createElement("div");
                div.className = "card-content";
                div.innerHTML = `
                    <p><strong>Group Name:</strong> ${group.name}</p>
                    <p><strong>Teacher:</strong> ${group.teacherFullName}</p>
                    <p><strong>Status:</strong> ${group.isActive ? "Active" : "Inactive"}</p>
                `;
                container.appendChild(div);
            });
        })
        .catch(err => {
            const container = document.getElementById("student-group-container");
            if (container) {
                container.innerHTML = "<p>Error loading group info.</p>";
            }
        });
}