function openHomeworkGroupModal(lessonId) {
    fetch(`/api/homeworks/by-lesson/${lessonId}`)
        .then(res => {
            if (!res.ok) {
                throw new Error("Failed to fetch homeworks for lesson");
            }
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

                const statusSelectId = `status-${hw.id}`;
                const gradeInputId = `grade-${hw.id}`;
                const contentInputId = `content-${hw.id}`;

                div.innerHTML = `
                    <p><strong>Student:</strong> ${hw.studentName || "-"}</p>
                    <p><strong>Status:</strong> <span class="badge badge-${hw.status}">${hw.status}</span></p>
                    <label for="${statusSelectId}">Update Status:</label>
                    <select id="${statusSelectId}">
                        <option value="NOT_SUBMITTED" ${hw.status === 'NOT_SUBMITTED' ? 'selected' : ''}>NOT_SUBMITTED</option>
                        <option value="SUBMITTED" ${hw.status === 'SUBMITTED' ? 'selected' : ''}>SUBMITTED</option>
                        <option value="CHECKED" ${hw.status === 'CHECKED' ? 'selected' : ''}>CHECKED</option>
                    </select>
                    <br/>
                    <label for="${gradeInputId}">Grade:</label>
                    <input type="number" id="${gradeInputId}" value="${hw.grade ?? ''}" />
                    <br/>
                    <label for="${contentInputId}">Content:</label>
                    <input type="text" id="${contentInputId}" value="${hw.content || ''}" />
                    <br/>
                    <button onclick="submitHomeworkUpdate(${hw.id})" class="btn">💾 Save</button>
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

function submitHomeworkUpdate(homeworkId) {
    const status = document.getElementById(`status-${homeworkId}`).value;
    const grade = document.getElementById(`grade-${homeworkId}`).value;
    const content = document.getElementById(`content-${homeworkId}`).value;

    const payload = {
        homeworkId,
        status,
        grade: grade ? parseInt(grade) : null,
        content: content || null
    };

    fetch(`/api/homeworks/grade`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
        .then(res => {
            if (!res.ok) {
                throw new Error("Failed to submit grade");
            }
            return res.json();
        })
        .then(updated => {
            alert("Saved ✅");
            console.log("Updated:", updated);
        })
        .catch(err => {
            console.error("Error submitting grade: ", err);
            alert("Failed to submit grade");
        });
}

function closeHomeworkGroupModal() {
    document.getElementById("homeworkGroupModal").style.display = "none";
    document.getElementById("homeworkGroupOverlay").style.display = "none";
}