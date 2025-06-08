import {isFloat} from "./util.js";

let decimalField = document.getElementById('incomeAmount');

decimalField.addEventListener('blur', function () {

    if (decimalField.value !== '') {
        if (!isFloat(decimalField.value)) {
            window.alert('Insira valores decimais!!');
            decimalField.value = '';
        }
    }


});
