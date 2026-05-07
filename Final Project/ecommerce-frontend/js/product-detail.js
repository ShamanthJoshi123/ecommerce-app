async function loadProduct() {
     const rupee = "\u20B9";
    const id = localStorage.getItem("productId");

    const response = await fetch(`http://localhost:8080/products/${id}`);
    const p = await response.json();

    document.getElementById("image").src = p.imageUrl;
    document.getElementById("name").innerText = p.name;
    document.getElementById("price").innerText =  rupee+p.price;
    document.getElementById("desc").innerText = p.description;
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

    if (el) {
        el.innerText = count;
    }
}
async function addToCartDetail() {

    const userId = localStorage.getItem("userId");
    const productId = localStorage.getItem("productId");

    await fetch(`http://localhost:8080/cart/add?userId=${userId}&productId=${productId}&quantity=1`, {
        method: "POST"
    });

    alert("Added to cart");
}


window.onload = function () {

    if (!checkSession()) return;

    updateCartCountFromBackend();

    loadProduct();
};
loadProduct();




