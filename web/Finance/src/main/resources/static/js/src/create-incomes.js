import {isFloat} from "./util.js";



document.addEventListener('DOMContentLoaded', function() {
    flatpickr("#incomeDate", {
        dateFormat: "Y-m-d",
        altInput: true,
        altFormat: "d/m/Y",
        locale: flatpickr.l10ns.pt,
        allowInput: true
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
