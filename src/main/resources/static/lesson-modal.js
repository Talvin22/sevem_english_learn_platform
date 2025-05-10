let selectedLessonId = null;

function openLessonModal(lessonId) {
    fetch(`/lessons/api/lesson?id=${lessonId}`)
        .then(res => res.json())
        .then(data => {
            selectedLessonId = data.id;

            document.getElementById("modal-teacher").textContent = data.teacherName || "–";
            document.getElementById("modal-group").textContent = data.groupName || "–";
            document.getElementById("modal-students").textContent = data.studentNames?.join(", ") || "–";

            const statusElem = document.getElementById("modal-status");
            statusElem.textContent = data.status;
            statusElem.className = "badge badge-" + data.status.toLowerCase();

            document.getElementById("statusSelect").value = data.status || "PLANNED";
            document.getElementById("cancelledBySelect").value = data.cancelledBy || "TEACHER";
            document.getElementById("cancelReasonSelect").value = data.cancelingReason || "VALID_REASON";
            toggleCancelFields(data.status === "CANCELLED");

            // === Attendance UI ===
            const attendanceFields = document.getElementById("attendanceFields");
            attendanceFields.innerHTML = "";

            if (data.participants && data.participants.length > 0) {
                data.participants.forEach(p => {
                    const wrapper = document.createElement("div");
                    wrapper.style.marginTop = "6px";

                    const label = document.createElement("label");
                    label.textContent = p.name + ": ";
                    label.style.marginRight = "8px";

                    const select = document.createElement("select");
                    select.dataset.studentId = p.studentId;
                    ["ATTENDED", "CANCELLED", "PLANNED"].forEach(status => {
                        const option = document.createElement("option");
                        option.value = status;
                        option.textContent = status;
                        if (p.attendanceStatus === status) {
                            option.selected = true;
                        }
                        select.appendChild(option);
                    });

                    wrapper.appendChild(label);
                    wrapper.appendChild(select);
                    attendanceFields.appendChild(wrapper);
                });
            }

            document.getElementById("lessonModal").style.display = "block";
            document.getElementById("modalOverlay").style.display = "block";
        });
}

function closeModal() {
    document.getElementById("lessonModal").style.display = "none";
    document.getElementById("modalOverlay").style.display = "none";
}

function toggleCancelFields(show) {
    document.getElementById("cancelFields").style.display = show ? "block" : "none";
}

document.getElementById("statusSelect").addEventListener("change", function () {
    toggleCancelFields(this.value === "CANCELLED");
});

function updateLesson() {
    const status = document.getElementById("statusSelect").value;

    const participants = Array.from(document.querySelectorAll("#attendanceFields select")).map(select => {
        return {
            studentId: select.dataset.studentId,
            attendanceStatus: select.value
        };
    });

    const payload = {
        lessonId: selectedLessonId,
        status: status,
        cancelledBy: document.getElementById("cancelledBySelect").value,
        cancelingReason: document.getElementById("cancelReasonSelect").value,
        participants: participants
    };

    fetch("/lessons/api/lesson/update", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(payload)
    }).then(() => {
        closeModal();
        location.reload();
    });
}

// === Format lesson dates on homepage cards ===
document.addEventListener("DOMContentLoaded", () => {
    const dateElements = document.querySelectorAll(".lesson-date");

    dateElements.forEach(el => {
        const raw = el.getAttribute("data-date");
        if (!raw) return;

        const formatted = new Date(raw).toLocaleString("de-DE", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit"
        });

        el.textContent = formatted;
    });
});