import { isFloat } from "./util.js";
const incomeAmount = document.getElementById('incomeAmount');
incomeAmount.addEventListener('blur', checkDecimal);
function checkDecimal() {
    if (!isFloat(incomeAmount.value) && incomeAmount.value.trim() !== '') {
        window.alert('Insira valores decimais!!');
        incomeAmount.value = '';
    }
}
