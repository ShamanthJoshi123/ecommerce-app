
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

async function loadCart() {

    const userId = checkLogin();
    if (!userId) return;

    const response = await fetch(`http://localhost:8080/cart/${userId}`);
    const result = await response.json();

    console.log("FULL RESPONSE:", result);

    const items = result.data?.items || [];

    let html = "";
    let itemsTotal = 0;
    let discount = 0;
    let count = 0;

    if (items.length === 0) {
        html = "<h3>Your cart is empty</h3>";
    }

    items.forEach(item => {

        const product = item.product;
        if (!product) return;

        itemsTotal += product.price * item.quantity;
        count += item.quantity;

        html += `
            <div class="cart-item">

                <img src="${product.imageUrl || 'https://via.placeholder.com/150'}" />

                <div class="cart-details">
                    <h3>${product.name}</h3>
                    <p>${rupee}${product.price}</p>

                    <div>
                        <button class="qty-btn" onclick="updateQty(${item.productId}, -1)">-</button>
                        <b>${item.quantity}</b>
                        <button class="qty-btn" onclick="updateQty(${item.productId}, 1)">+</button>
                    </div>

                    <button class="remove-btn" onclick="removeItem(${item.productId})">
                        Remove
                    </button>
                </div>

            </div>
        `;
    });


    if (itemsTotal > 50000) {
        discount = 2000;
    }

    const finalTotal = itemsTotal - discount;


    document.getElementById("cart-items").innerHTML = html;
  document.getElementById("itemsTotal").innerText = rupee + itemsTotal;
  document.getElementById("discount").innerText = rupee + discount;
  document.getElementById("total").innerText = rupee + finalTotal;

    const cartCount = document.getElementById("cartCount");
    if (cartCount) cartCount.innerText = count;
}



async function updateQty(productId, change) {

    const userId = checkLogin();
    if (!userId) return;


    const cartResponse = await fetch(`http://localhost:8080/cart/${userId}`);
    const cartData = await cartResponse.json();

    const items = cartData.data?.items || [];

    // FIND PRODUCT
    const item = items.find(i => i.productId === productId);

    if (!item) return;

    const currentQty = item.quantity;

    // AVAILABLE STOCK
    const availableQty = item.product.quantity;


    if (change > 0 && currentQty >= availableQty) {

        alert(`Only ${availableQty} items available in stock`);

        return;
    }

    // OPTIONAL: prevent below 1
    if (change < 0 && currentQty <= 1) {

        alert("Minimum quantity is 1");

        return;
    }

    // UPDATE CART
    await fetch(
        `http://localhost:8080/cart/add?userId=${userId}&productId=${productId}&quantity=${change}`,
        { method: "POST" }
    );

    loadCart();
}


async function removeItem(productId) {

    const userId = checkLogin();
    if (!userId) return;

    await fetch(
        `http://localhost:8080/cart/remove?userId=${userId}&productId=${productId}`,
        { method: "DELETE" }
    );

    loadCart();
}


async function updateCartCountFromBackend() {

    const userId = localStorage.getItem("userId");
    if (!userId) return;

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
}


function goToCheckout() {

    const userId = checkLogin();
    if (!userId) return;

    window.location.href = "checkout.html";
}

window.onload = function () {

    if (!checkSession()) return;

    updateCartCountFromBackend();
    loadCart();
};