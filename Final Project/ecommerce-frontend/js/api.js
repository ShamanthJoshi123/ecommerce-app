const BASE_URL = "http://localhost:8080";

async function post(url, data) {
    const response = await fetch(BASE_URL + url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });

    return response.json();
}