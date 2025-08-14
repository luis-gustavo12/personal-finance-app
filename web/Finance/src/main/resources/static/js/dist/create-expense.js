"use strict";
let amountField = document.getElementById('amount');
amountField.addEventListener('blur', isFloat);
let regex = new RegExp("^\\d+[.,]\\d{1,3}$");
function isFloat() {
    if (amountField.value.trim() !== '') {
        if (!regex.test(amountField.value)) {
            window.alert('Insira valores decimais!!!');
            amountField.value = '';
        }
    }
}
//# sourceMappingURL=create-expense.js.map