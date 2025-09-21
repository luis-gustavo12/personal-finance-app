import {isFloat} from "./util.js";


const filterForm = document.getElementById('filter-incomes-form');
const decimals = document.querySelectorAll('.decimal-field');
let displayTable = document.querySelector('.entity-display-table');


filterForm.addEventListener('submit', function(event) {

    event.preventDefault();

    incomesFilterSearch();
});

filterForm.addEventListener('reset', function() {
    setTimeout(() => {
        incomesFilterSearch();
    }, 0);
});


decimals.forEach(field => {
    field.addEventListener('blur', function () {
        if (field.value.trim() !== '' && !isFloat(field.value)) {
            window.alert('Preencha os campos decimais corretamente!!');
            field.value = '';
        }
    });
});


function incomesFilterSearch() {

    if (filterForm == null) {
        return;
    }

    const params = new URLSearchParams(new FormData(filterForm)).toString();
    console.log('Params:', params);

    fetch(`/incomes/filter?${params}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Request error!! Status: ' + response.status);
            }
            return response.json();
        })
        .then(incomes => {

            if (incomes.length === 0) {
                showEmptyTable();
                return;
            }

            // cleaning up the table.

            if (incomes && incomes.length > 0) {
                updateTable(incomes);
            } else {
                showEmptyTable();
            }


        })
        .catch(error => {
            window.alert('An error occurred during the request.');
        });
}


function showEmptyTable() {
    const tbody = displayTable.querySelector('tbody');
    const tfoot = displayTable.querySelector('tfoot');

    tbody.innerHTML = '';
    tfoot.innerHTML = '';

    const row = tbody.insertRow();
    const cell = row.insertCell();
    cell.textContent = 'Nenhum resultado encontrado para os filtros aplicados.';
    cell.colSpan = 5;
    cell.style.textAlign = 'center';
}

function divActionButton(number) {
    return `
        <div class="action-icons">
            <a href="/incomes/edit/${number}" title="Editar">
                <i class="fas fa-pencil-alt edit-icon"></i>
            </a>
            <a href="/incomes/delete/${number}" title="Excluir"">
                <i class="fas fa-times delete-icon"></i>
            </a>
        </div>
    `;
}


function updateTable(incomes) {
    const tbody = displayTable.querySelector('tbody');
    const tfoot = displayTable.querySelector('tfoot');


    tbody.innerHTML = '';
    tfoot.innerHTML = '';

    let totalSum = 0;

    let currency = incomes[0].currency;

    incomes.forEach(income => {

        totalSum += income.amount;


        const row = tbody.insertRow();

        row.insertCell().textContent = income.currency;
        row.insertCell().textContent = income.amount.toFixed(2);
        row.insertCell().textContent = income.paymentForm;

        const date = new Date(income.date);
        row.insertCell().textContent = date.toLocaleDateString('pt-BR');
        row.insertCell().textContent = income.extraInfo;
        row.insertCell().innerHTML = divActionButton(income.id);
    });


    const footerRow = tfoot.insertRow();
    const footerCell = footerRow.insertCell();


    const formattedSum = totalSum.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

    footerCell.textContent = `Total: ${currency} ${formattedSum}`;
}


