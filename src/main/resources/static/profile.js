document.addEventListener("DOMContentLoaded", () => {
    const role = document.body.getAttribute("data-role");

    if (role === "TEACHER") {
        loadTeacherStudents();
        loadTeacherGroups();
        document.getElementById("create-group-btn").onclick = showCreateGroupPrompt;
    } else if (role === "STUDENT") {
        loadStudentGroup();
    }
});

// ===== TEACHER =====

function loadTeacherGroups() {
    fetch("/api/groups/teacher")
        .then(res => res.json())
        .then(groups => {
            const container = document.getElementById("teacher-groups-container");
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
            console.error("Failed to load teacher groups:", err);
            document.getElementById("teacher-groups-container").innerHTML =
                `<p style="color: red;">Error loading groups: ${err.message}</p>`;
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

// ===== STUDENT =====

function loadStudentGroup() {
    fetch("/api/groups/my")
        .then(res => res.json())
        .then(group => {
            const container = document.getElementById("student-group-container");
            if (!group || !group.id) {
                container.innerHTML = "<p>You are not assigned to any group.</p>";
                return;
            }

            container.innerHTML = `
                <p><strong>Group Name:</strong> ${group.name}</p>
                <p><strong>Teacher:</strong> ${group.teacherFullName}</p>
                <p><strong>Status:</strong> ${group.isActive ? "Active" : "Inactive"}</p>
            `;
        })
        .catch(err => {
            console.error("❌ Failed to load student group:", err);
            document.getElementById("student-group-container").innerHTML =
                "<p>Error loading group info.</p>";
        });
}
function loadTeacherStudents() {
    fetch("/api/teacher/students")
        .then(res => res.json())
        .then(students => {
            const container = document.getElementById("teacher-students-container");
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
                    <button class="delete-btn" style="float: right;" onclick="unassignStudent(${student.id})">✖</button>
                `;
                container.appendChild(div);
            });
        })
        .catch(err => {
            console.error("Failed to load students:", err);
            document.getElementById("teacher-students-container").innerHTML =
                "<p style='color: red;'>Error loading students.</p>";
        });
}
function showAssignStudentModal() {
    fetch("/api/teacher/unassigned-students")
        .then(res => res.json())
        .then(students => {
            const container = document.getElementById("assign-student-container");
            container.innerHTML = "";

            if (students.length === 0) {
                container.innerHTML = "<p>No available students.</p>";
                return;
            }

            const select = document.createElement("select");
            students.forEach(s => {
                const option = document.createElement("option");
                option.value = s.id;
                option.textContent = `${s.firstName} ${s.lastName}`;
                select.appendChild(option);
            });

            const btn = document.createElement("button");
            btn.textContent = "Assign";
            btn.className = "btn";
            btn.onclick = () => {
                fetch(`/api/teacher/assign-student?studentId=${select.value}`, {
                    method: "POST"
                })
                    .then(() => {
                        closeAssignStudentModal();
                        loadTeacherStudents();
                    })
                    .catch(err => {
                        alert("Failed to assign student: " + err.message);
                    });
            };

            container.appendChild(select);
            container.appendChild(btn);

            document.getElementById("assignStudentModal").style.display = "block";
            document.getElementById("assignStudentModalOverlay").style.display = "block";
        });
}

function closeAssignStudentModal() {
    document.getElementById("assignStudentModal").style.display = "none";
    document.getElementById("assignStudentModalOverlay").style.display = "none";
}
function unassignStudent(studentId) {
    if (!confirm("Unassign this student?")) return;

    fetch(`/api/teacher/unassign-student?studentId=${studentId}`, {
        method: "DELETE"
    })
        .then(() => loadTeacherStudents())
        .catch(err => {
            alert("Failed to unassign student: " + err.message);
        });
}