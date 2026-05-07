
function checkLogin() {
    const userId = localStorage.getItem("userId");

    if (!userId) {
        alert("Please login first");
        window.location.href = "login.html";
        return null;
    }
    return userId;
}

 const rupee = "\u20B9";
async function loadOrders() {

    const userId = checkLogin();
    if (!userId) return;

    try {

        const response = await fetch(
            `http://localhost:8080/orders/user/${userId}`
        );

        if (!response.ok) {
            throw new Error("Failed to load orders");
        }

        const orders = await response.json();

        console.log("ORDERS:", orders);

        let html = "";

        if (!orders || orders.length === 0) {
            html = "<h3>No orders found</h3>";
        }

        orders.forEach(order => {

            html += `
                <div class="order-card">

                    <div class="order-header">
                        <div>
                            <p><b>Order ID:</b> ${order.orderId}</p>
                            <p><b>Date:</b> ${new Date(order.orderDate).toLocaleString()}</p>
                        </div>

                        <div>
                            <p><b>Total:</b> ${rupee}${order.totalAmount}</p>
                            <p><b>Status:</b> ${order.status}</p>
                        </div>
                    </div>
            `;


            const items = order.items || [];

            items.forEach(item => {

                const product = item.product || {};

                html += `
                    <div class="order-item">

                        <img src="${product.imageUrl || 'https://via.placeholder.com/100'}" />

                        <div>
                            <p><b>${product.name || 'Product'}</b></p>
                            <p>${rupee}${item.price}</p>
                            <p>Qty: ${item.quantity}</p>
                        </div>

                    </div>
                `;
            });

            html += `</div>`;
        });

        document.getElementById("orders-container").innerHTML = html;

    } catch (error) {
        console.error("Error loading orders:", error);
        document.getElementById("orders-container").innerHTML =
            "<h3>Failed to load orders</h3>";
    }
}


async function updateCartCountFromBackend() {

    const userId = localStorage.getItem("userId");
    if (!userId) return;

    try {
        const response = await fetch(`http://localhost:8080/cart/${userId}`);
        const data = await response.json();

        const items = data.data?.items || [];

        let count = 0;

        items.forEach(item => {
            count += item.quantity;
        });

        const el = document.getElementById("cartCount");
        if (el) el.innerText = count;

        localStorage.setItem("cartCount", count);

    } catch (e) {
        console.error("Cart count error:", e);
    }
}

window.onload = function () {

    if (!checkSession()) return;

    updateCartCountFromBackend();
    loadOrders();
};