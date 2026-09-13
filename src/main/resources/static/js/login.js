function selectRole(role) {
    document.getElementById("loginTitle").textContent = role + " Login";
    document.querySelectorAll(".tab-btn").forEach(b => b.classList.remove("active"));
    event.target.classList.add("active");
}

document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault(); // stop the browser's default full-page-reload form submit

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const messageBox = document.getElementById("message");

    try {
        const data = await apiFetch("/auth/login", {
            method: "POST",
            body: JSON.stringify({ email, password }),
        });

        saveSession(data.token, data.role, data.userId, data.name);

        const redirects = {
            ADMIN: "admin-dashboard.html",
            COLLEGE: "college-dashboard.html",
            COMPANY: "company-dashboard.html",
            STUDENT: "student-dashboard.html",
        };
        window.location.href = redirects[data.role];
    } catch (err) {
        messageBox.textContent = err.message;
        messageBox.className = "message error";
    }
});