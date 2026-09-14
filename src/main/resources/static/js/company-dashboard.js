if (!getToken() || getRole() !== "COMPANY") window.location.href = "login.html";
document.getElementById("welcomeMsg").textContent = "Welcome, " + localStorage.getItem("name");

function showTab(name) {
    ["propose", "drives", "applications"].forEach(t => {
        document.getElementById(`tab-${t}`).classList.toggle("hidden", t !== name);
    });
    document.querySelectorAll(".tab-btn").forEach(b => b.classList.remove("active"));
    document.querySelector(`.tab-btn[data-tab="${name}"]`).classList.add("active");
    if (name === "drives") loadDrives();
    if (name === "applications") loadApplications();
}

async function proposeDrive() {
    try {
        await apiFetch("/drives", {
            method: "POST",
            body: JSON.stringify({
                collegeId: parseInt(document.getElementById("collegeId").value),
                proposedRequirements: document.getElementById("requirements").value,
            }),
        });
        alert("Drive request sent!");
        showTab("drives");
    } catch (err) { alert("Failed: " + err.message); }
}

async function loadDrives() {
    const container = document.getElementById("drivesList");
    try {
        const drives = await apiFetch("/drives/company");
        if (drives.length === 0) { container.innerHTML = `<div class="empty-state">No drives proposed yet.</div>`; return; }
        container.innerHTML = drives.map(d => `
      <div class="card-row">
        <div class="info">
          <strong>${d.collegeName}</strong>
          <span>${d.proposedRequirements}</span>
          ${d.declineNote ? `<span>Decline reason: ${d.declineNote}</span>` : ""}
          <span class="status-badge status-${d.status}">${d.status}</span>
        </div>
      </div>`).join("");
    } catch (err) { container.innerHTML = `<div class="empty-state">${err.message}</div>`; }
}

// NOTE: filters client-side by matching your own company name against each
// job's companyName, since there's no dedicated "my jobs" backend endpoint yet.
async function loadApplications() {
    const container = document.getElementById("appsList");
    const myName = localStorage.getItem("name");
    try {
        const jobs = await apiFetch("/jobs");
        const myJobs = jobs.filter(j => j.companyName === myName);

        if (myJobs.length === 0) { container.innerHTML = `<div class="empty-state">No jobs published for your drives yet.</div>`; return; }

        let allRows = "";
        for (const job of myJobs) {
            const apps = await apiFetch(`/applications/job/${job.id}`);
            const forwarded = apps.filter(a => a.status === "FORWARDED_TO_COMPANY");
            forwarded.forEach(a => {
                allRows += `<div class="card-row">
          <div class="info"><strong>${a.studentName}</strong><span>${job.title}</span></div>
          <div class="actions">
            <button class="btn-success" onclick="decide(${a.id})">Select</button>
            <button class="btn-danger" onclick="decide(${a.id}, false)">Reject</button>
          </div>
        </div>`;
            });
        }
        container.innerHTML = allRows || `<div class="empty-state">No applications forwarded yet.</div>`;
    } catch (err) { container.innerHTML = `<div class="empty-state">${err.message}</div>`; }
}

async function decide(appId, select = true) {
    try { await apiFetch(`/applications/${appId}/company-decision?select=${select}`, { method: "POST" }); loadApplications(); }
    catch (err) { alert("Failed: " + err.message); }
}