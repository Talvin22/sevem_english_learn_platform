document.addEventListener("DOMContentLoaded", () => {
    const role = document.body.getAttribute("data-role");
    if (role === "TEACHER") {
        loadTeacherGroups();
        document.getElementById("create-group-btn").onclick = createGroup;
    } else if (role === "STUDENT") {
        loadStudentGroup();
    }
});

// ===== TEACHER ====

function loadTeacherGroups() {
    fetch("/api/groups/teacher")
        .then(res => res.json())
        .then(groups => {
            const container = document.getElementById("teacher-groups");
            container.innerHTML = "";

            if (groups.length === 0) {
                container.innerHTML = "<p>You don't have any groups yet.</p>";
                return;
            }

            groups.forEach(group => {
                const div = document.createElement("div");
                div.className = "card-content";
                div.innerHTML = `
                    <strong>${group.name}</strong> (${group.isActive ? "active" : "inactive"})
                    <button onclick="deleteGroup(${group.id})" class="btn danger" style="float: right;">🗑</button>
                `;
                container.appendChild(div);
            });
        })
        .catch(err => {
            console.error("Failed to load teacher groups:", err);
        });
}

function createGroup() {
    const input = document.getElementById("new-group-name");
    const name = input.value.trim();

    if (!name) {
        alert("Group name cannot be empty.");
        return;
    }

    fetch("/api/groups/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name })
    })
        .then(res => {
            if (!res.ok) throw new Error("Failed to create group");
            return res.json();
        })
        .then(() => {
            input.value = "";
            loadTeacherGroups();
        })
        .catch(err => {
            console.error("❌ Failed to create group:", err);
            alert("Could not create group.");
        });
}

function deleteGroup(groupId) {
    if (!confirm("Are you sure you want to delete this group?")) return;

    fetch(`/api/groups/${groupId}`, {
        method: "DELETE"
    })
        .then(() => loadTeacherGroups())
        .catch(err => {
            console.error("❌ Failed to delete group:", err);
            alert("Could not delete group.");
        });
}

// ===== STUDENT =====

function loadStudentGroup() {
    fetch("/api/groups/my")
        .then(res => res.json())
        .then(group => {
            const container = document.getElementById("student-group");
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
            document.getElementById("student-group").innerHTML = "<p>Error loading group info.</p>";
        });
}