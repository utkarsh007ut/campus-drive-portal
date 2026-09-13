function switchTab(which) {
    document.getElementById("tabCollege").classList.toggle("active", which === "college");
    document.getElementById("tabCompany").classList.toggle("active", which === "company");
    document.getElementById("collegeForm").classList.toggle("hidden", which !== "college");
    document.getElementById("companyForm").classList.toggle("hidden", which !== "company");
}

document.getElementById("collegeForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const messageBox = document.getElementById("message");
    try {
        await apiFetch("/colleges/register", {
            method: "POST",
            body: JSON.stringify({
                name: document.getElementById("collegeName").value,
                email: document.getElementById("collegeEmail").value,
                password: document.getElementById("collegePassword").value,
                address: document.getElementById("collegeAddress").value,
            }),
        });
        messageBox.textContent = "Registered! Awaiting Admin approval before you can log in.";
        messageBox.className = "message success";
    } catch (err) {
        messageBox.textContent = err.message;
        messageBox.className = "message error";
    }
});

document.getElementById("companyForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const messageBox = document.getElementById("message");
    try {
        await apiFetch("/companies/register", {
            method: "POST",
            body: JSON.stringify({
                name: document.getElementById("companyName").value,
                email: document.getElementById("companyEmail").value,
                password: document.getElementById("companyPassword").value,
                website: document.getElementById("companyWebsite").value,
            }),
        });
        messageBox.textContent = "Registered! Awaiting Admin approval before you can log in.";
        messageBox.className = "message success";
    } catch (err) {
        messageBox.textContent = err.message;
        messageBox.className = "message error";
    }
});