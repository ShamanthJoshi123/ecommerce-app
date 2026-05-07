async function register(event) {
    event.preventDefault();

    const user = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        phone: document.getElementById("phone").value,
        address: document.getElementById("address").value
    };

    await post("/users/register", user);

    alert("Registered Successfully");
    window.location.href = "login.html";
}

async function login(event) {
    event.preventDefault();

    const data = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    const response = await fetch("http://localhost:8080/users/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });

    const result = await response.json();

    console.log("LOGIN RESPONSE:", result);

    localStorage.setItem("userId", result.userId || result.id);


     localStorage.setItem("loginTime", Date.now());

    alert("Login successful");
    window.location.href = "index.html";
}