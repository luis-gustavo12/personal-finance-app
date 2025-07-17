"use strict";
// The main installment checker
var installmentButton = document.getElementById('installmentCheck');
// This var contains all the inputs that must be shown, once the 
let installmentShownInputs = [
    document.getElementById('splits'),
    document.getElementById('interestRate'),
    document.getElementById('rawAmount')
];
installmentButton.addEventListener('click', installmentClick);
function installmentClick() {
    if (installmentButton.checked) {
        for (let elem of installmentShownInputs) {
            elem.style.display = 'block';
            elem.labels[0].style.display = 'block';
            elem.required = true;
        }
    }
    else {
        for (let elem of installmentShownInputs) {
            elem.style.display = 'none';
            elem.labels[0].style.display = 'none';
            elem.required = false;
        }
    }
}
document.addEventListener('DOMContentLoaded', function () {
    for (let elem of installmentShownInputs) {
        elem.style.display = 'none';
        elem.labels[0].style.display = 'none';
        elem.required = false;
    }
});
