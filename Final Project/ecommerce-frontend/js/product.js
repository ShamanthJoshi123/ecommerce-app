
function checkLogin() {
    const userId = localStorage.getItem("userId");

    if (!userId || userId === "undefined" || userId === "") {
        alert("Please login first");
        window.location.href = "login.html";
        return null;
    }
    return userId;
}


async function loadProducts() {

    try {
        const response = await fetch("http://localhost:8080/products");

        if (!response.ok) {
            throw new Error("Failed to load products");
        }

        const products = await response.json();

        let html = "";

        products.forEach(p => {

            const rating = (Math.random() * 2 + 3).toFixed(1);
            const discount = Math.floor(Math.random() * 30) + 10;

            html += `
                <div class="product-card" onclick="viewProduct(${p.id})">

                    <div class="badge">${discount}% OFF</div>

                    <img src="${p.imageUrl || 'https://picsum.photos/200'}" class="product-img">

                    <h3>${p.name}</h3>

                    <div class="rating">
                        ${rating}
                    </div>

                    <p class="price">
                        &#8377;${p.price}
                        <span class="old-price">${Math.floor(p.price * 1.3)}</span>
                    </p>

                    <button onclick="event.stopPropagation(); addToCart(${p.id})">
                        Add to Cart
                    </button>

                </div>
            `;
        });

        document.getElementById("product-list").innerHTML = html;

    } catch (e) {
        console.error("Product load error:", e);
        document.getElementById("product-list").innerHTML =
            "<h3>Failed to load products</h3>";
    }
}


function viewProduct(id) {
    localStorage.setItem("productId", id);
    window.location.href = "product-detail.html";
}


async function addToCart(productId) {

    const userId = localStorage.getItem("userId");
    const loginTime = localStorage.getItem("loginTime");

    // LOGIN CHECK
    if (!userId || !loginTime) {

        alert("Please login first");

        window.location.href = "login.html";

        return;
    }

    // SESSION CHECK
    const now = Date.now();
    const SESSION_LIMIT = 30 * 60 * 1000;

    if (now - loginTime > SESSION_LIMIT) {

        localStorage.clear();

        alert("Session expired. Please login again.");

        window.location.href = "login.html";

        return;
    }

    try {


        const productsResponse = await fetch(
            "http://localhost:8080/products"
        );

        const products = await productsResponse.json();

        // FIND PRODUCT
        const product = products.find(p => p.id === productId);

        if (!product) {
            alert("Product not found");
            return;
        }


        const cartResponse = await fetch(
            `http://localhost:8080/cart/${userId}`
        );

        const cartData = await cartResponse.json();

        const items = cartData.data?.items || [];

        // FIND EXISTING CART ITEM
        const existingItem = items.find(
            i => i.productId === productId
        );

        const currentQty = existingItem
            ? existingItem.quantity
            : 0;


        if (currentQty >= product.quantity) {

            alert(`Only ${product.quantity} items available in stock`);

            return;
        }


        const response = await fetch(
            `http://localhost:8080/cart/add?userId=${userId}&productId=${productId}&quantity=1`,
            {
                method: "POST"
            }
        );

        if (!response.ok) {

            alert("Failed to add to cart");

            return;
        }

        // UPDATE CART COUNT
        await updateCartCountFromBackend();

        alert("Added to cart");

    } catch (e) {

        console.error(e);

        alert("Something went wrong");
    }
}

async function updateCartCountFromBackend() {

    const userId = localStorage.getItem("userId");
    if (!userId) return;

    try {
        const response = await fetch(`http://localhost:8080/cart/${userId}`);
        const data = await response.json();

        console.log("COUNT API:", data);

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
    loadProducts();
    updateCartCountFromBackend();
};