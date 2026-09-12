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

        window.location.href = "admin-dashboard.html";

        messageBox.textContent = `Logged in as ${data.name} (${data.role})`;
        messageBox.className = "message success";

        // Dashboards don't exist yet - next step. For now, just prove login works.
    } catch (err) {
        messageBox.textContent = err.message;
        messageBox.className = "message error";
    }
});