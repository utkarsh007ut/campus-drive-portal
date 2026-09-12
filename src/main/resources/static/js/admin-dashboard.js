// Guard clause: if someone opens this page without a valid Admin session,
// send them back to login instead of showing a broken/empty dashboard.
if (!getToken() || getRole() !== "ADMIN") {
    window.location.href = "login.html";
}

document.getElementById("welcomeMsg").textContent = "Welcome, " + localStorage.getItem("name");

async function loadPendingColleges() {
    const container = document.getElementById("pendingColleges");
    try {
        const colleges = await apiFetch("/admin/colleges/pending");
        renderList(container, colleges, "college");
    } catch (err) {
        container.innerHTML = `<div class="empty-state">Error loading colleges: ${err.message}</div>`;
    }
}

async function loadPendingCompanies() {
    const container = document.getElementById("pendingCompanies");
    try {
        const companies = await apiFetch("/admin/companies/pending");
        renderList(container, companies, "company");
    } catch (err) {
        container.innerHTML = `<div class="empty-state">Error loading companies: ${err.message}</div>`;
    }
}

function renderList(container, items, type) {
    if (items.length === 0) {
        const label = type === "company" ? "companies" : `${type}s`;
        container.innerHTML = `<div class="empty-state">No pending ${label} right now.</div>`;
        return;
    }


    container.innerHTML = items.map(item => `
    <div class="card-row">
      <div class="info">
        <strong>${item.name}</strong>
        <span>${item.email}</span>
      </div>
      <div class="actions">
        <button class="btn-success" onclick="decide('${type}', ${item.id}, true)">Approve</button>
        <button class="btn-danger" onclick="decide('${type}', ${item.id}, false)">Reject</button>
      </div>
    </div>
  `).join("");
}

async function decide(type, id, approve) {
    const endpoint = type === "college" ? "/admin/colleges" : "/admin/companies";
    try {
        await apiFetch(`${endpoint}/${id}/decide?approve=${approve}`, { method: "POST" });
        // refresh both lists after any decision, since state changed
        loadPendingColleges();
        loadPendingCompanies();
    } catch (err) {
        alert("Action failed: " + err.message);
    }
}

loadPendingColleges();
loadPendingCompanies();