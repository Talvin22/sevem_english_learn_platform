function showHomeworkDetails(id) {
    fetch(`/api/homeworks/${id}`)
        .then(res => {
            if (!res.ok) throw new Error("Failed to load homework");
            return res.json();
        })
        .then(data => {
            document.getElementById("homework-lesson-date").textContent = formatDate(data.lessonDate);
            document.getElementById("homework-group").textContent = data.groupName ?? "-";
            document.getElementById("homework-status").textContent = data.status;
            document.getElementById("homework-status").className = "badge badge-" + data.status;
            document.getElementById("homework-grade").textContent = data.grade ?? "–";
            document.getElementById("homework-content").textContent = data.content || "–";

            // Show submit button only if homework is NOT_SUBMITTED
            const submitBtn = document.getElementById("submitHomeworkBtn");
            if (submitBtn) {
                if (data.status === "NOT_SUBMITTED") {
                    submitBtn.style.display = "inline-block";
                    submitBtn.onclick = () => submitHomework(id);
                } else {
                    submitBtn.style.display = "none";
                }
            }

            document.getElementById("homeworkModal").style.display = "block";
            document.getElementById("homeworkOverlay").style.display = "block";
        })
        .catch(err => {
            console.error("Error loading homework:", err);
            alert("Could not load homework.");
        });
}

function closeHomeworkModal() {
    document.getElementById("homeworkModal").style.display = "none";
    document.getElementById("homeworkOverlay").style.display = "none";
}

function submitHomework(homeworkId) {
    fetch("/api/homeworks/submit", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ homeworkId })
    })
        .then(res => {
            if (!res.ok) throw new Error("Failed to submit homework");
            return res.text();
        })
        .then(() => {
            alert("Homework submitted!");
            closeHomeworkModal();
            location.reload();
        })
        .catch(err => {
            console.error("Submit error:", err);
            alert("Could not submit homework.");
        });
}

// ====================== TEACHER PART ======================

function openHomeworkGroupModal(lessonId) {
    fetch(`/api/homeworks/by-lesson/${lessonId}`)
        .then(res => {
            if (!res.ok) throw new Error("Failed to fetch homeworks for lesson");
            return res.json();
        })
        .then(data => {
            const homeworks = data.homeworks || data;
            const listContainer = document.getElementById("homeworkGroupList");
            listContainer.innerHTML = "";

            homeworks.forEach(hw => {
                const div = document.createElement("div");
                div.className = "card-content";
                div.style.marginBottom = "10px";

                const statusId = `status-${hw.id}`;
                const gradeId = `grade-${hw.id}`;
                const contentId = `content-${hw.id}`;

                div.innerHTML = `
                    <p><strong>Student:</strong> ${hw.studentName ?? "-"}</p>
                    <p><strong>Status:</strong> <span class="badge badge-${hw.status}">${hw.status}</span></p>

                    <label for="${statusId}">Update Status:</label>
                    <select id="${statusId}">
                        <option value="NOT_SUBMITTED" ${hw.status === "NOT_SUBMITTED" ? "selected" : ""}>NOT_SUBMITTED</option>
                        <option value="SUBMITTED" ${hw.status === "SUBMITTED" ? "selected" : ""}>SUBMITTED</option>
                        <option value="CHECKED" ${hw.status === "CHECKED" ? "selected" : ""}>CHECKED</option>
                    </select>

                    <label for="${gradeId}">Grade:</label>
                    <input type="number" id="${gradeId}" value="${hw.grade ?? ""}" />

                    <label for="${contentId}">Content:</label>
                    <input type="text" id="${contentId}" value="${hw.content ?? ""}" />

                    <button class="btn" onclick="saveHomeworkChanges(${hw.id})">💾 Save</button>
                `;

                listContainer.appendChild(div);
            });

            document.getElementById("homeworkGroupModal").style.display = "block";
            document.getElementById("homeworkGroupOverlay").style.display = "block";
        })
        .catch(err => {
            console.error("Failed to load homeworks by lesson:", err);
            alert("Could not load homeworks.");
        });
}

function closeHomeworkGroupModal() {
    document.getElementById("homeworkGroupModal").style.display = "none";
    document.getElementById("homeworkGroupOverlay").style.display = "none";
}

function saveHomeworkChanges(homeworkId) {
    const status = document.getElementById(`status-${homeworkId}`).value;
    const grade = document.getElementById(`grade-${homeworkId}`).value;
    const content = document.getElementById(`content-${homeworkId}`).value;

    fetch(`/api/homeworks/${homeworkId}/grade`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            homeworkId,
            status,
            grade: grade ? parseInt(grade) : null,
            content: content ?? null
        })
    })
        .then(res => {
            if (!res.ok) throw new Error("Failed to submit grade");
            return res.json();
        })
        .then(() => {
            alert("Updated successfully!");
            location.reload();
        })
        .catch(err => {
            console.error("Error submitting grade: ", err);
            alert("Failed to submit grade");
        });
}

// ============ UTILS ============

function formatDate(dateStr) {
    if (!dateStr) return "-";
    const date = new Date(dateStr);
    if (isNaN(date.getTime())) return "-";
    const pad = n => n.toString().padStart(2, "0");
    return `${pad(date.getDate())}.${pad(date.getMonth() + 1)} ${pad(date.getHours())}:${pad(date.getMinutes())}`;
}