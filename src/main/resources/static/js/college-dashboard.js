if (!getToken() || getRole() !== "COLLEGE") window.location.href = "login.html";
document.getElementById("welcomeMsg").textContent = "Welcome, " + localStorage.getItem("name");

function showTab(name) {
    ["students", "drives", "jobs"].forEach(t => {
        document.getElementById(`tab-${t}`).classList.toggle("hidden", t !== name);
    });
    document.querySelectorAll(".tab-btn").forEach(b => b.classList.remove("active"));
    event.target.classList.add("active");
    if (name === "students") loadStudents();
    if (name === "drives") loadDrives();
    if (name === "jobs") loadJobs();
}

async function addStudent() {
    try {
        await apiFetch("/students", {
            method: "POST",
            body: JSON.stringify({
                name: document.getElementById("studentName").value,
                email: document.getElementById("studentEmail").value,
                password: document.getElementById("studentPassword").value,
                cgpa: parseFloat(document.getElementById("studentCgpa").value) || null,
                skills: document.getElementById("studentSkills").value,
            }),
        });
        loadStudents();
    } catch (err) {
        alert("Failed: " + err.message);
    }
}

async function loadStudents() {
    const container = document.getElementById("studentsList");
    try {
        const students = await apiFetch("/students");
        if (students.length === 0) { container.innerHTML = `<div class="empty-state">No students added yet.</div>`; return; }
        container.innerHTML = students.map(s => `
      <div class="card-row">
        <div class="info"><strong>${s.name}</strong><span>${s.email} · CGPA ${s.cgpa ?? "-"} · ${s.skills ?? ""}</span></div>
      </div>`).join("");
    } catch (err) { container.innerHTML = `<div class="empty-state">${err.message}</div>`; }
}

async function loadDrives() {
    const container = document.getElementById("drivesList");
    try {
        const drives = await apiFetch("/drives/college");
        if (drives.length === 0) { container.innerHTML = `<div class="empty-state">No drive requests yet.</div>`; return; }
        container.innerHTML = drives.map(d => `
      <div class="card-row">
        <div class="info">
          <strong>${d.companyName}</strong>
          <span>${d.proposedRequirements}</span>
          <span class="status-badge status-${d.status}">${d.status}</span>
        </div>
        <div class="actions">
          ${d.status === "REQUESTED" ? `
            <button class="btn-success" onclick="acceptDrive(${d.id})">Accept</button>
            <button class="btn-danger" onclick="declineDrive(${d.id})">Decline</button>
          ` : ""}
        </div>
      </div>`).join("");
    } catch (err) { container.innerHTML = `<div class="empty-state">${err.message}</div>`; }
}

async function acceptDrive(id) {
    try { await apiFetch(`/drives/${id}/accept`, { method: "POST" }); loadDrives(); }
    catch (err) { alert("Failed: " + err.message); }
}

async function declineDrive(id) {
    const reason = prompt("Reason for declining this drive:");
    if (!reason) return; // matches backend's mandatory-reason rule - don't even send an empty one
    try { await apiFetch(`/drives/${id}/decline`, { method: "POST", body: JSON.stringify({ declineNote: reason }) }); loadDrives(); }
    catch (err) { alert("Failed: " + err.message); }
}

async function loadJobs() {
    const container = document.getElementById("jobsList");
    try {
        const [drives, allJobs] = await Promise.all([apiFetch("/drives/college"), apiFetch("/jobs")]);
        const accepted = drives.filter(d => d.status === "ACCEPTED");

        if (accepted.length === 0) { container.innerHTML = `<div class="empty-state">No accepted drives yet.</div>`; return; }

        container.innerHTML = accepted.map(d => {
            const job = allJobs.find(j => j.campusDriveId === d.id);
            if (job) {
                return `<div class="card-row">
          <div class="info"><strong>${job.title}</strong><span>Published for ${d.companyName}</span></div>
          <div class="actions"><button class="btn-primary" onclick="viewApplications(${job.id})">View Applications</button></div>
        </div>`;
            }
            return `<div class="card-row">
        <div class="info"><strong>Drive with ${d.companyName}</strong><span>No job published yet</span></div>
        <div class="actions"><button class="btn-success" onclick="publishJob(${d.id})">Publish Job</button></div>
      </div>`;
        }).join("");
    } catch (err) { container.innerHTML = `<div class="empty-state">${err.message}</div>`; }
}

async function publishJob(driveId) {
    const title = prompt("Job title:");
    if (!title) return;
    const minCgpa = prompt("Minimum CGPA required (leave blank for none):");
    try {
        await apiFetch(`/jobs/drive/${driveId}`, {
            method: "POST",
            body: JSON.stringify({ title, requirements: "", minCgpa: minCgpa ? parseFloat(minCgpa) : null }),
        });
        loadJobs();
    } catch (err) { alert("Failed: " + err.message); }
}

async function viewApplications(jobId) {
    try {
        const apps = await apiFetch(`/applications/job/${jobId}`);
        const container = document.getElementById("jobsList");
        if (apps.length === 0) { container.innerHTML = `<div class="empty-state">No applications yet for this job. <button class="btn-primary" onclick="loadJobs()">Back</button></div>`; return; }
        container.innerHTML = `<button class="btn-primary" style="margin-bottom:14px" onclick="loadJobs()">← Back to Jobs</button>` +
            apps.map(a => `
      <div class="card-row">
        <div class="info"><strong>${a.studentName}</strong><span class="status-badge status-${a.status}">${a.status}</span></div>
        <div class="actions">
          ${a.status === "APPLIED" ? `
            <button class="btn-success" onclick="shortlist(${a.id}, ${jobId})">Shortlist</button>
            <button class="btn-danger" onclick="rejectApp(${a.id}, ${jobId})">Reject</button>
          ` : ""}
        </div>
      </div>`).join("");
    } catch (err) { alert("Failed: " + err.message); }
}

async function shortlist(appId, jobId) {
    try { await apiFetch(`/applications/${appId}/shortlist`, { method: "POST" }); viewApplications(jobId); }
    catch (err) { alert("Failed: " + err.message); }
}

async function rejectApp(appId, jobId) {
    const reason = prompt("Reason for rejecting this application:");
    if (!reason) return;
    try { await apiFetch(`/applications/${appId}/reject-by-college`, { method: "POST", body: JSON.stringify({ reason }) }); viewApplications(jobId); }
    catch (err) { alert("Failed: " + err.message); }
}

loadStudents();