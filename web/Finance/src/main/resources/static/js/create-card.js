
const firstSixDigitsElem = document.getElementById('firstSixDigits');
firstSixDigitsElem.addEventListener('blur', validateFirstSixDigits)


const fourLastDigitsElem = document.getElementById('lastFourDigits');
fourLastDigitsElem.addEventListener('blur', validateLastFourDigits);




function validateFirstSixDigits() {

    var value = firstSixDigitsElem.value.trim();

    if (value != '') {
        if (value.length != 6) {
            alert('Preencha o campo com 6 dígitos numéricos!!');
            firstSixDigitsElem.value = '';
            return;
        }
        if (!allValuesAreNumbers(value)) {
            alert('Caractér invalido encontrado!! Digite apenas valores numéricos');
            firstSixDigitsElem.value = '';
        }
    }




}

function validateLastFourDigits() {

    var value = fourLastDigitsElem.value.trim();

    if (value != '') {
        if (value.length != 4) {
            alert('Preencha o campo com 4 dígitos numéricos!!');
            fourLastDigitsElem.value = '';
            return;
        }
        if (!allValuesAreNumbers(value)) {
            alert('Caractér invalido encontrado!! Digite apenas valores numéricos');
            fourLastDigitsElem.value = '';
            return;
        }
    }

    


}
