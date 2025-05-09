let currentStartDate = getMonday(new Date());

document.addEventListener("DOMContentLoaded", function () {
    loadLessons();

    document.getElementById("prevWeekBtn").addEventListener("click", () => {
        currentStartDate.setDate(currentStartDate.getDate() - 7);
        loadLessons();
    });

    document.getElementById("nextWeekBtn").addEventListener("click", () => {
        currentStartDate.setDate(currentStartDate.getDate() + 7);
        loadLessons();
    });
});

function loadLessons() {
    const tz = Intl.DateTimeFormat().resolvedOptions().timeZone;
    const isoDate = currentStartDate.toISOString().split("T")[0];

    fetch(`/lessons/api/weekly?startDate=${isoDate}&timeZone=${tz}`)
        .then(res => res.json())
        .then(data => renderLessonsByDay(data));
}

function renderLessonsByDay(data) {
    const container = document.getElementById("weekly-lessons-container");
    container.innerHTML = "";

    const daysOfWeek = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];

    daysOfWeek.forEach(day => {
        const lessons = data[day] || [];
        const dayDiv = document.createElement("div");
        dayDiv.className = "day-section";

        const title = document.createElement("h3");
        title.textContent = day;
        dayDiv.appendChild(title);

        if (lessons.length === 0) {
            const none = document.createElement("p");
            none.textContent = "No lessons.";
            none.style.color = "gray";
            dayDiv.appendChild(none);
        } else {
            lessons.forEach(lesson => {
                const card = document.createElement("div");
                card.className = "card-content";
                card.style.cursor = "pointer";
                card.onclick = () => openLessonModal(lesson.id);

                const date = new Date(lesson.dateUtc);
                const timeString = date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

                const dateText = document.createElement("strong");
                dateText.textContent = timeString;
                card.appendChild(dateText);

                card.appendChild(document.createElement("br"));

                const groupOrStudents = document.createElement("span");
                groupOrStudents.textContent = lesson.groupName || lesson.studentNames.join(", ");
                card.appendChild(groupOrStudents);

                card.appendChild(document.createElement("br"));

                const badge = document.createElement("span");
                badge.className = "badge badge-" + lesson.status.toLowerCase();
                badge.textContent = lesson.status;
                card.appendChild(badge);

                dayDiv.appendChild(card);
            });
        }

        container.appendChild(dayDiv);
    });
}

function getMonday(d) {
    const date = new Date(d);
    const day = date.getDay();
    const diff = date.getDate() - day + (day === 0 ? -6 : 1);
    return new Date(date.setDate(diff));
}