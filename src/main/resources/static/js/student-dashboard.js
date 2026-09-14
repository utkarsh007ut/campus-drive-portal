if (!getToken() || getRole() !== "STUDENT") window.location.href = "login.html";
document.getElementById("welcomeMsg").textContent = "Welcome, " + localStorage.getItem("name");

function showTab(name) {
    ["jobs", "applications"].forEach(t => {
        document.getElementById(`tab-${t}`).classList.toggle("hidden", t !== name);
    });
    document.querySelectorAll(".tab-btn").forEach(b => b.classList.remove("active"));
    document.querySelector(`.tab-btn[data-tab="${name}"]`).classList.add("active");
    if (name === "applications") loadApplications();
    if (name === "jobs") loadJobs();
}

async function loadJobs() {
    const container = document.getElementById("jobsList");
    try {
        const [jobs, myApps] = await Promise.all([apiFetch("/jobs"), apiFetch("/applications/student")]);
        const appliedJobIds = new Set(myApps.map(a => a.jobId));

        if (jobs.length === 0) { container.innerHTML = `<div class="empty-state">No jobs published yet.</div>`; return; }

        container.innerHTML = jobs.map(j => `
      <div class="card-row">
        <div class="info">
          <strong>${j.title}</strong>
          <span>${j.companyName} via ${j.collegeName} · Min CGPA: ${j.minCgpa ?? "None"}</span>
        </div>
        <div class="actions">
          <button class="btn-primary" ${appliedJobIds.has(j.id) ? "disabled" : ""} onclick="apply(${j.id})">
            ${appliedJobIds.has(j.id) ? "Applied" : "Apply"}
          </button>
        </div>
      </div>`).join("");
    } catch (err) { container.innerHTML = `<div class="empty-state">${err.message}</div>`; }
}

async function apply(jobId) {
    try { await apiFetch(`/applications/job/${jobId}`, { method: "POST" }); loadJobs(); }
    catch (err) { alert("Failed: " + err.message); }
}

async function loadApplications() {
    const container = document.getElementById("appsList");
    try {
        const apps = await apiFetch("/applications/student");
        if (apps.length === 0) { container.innerHTML = `<div class="empty-state">You haven't applied to any jobs yet.</div>`; return; }
        container.innerHTML = apps.map(a => `
      <div class="card-row">
        <div class="info">
          <strong>${a.jobTitle}</strong>
          <span class="status-badge status-${a.status}">${a.status}</span>
          ${a.rejectionReason ? `<span>Reason: ${a.rejectionReason}</span>` : ""}
        </div>
      </div>`).join("");
    } catch (err) { container.innerHTML = `<div class="empty-state">${err.message}</div>`; }
}

loadJobs();