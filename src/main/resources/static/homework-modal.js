function showHomeworkDetails(homeworkId) {
    fetch(`/api/homeworks/${homeworkId}`)
        .then(res => res.json())
        .then(hw => {
            document.getElementById("homework-lesson-date").textContent = hw.lessonDate || "–";
            document.getElementById("homework-group").textContent = hw.groupName || "–";
            document.getElementById("homework-status").textContent = hw.status;
            document.getElementById("homework-status").className = "badge badge-" + hw.status.toLowerCase();
            document.getElementById("homework-grade").textContent = hw.grade ?? "–";
            document.getElementById("homework-content").textContent = hw.content || "–";

            document.getElementById("homeworkModal").style.display = "block";
            document.getElementById("homeworkOverlay").style.display = "block";
        })
        .catch(err => {
            console.error("Failed to load homework details", err);
            alert("Failed to load homework details.");
        });
}

function closeHomeworkModal() {
    document.getElementById("homeworkModal").style.display = "none";
    document.getElementById("homeworkOverlay").style.display = "none";
}