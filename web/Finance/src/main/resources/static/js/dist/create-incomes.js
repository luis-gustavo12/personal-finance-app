import { isFloat } from "./util.js";
import flatpickr from 'flatpickr';
import { Portuguese } from 'flatpickr/dist/l10n/pt';
document.addEventListener('DOMContentLoaded', function () {
    // Initialize Flatpickr on the incomeDate input
    flatpickr("#incomeDate", {
        // Basic configuration for a single date picker
        dateFormat: "Y-m-d", // Matches the default HTML date input format
        altInput: true, // Display a human-friendly date in the input field
        altFormat: "d/m/Y", // Format for the human-friendly date
        locale: Portuguese, // Set the locale to Portuguese
        // You can add more options here:
        // minDate: "today", // Disable past dates
        // maxDate: new Date().fp_incr(14), // Disable dates more than 14 days in the future
        // enableTime: true, // Include time picker
        // noCalendar: true, // Only show time picker
        // enable: [
        //     { from: "2025-01-01", to: "2025-03-01" },
        //     { from: "2025-05-01", to: "2025-07-01" }
        // ], // Enable specific date ranges
    });
});
const incomeAmount = document.getElementById('incomeAmount');
incomeAmount.addEventListener('blur', checkDecimal);
function checkDecimal() {
    if (!isFloat(incomeAmount.value) && incomeAmount.value.trim() !== '') {
        window.alert('Insira valores decimais!!');
        incomeAmount.value = '';
    }
}
