let paymentData = null; // store globally

async function loadAmount() {

    const userId = localStorage.getItem("userId");


    if (!userId || userId === "null" || userId === "undefined") {
        alert("Please login first");
        window.location.href = "login.html";
        return;
    }

    try {
        const res = await fetch(
            `http://localhost:8080/orders/create-payment?userId=${userId}`,
            { method: "POST" }
        );

        if (!res.ok) {
            throw new Error("Payment API failed");
        }

        const data = await res.json();

        console.log("PAYMENT DATA:", data);

        paymentData = data;

        const amount = Number(data.amount || 0);
         const rupee = "\u20B9";
        document.getElementById("amount").innerText = rupee+(amount / 100);

    } catch (e) {
        console.error("Error:", e);
    }
}


function startPayment() {

    if (!paymentData) {
        alert("Payment not initialized");
        return;
    }

    const userId = localStorage.getItem("userId");

    const options = {
        key: "rzp_test_SlEnNKNbyoYMJs",
        amount: paymentData.amount,
        currency: "INR",
        name: "ShopEasy",
        description: "Order Payment",
        order_id: paymentData.orderId,

        handler: async function () {

            const checkoutData = JSON.parse(localStorage.getItem("checkoutData"));

            const orderRes = await fetch(
                `http://localhost:8080/orders/checkout/${userId}`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(checkoutData)
                }
            );

           if (!orderRes.ok) {
               alert("Order failed");
               return;
           }


           const orderData = await orderRes.json();

           console.log("ORDER RESPONSE:", orderData);


           const orderId = orderData.data.orderId;


           const paymentRes = await fetch(
               `http://localhost:8080/payments/pay/${orderId}?method=UPI`,
               {
                   method: "POST"
               }
           );

           if (!paymentRes.ok) {
               alert("Payment record save failed");
               return;
           }

           console.log("Payment saved successfully");

           // CLEAR CART
           localStorage.setItem("cartCount", 0);

           alert("Payment successful ");

           // REDIRECT
           window.location.href = "orders.html";
        }
    };

    const rzp = new Razorpay(options);
    rzp.open();
}


window.onload = function () {

    if (!checkSession()) return;

    loadAmount();
};

