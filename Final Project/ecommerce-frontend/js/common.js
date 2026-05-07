
function checkSession() {

    const userId = localStorage.getItem("userId");
    const loginTime = localStorage.getItem("loginTime");

    // NOT LOGGED IN
    if (!userId || !loginTime) {

        alert("Please login first");

        window.location.href = "login.html";

        return false;
    }

    const now = Date.now();

    // 30 MINUTES
    const SESSION_LIMIT = 30 * 60 * 1000;

    // SESSION EXPIRED
    if (now - loginTime > SESSION_LIMIT) {

        localStorage.clear();

        alert("Session expired. Please login again.");

        window.location.href = "login.html";

        return false;
    }

    return true;
}


function logout() {

    localStorage.clear();

    alert("Logged out successfully");

    window.location.href = "index.html";
}
function goToLogin() {
    window.location.href = "login.html";
}

function updateNavbar() {

    const userId = localStorage.getItem("userId");

    const loginBtn = document.getElementById("loginBtn");
    const logoutBtn = document.getElementById("logoutBtn");

    if (!loginBtn || !logoutBtn) return;

    if (userId) {

        loginBtn.style.display = "none";
        logoutBtn.style.display = "inline-block";
    } else {

        loginBtn.style.display = "inline-block";
        logoutBtn.style.display = "none";
    }
}
window.addEventListener("load", updateNavbar);