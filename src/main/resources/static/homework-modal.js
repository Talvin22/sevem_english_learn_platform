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

                div.innerHTML = `
                    <p><strong>Student:</strong> ${hw.studentName || "-"}</p>
                    <p><strong>Status:</strong> <span class="badge badge-${hw.status}">${hw.status}</span></p>
                    <p><strong>Grade:</strong> ${hw.grade ?? "–"}</p>
                    <p><strong>Content:</strong> ${hw.content || "–"}</p>
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