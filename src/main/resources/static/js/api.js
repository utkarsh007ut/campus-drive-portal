const API_BASE = "/api"; // relative path now - same origin as the backend, no need for the full URL

function saveSession(token, role, userId, name) {
    localStorage.setItem("token", token);
    localStorage.setItem("role", role);
    localStorage.setItem("userId", userId);
    localStorage.setItem("name", name);
}

function getToken() {
    return localStorage.getItem("token");
}

function getRole() {
    return localStorage.getItem("role");
}

function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

async function apiFetch(path, options = {}) {
    const headers = options.headers || {};
    headers["Content-Type"] = "application/json";

    const token = getToken();
    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    const response = await fetch(API_BASE + path, { ...options, headers });
    const data = await response.json().catch(() => null);

    if (!response.ok) {
        throw new Error((data && (data.error || JSON.stringify(data.errors))) || "Request failed");
    }
    return data;
}