if (!getToken() || getRole() !== "COMPANY") window.location.href = "login.html";
document.getElementById("welcomeMsg").textContent = "Welcome, " + localStorage.getItem("name");

let selectedCollegeFilter = null;

function showTab(name) {
    ["propose", "drives", "applications"].forEach(t => {
        document.getElementById(`tab-${t}`).classList.toggle("hidden", t !== name);
    });
    document.querySelectorAll(".tab-btn[data-tab]").forEach(b => b.classList.remove("active"));
    document.querySelector(`.tab-btn[data-tab="${name}"]`).classList.add("active");
    if (name === "propose") loadColleges();
    if (name === "drives") { selectedCollegeFilter = null; loadDrives(); }
    if (name === "applications") loadApplications();
}

async function loadColleges() {
    const select = document.getElementById("collegeSelect");
    try {
        const colleges = await apiFetch("/colleges/approved");
        if (colleges.length === 0) {
            select.innerHTML = `<option disabled>No approved colleges available yet.</option>`;
            return;
        }
        select.innerHTML = colleges.map(c => `<option value="${c.id}">${c.name}</option>`).join("");
    } catch (err) {
        select.innerHTML = `<option disabled>${err.message}</option>`;
    }
}

async function proposeDrive() {
    const select = document.getElementById("collegeSelect");
    const selectedOptions = [...select.selectedOptions];
    const requirements = document.getElementById("requirements").value;

    if (selectedOptions.length === 0) { alert("Select at least one college."); return; }
    if (!requirements.trim()) { alert("Enter the drive requirements."); return; }

    let succeeded = [], failed = [];
    for (const option of selectedOptions) {
        try {
            await apiFetch("/drives", {
                method: "POST",
                body: JSON.stringify({ collegeId: parseInt(option.value), proposedRequirements: requirements }),
            });
            succeeded.push(option.textContent);
        } catch (err) {
            failed.push(`${option.textContent} (${err.message})`);
        }
    }

    let summary = "";
    if (succeeded.length) summary += `Sent to: ${succeeded.join(", ")}\n`;
    if (failed.length) summary += `Failed for: ${failed.join(", ")}`;
    alert(summary);

    showTab("drives");
}

async function loadDrives() {
    const listContainer = document.getElementById("drivesList");
    const filterContainer = document.getElementById("collegeFilterBar");
    try {
        const drives = await apiFetch("/drives/company");

        if (drives.length === 0) {
            filterContainer.innerHTML = "";
            listContainer.innerHTML = `<div class="empty-state">No drives proposed yet.</div>`;
            return;
        }

        const collegesMap = new Map();
        drives.forEach(d => collegesMap.set(d.collegeId, d.collegeName));

        filterContainer.innerHTML = `
      <button class="tab-btn ${selectedCollegeFilter === null ? "active" : ""}" onclick="filterDrivesByCollege(null)">All Colleges</button>
      ${[...collegesMap.entries()].map(([id, name]) => `
        <button class="tab-btn ${selectedCollegeFilter === id ? "active" : ""}" onclick="filterDrivesByCollege(${id})">${name}</button>
      `).join("")}
    `;

        const filtered = selectedCollegeFilter === null ? drives : drives.filter(d => d.collegeId === selectedCollegeFilter);

        listContainer.innerHTML = filtered.map(d => `
      <div class="card-row">
        <div class="info">
          <strong>${d.collegeName}</strong>
          <span>${d.proposedRequirements}</span>
          ${d.declineNote ? `<span>Decline reason: ${d.declineNote}</span>` : ""}
          <span class="status-badge status-${d.status}">${d.status}</span>
        </div>
      </div>`).join("");
    } catch (err) {
        listContainer.innerHTML = `<div class="empty-state">${err.message}</div>`;
    }
}

function filterDrivesByCollege(collegeId) {
    selectedCollegeFilter = collegeId;
    loadDrives();
}

async function loadApplications() {
    const container = document.getElementById("appsList");
    try {
        const myJobs = await apiFetch("/jobs/company");

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

loadColleges();