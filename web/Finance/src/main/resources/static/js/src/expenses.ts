

const mainForm = document.getElementById('filter-incomes-form') as HTMLFormElement;
const table = document.querySelector('.entity-display-table') as HTMLTableElement;
// const editIcons = document.querySelectorAll('div.action-icons') as HTMLElement[];

if (mainForm) {

    mainForm.addEventListener('submit', (e) => {
        e.preventDefault();
        submitForm(mainForm);
    })

}


function divActionButton(id: number): string {
    return `
        <div class="action-icons">
            <a href="/incomes/edit/${id}" title="Editar">
                <i class="fas fa-pencil-alt edit-icon"></i>
            </a>
            <a href="/incomes/delete/${id}" title="Excluir"">
                <i class="fas fa-times delete-icon"></i>
            </a>
        </div>
    `;
}

function updateTable(incomes: Array<any>) {
    if (!table) return;

    let tbody = table.querySelector('tbody');

    incomes.forEach((income: any) => {
       let row = tbody?.insertRow();
       if (row) {
           row.insertCell().textContent = income.paymentMethod;
           row.insertCell().textContent = income.currency;
           row.insertCell().textContent = income.amount.toFixed(2);
           row.insertCell().textContent = income.info;
           row.insertCell().textContent = income.category;
           // row.insertCell().textContent = income.date;
           const dateObject = new Date(income.date);
           const formatter = Intl.DateTimeFormat(
               'pt-br', {
                   day: '2-digit',
                   month: '2-digit',
                   year: 'numeric'
               }
           );
           row.insertCell().textContent = formatter.format(dateObject);
           row.insertCell().innerHTML = divActionButton(income.id);
       }
    });

}

function submitForm(form: HTMLFormElement) {

    const formData = new FormData(form);
    const params = new URLSearchParams(formData as any).toString();

    fetch(`/expenses/filter?${params}`)
        .then(res => {
            if (!res.ok) {
                throw new Error("Error: " + res.statusText);
            }
            return res.json();
        })
    .then(incomes => {
        if (incomes.lenght === 0) {
            clearTable(true);
            return;
        }

        clearTable(false);
        updateTable(incomes);
    })


}

function clearTable(showMsg: boolean) {
    if (table === null) return;

    let tbody = table.querySelector('tbody');

    if (tbody === null) {
        console.log("Error!!");
        return;
    }

    tbody.innerHTML = '';

    if (showMsg) {
        showEmptyTableMessage(tbody);
    }

}

function showEmptyTableMessage(tbody: HTMLTableSectionElement) {


    const row = tbody.insertRow();
    const cell = row.insertCell();

    cell.textContent = 'Nenhum resultado encontrado!!!';
    cell.colSpan = 5;
    cell.style.textAlign = 'center';


}



