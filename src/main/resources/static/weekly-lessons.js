document.addEventListener("DOMContentLoaded", function () {
    const container = document.getElementById("weekly-lessons-container");
    const prevBtn = document.getElementById("prevWeekBtn");
    const nextBtn = document.getElementById("nextWeekBtn");

    let currentStartDate = getStartOfWeek(new Date());

    function getStartOfWeek(date) {
        const day = date.getDay(); // 0 (Sun) to 6 (Sat)
        const diff = (day === 0 ? -6 : 1) - day;
        const start = new Date(date);
        start.setDate(date.getDate() + diff);
        start.setHours(0, 0, 0, 0);
        return start;
    }

    function formatDateISO(date) {
        return date.toISOString().split("T")[0];
    }

    function renderLessonsByWeek(startDate) {
        const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
        const startISO = formatDateISO(startDate);

        fetch(`/lessons/api/weekly?startDate=${startISO}&timeZone=${encodeURIComponent(timeZone)}`)
            .then(res => res.json())
            .then(data => {
                container.innerHTML = "";

                const daysOfWeek = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"];
                daysOfWeek.forEach(day => {
                    const section = document.createElement("div");
                    const title = document.createElement("h3");
                    title.textContent = capitalizeFirstLetter(day.toLowerCase());
                    section.appendChild(title);

                    const lessons = data[day];
                    if (lessons && lessons.length > 0) {
                        lessons.forEach(lesson => {
                            const date = new Date(lesson.dateUtc);
                            const time = date.toLocaleTimeString("de-DE", { hour: "2-digit", minute: "2-digit" });
                            const formattedDate = date.toLocaleDateString("de-DE", { day: "2-digit", month: "2-digit" });

                            const lessonCard = document.createElement("div");
                            lessonCard.className = "card-content";
                            lessonCard.style.cursor = "pointer";
                            lessonCard.onclick = () => openLessonModal(lesson.id);

                            lessonCard.innerHTML = `
                                <strong>${formattedDate} ${time}</strong><br>
                                <span>${lesson.groupName || lesson.studentNames.join(", ")}</span><br>
                                <span class="badge badge-${lesson.status.toLowerCase()}">${lesson.status}</span>
                            `;

                            section.appendChild(lessonCard);
                        });
                    } else {
                        const noLessons = document.createElement("p");
                        noLessons.textContent = "No lessons.";
                        section.appendChild(noLessons);
                    }

                    container.appendChild(section);
                });
            });
    }

    function capitalizeFirstLetter(str) {
        return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
    }

    prevBtn.addEventListener("click", function () {
        currentStartDate.setDate(currentStartDate.getDate() - 7);
        renderLessonsByWeek(currentStartDate);
    });

    nextBtn.addEventListener("click", function () {
        currentStartDate.setDate(currentStartDate.getDate() + 7);
        renderLessonsByWeek(currentStartDate);
    });

    renderLessonsByWeek(currentStartDate);
});