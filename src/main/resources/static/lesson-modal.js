let selectedLessonId = null;

function closeModal() {
    document.getElementById("lessonModal").style.display = "none";
    document.getElementById("modalOverlay").style.display = "none";
}

function openLessonModal(lessonId) {
    const userRole = document.body.dataset.role;
    const isTeacher = userRole === "TEACHER";

    console.log("ROLE:", userRole);

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

            // Управление статусом урока
            if (isTeacher) {
                document.getElementById("statusSelect").value = data.status || "PLANNED";
                document.getElementById("cancelledBySelect").value = data.cancelledBy || "TEACHER";
                document.getElementById("cancelReasonSelect").value = data.cancelingReason || "VALID_REASON";
                toggleCancelFields(data.status === "CANCELLED");
            } else {
                document.getElementById("statusBlock").style.display = "none";
                document.getElementById("cancelFields").style.display = "none";
            }

            // Посещаемость
            const attendanceFields = document.getElementById("attendanceFields");
            attendanceFields.innerHTML = "";

            if (data.participants && data.participants.length > 0) {
                data.participants.forEach(p => {
                    const wrapper = document.createElement("div");
                    wrapper.style.marginTop = "6px";

                    const label = document.createElement("label");
                    label.textContent = p.name + ": ";
                    label.style.marginRight = "8px";

                    if (isTeacher) {
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
                    } else {
                        const span = document.createElement("span");
                        span.textContent = p.attendanceStatus;
                        wrapper.appendChild(label);
                        wrapper.appendChild(span);
                    }

                    attendanceFields.appendChild(wrapper);
                });
            }

            // Кнопки обновления и удаления
            const btnBlock = document.getElementById("modal-buttons");
            if (btnBlock) {
                btnBlock.style.display = isTeacher ? "flex" : "none";
            }

            document.getElementById("lessonModal").style.display = "block";
            document.getElementById("modalOverlay").style.display = "block";
        });
}

document.getElementById("statusSelect").addEventListener("change", function () {
    toggleCancelFields(this.value === "CANCELLED");
});

function toggleCancelFields(show) {
    document.getElementById("cancelFields").style.display = show ? "block" : "none";
}

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

function confirmDeleteLesson() {
    if (confirm("Are you sure you want to delete this lesson?")) {
        fetch(`/lessons/api/lesson/${selectedLessonId}`, {
            method: "DELETE"
        }).then(res => {
            if (res.ok) {
                closeModal();
                location.reload();
            } else {
                alert("Error deleting lesson");
            }
        });
    }
}