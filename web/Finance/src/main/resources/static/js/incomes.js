import {isFloat} from "./util.js";





const decimals = document.querySelectorAll('.decimal-field');
const filterSearchButton = document.getElementById('apply-filter');

filterSearchButton.addEventListener('click', incomesFilterSearch);


decimals.forEach(field => {
    field.addEventListener('blur', function () {
        if (field.value.trim() !== '' && !isFloat(field.value)) {
            window.alert('Preencha os campos decimais corretamente!!');
            field.value = '';
        }
    });
});



function incomesFilterSearch() {

    const form = document.getElementById('filter-incomes-form');

    if (form == null) {
        return null;
    }

    const params = new URLSearchParams(new FormData(form)).toString();

    fetch(`http://localhost:8080/incomes/filter?${params}`)

        .then(response => {

            if (!response.ok) {
                throw new Error('Request error!!');
            }

            return response.json();

        })

        .then(data => {

            console.log(data);

        })


        .catch( error => {



        });






}
