const stripe = Stripe("pk_test_51QEmdjIPcG2uw7V1SYdl7hCjpYV19b4vyWelZf4AQEKQGW87QLDnlZhTIfR40cfWxoMEwWY1vq1i7gikIvRUOXY200zMUBKuYH")
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
	stripe.redirectToCheckout({
		sessionId: sessionId		
	})
});