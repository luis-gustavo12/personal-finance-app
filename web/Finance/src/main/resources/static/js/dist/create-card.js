"use strict";
const stripePublishableKey = window.stripePublishableKey;
// 1. Initialize Stripe.js with your publishable key
const stripe = Stripe(stripePublishableKey);
const elements = stripe.elements();
// 2. Optional: Add styling to the Stripe Element for a better look and feel.
const style = {
    base: {
        color: '#32325d',
        fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
        fontSmoothing: 'antialiased',
        fontSize: '16px',
        '::placeholder': {
            color: '#aab7c4'
        }
    },
    invalid: {
        color: '#fa755a',
        iconColor: '#fa755a'
    }
};
// 3. Create and mount the Card Element to your div
const cardElement = elements.create('card', { style: style });
cardElement.mount('#card-element');
// 4. Handle real-time validation errors from the card element
cardElement.addEventListener('change', function (event) {
    const displayError = document.getElementById('card-errors');
    if (event.error) {
        displayError.textContent = event.error.message;
    }
    else {
        displayError.textContent = '';
    }
});
// 5. Handle the form submission
const form = document.getElementById('cardForm');
const cardholderNameInput = document.getElementById('cardholderName');
form.addEventListener('submit', async (event) => {
    // We stop the form from submitting to our server immediately
    event.preventDefault();
    // Disable the button to prevent multiple submissions
    document.querySelector('button').disabled = true;
    // Use Stripe.js to create a payment token from the card element
    const { token, error } = await stripe.createToken(cardElement, {
        name: cardholderNameInput.value,
    });
    if (error) {
        // If Stripe returns an error, display it to the user
        const errorElement = document.getElementById('card-errors');
        errorElement.textContent = error.message;
        // Re-enable the button so the user can try again
        document.querySelector('button').disabled = false;
    }
    else {
        // If successful, we have a token!
        // Set the token ID in our hidden form input
        const hiddenInput = document.getElementById('stripeToken');
        hiddenInput.value = token.id;
        // Now, submit the form to your Spring Boot server
        form.submit();
    }
});
//# sourceMappingURL=create-card.js.map