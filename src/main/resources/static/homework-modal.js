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

                const gradeInput = `<input type="number" value="${hw.grade ?? ''}" min="0" max="100" 
                                    data-id="${hw.id}" class="grade-input" placeholder="Enter grade..." style="margin-top:5px;"/>`;

                const statusSelect = `
                    <select data-id="${hw.id}" class="status-select" style="margin-top:5px;">
                        <option value="NOT_SUBMITTED" ${hw.status === "NOT_SUBMITTED" ? "selected" : ""}>NOT_SUBMITTED</option>
                        <option value="SUBMITTED" ${hw.status === "SUBMITTED" ? "selected" : ""}>SUBMITTED</option>
                        <option value="CHECKED" ${hw.status === "CHECKED" ? "selected" : ""}>CHECKED</option>
                    </select>
                `;

                div.innerHTML = `
                    <p><strong>Student:</strong> ${hw.studentName || "-"}</p>
                    <p><strong>Status:</strong> <span class="badge badge-${hw.status}">${hw.status}</span></p>
                    <p><strong>Update Status:</strong> ${statusSelect}</p>
                    <p><strong>Grade:</strong> ${gradeInput}</p>
                    <p><strong>Content:</strong> ${hw.content || "–"}</p>
                    <button onclick="submitHomeworkGrade(${hw.id})" class="btn" style="margin-top: 5px;">💾 Save</button>
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

function submitHomeworkGrade(homeworkId) {
    const grade = document.querySelector(`input.grade-input[data-id="${homeworkId}"]`).value;
    const status = document.querySelector(`select.status-select[data-id="${homeworkId}"]`).value;

    fetch(`/api/homeworks/${homeworkId}/grade`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            grade: grade !== "" ? parseInt(grade) : null,
            status: status
        })
    })
        .then(res => {
            if (!res.ok) throw new Error("Failed to submit grade");
            return res.json();
        })
        .then(() => {
            alert("Saved successfully!");
        })
        .catch(err => {
            console.error("Error submitting grade:", err);
            alert("Failed to save homework.");
        });
}

function closeHomeworkGroupModal() {
    document.getElementById("homeworkGroupModal").style.display = "none";
    document.getElementById("homeworkGroupOverlay").style.display = "none";
}