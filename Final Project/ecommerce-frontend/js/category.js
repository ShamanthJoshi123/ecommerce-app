
function getCategoryFromURL() {

    const params = new URLSearchParams(window.location.search);

    return decodeURIComponent(params.get("type"));
}

const rupee = "\u20B9";

async function loadCategoryProducts() {

    const category = getCategoryFromURL();

    if (!category) return;

    document.getElementById("categoryTitle").innerText = category;

    try {

        const response = await fetch("http://localhost:8080/products");

        const products = await response.json();

        // FILTER PRODUCTS
        const filtered = products.filter(p =>
            p.category?.trim().toLowerCase() === category?.trim().toLowerCase()
        );

        let html = "";

        if (filtered.length === 0) {
            html = "<h3>No products found</h3>";
        }

        filtered.forEach(p => {

            html += `
                <div class="product-card">

                    <img src="${p.imageUrl}" class="product-img">

                    <h3>${p.name}</h3>

                    <p>${rupee}${p.price}</p>

                    <button onclick="addToCart(${p.id})">
                        Add to Cart
                    </button>

                </div>
            `;
        });

        document.getElementById("product-list").innerHTML = html;

    } catch (e) {
        console.error(e);
    }
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
}

async function addToCart(productId) {

    const userId = localStorage.getItem("userId");

    if (!userId) {
        alert("Please login first");
        window.location.href = "login.html";
        return;
    }

    await fetch(
        `http://localhost:8080/cart/add?userId=${userId}&productId=${productId}&quantity=1`,
        { method: "POST" }
    );

    updateCartCountFromBackend();

    alert("Added to cart");
}

window.onload = function () {

    if (!checkSession()) return;

    updateCartCountFromBackend();

    loadCategoryProducts();
};