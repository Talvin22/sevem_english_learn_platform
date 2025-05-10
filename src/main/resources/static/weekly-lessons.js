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

    function formatDisplayDate(date) {
        return date.toLocaleDateString("de-DE", {
            day: "2-digit",
            month: "2-digit",
            year: "numeric"
        });
    }

    function isSameDay(date1, date2) {
        return (
            date1.getFullYear() === date2.getFullYear() &&
            date1.getMonth() === date2.getMonth() &&
            date1.getDate() === date2.getDate()
        );
    }

    // ...

    function renderLessonsByWeek(startDate) {
        const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
        const startISO = formatDateISO(startDate);

        const endDate = new Date(startDate);
        endDate.setDate(endDate.getDate() + 6);

        const header = document.querySelector("h2");
        header.textContent = `Lessons (${formatDisplayDate(startDate)} – ${formatDisplayDate(endDate)})`;

        fetch(`/lessons/api/weekly?startDate=${startISO}&timeZone=${encodeURIComponent(timeZone)}`)
            .then(res => res.json())
            .then(data => {
                container.innerHTML = "";

                const daysOfWeek = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"];
                const today = new Date();
                const todayStr = daysOfWeek[today.getDay() === 0 ? 6 : today.getDay() - 1];

                daysOfWeek.forEach(day => {
                    const section = document.createElement("div");
                    const title = document.createElement("h3");
                    title.textContent = capitalizeFirstLetter(day.toLowerCase());

                    if (day === todayStr && today >= startDate && today <= endDate) {
                        title.style.color = "#2563eb";
                    }

                    section.appendChild(title);

                    const lessons = data[day];
                    if (lessons && lessons.length > 0) {
                        lessons.forEach(lesson => {
                            const date = new Date(lesson.dateUtc);
                            const time = date.toLocaleTimeString("de-DE", { hour: "2-digit", minute: "2-digit" });
                            const formattedDate = date.toLocaleDateString("de-DE", { day: "2-digit", month: "2-digit" });

                            const isToday = isSameDay(today, date);
                            const todayIcon = isToday ? '🟢 ' : '';

                            const lessonCard = document.createElement("div");
                            lessonCard.className = "card-content";
                            lessonCard.style.cursor = "pointer";
                            lessonCard.onclick = () => openLessonModal(lesson.id);

                            if (isToday) {
                                lessonCard.style.backgroundColor = "#e6f0ff";
                                lessonCard.style.border = "1px solid #3399ff";
                            }

                            lessonCard.innerHTML = `
                            <strong>${todayIcon}${formattedDate} ${time}</strong><br>
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