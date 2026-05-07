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

    try {
        const response = await fetch("http://localhost:8080/users/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();

        // FIX: Check if the response was successful (status 200-299)
        if (response.ok) {
            console.log("LOGIN SUCCESS:", result);

            // Store the ID correctly depending on how your backend sends it
            localStorage.setItem("userId", result.userId || result.id);
            localStorage.setItem("loginTime", Date.now());

            alert("Login successful");
            window.location.href = "index.html";
        } else {
            // FIX: If the backend sent an error (like 400), show the error message
            console.error("LOGIN FAILED:", result);
            alert("Login failed: " + (result.error || "Invalid credentials"));
        }
    } catch (error) {
        console.error("NETWORK ERROR:", error);
        alert("An error occurred. Please check if the backend server is running.");
    }
}
