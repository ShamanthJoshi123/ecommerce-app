
 const rupee = "\u20B9";

function goToPayment() {

    const name = document.getElementById("name").value;
    const phone = document.getElementById("phone").value;
    const address = document.getElementById("address").value;
    const city = document.getElementById("city").value;

    // VALIDATION
    if (!name || !phone || !address || !city) {
        alert("Please fill all details");
        return;
    }

    // SAVE DATA (USED IN PAYMENT PAGE)
    localStorage.setItem("checkoutData", JSON.stringify({
        name: name,
        phone: phone,
        address: address,
        city: city
    }));

    // REDIRECT TO PAYMENT PAGE
    window.location.href = "payment.html";
}


async function loadSummary() {

    const userId = localStorage.getItem("userId");

    if (!userId) return;

    try {

        const response = await fetch(`http://localhost:8080/cart/${userId}`);
        const data = await response.json();

        const items = data.data?.items || [];

        let itemsTotal = 0;
        let discount = 0;

        items.forEach(item => {

            if (!item.product) return;

            itemsTotal += item.product.price * item.quantity;
        });

        // DISCOUNT LOGIC
        if (itemsTotal > 50000) {
            discount = 2000;
        }

        const total = itemsTotal - discount;

        // UPDATE UI
        document.getElementById("itemsTotal").innerText = rupee+itemsTotal;
        document.getElementById("discount").innerText = rupee+discount;
        document.getElementById("total").innerText = rupee+total;

    } catch (error) {
        console.error("Error loading summary:", error);
    }
}

window.onload = function () {

    if (!checkSession()) return;

    loadSummary();
};