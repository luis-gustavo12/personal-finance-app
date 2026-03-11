

interface BalanceReportResponse {
    incomes: IncomesDetailResponse[];
    expenses: ExpenseResponse[];
}

interface IncomesDetailResponse {
    currency: string;
    amount: number;
    paymentForm: string;
    date: string;
    extraInfo: string;
    id: number;
}

interface ExpenseResponse {
    id: number;
    currency: string;
    paymentMethod: string;
    amount: number;
    info: string;
    date: string;
    category: string;
    installment: object | null;
    cardData: object | null;
    isSubscription: boolean;
}

const form = document.getElementById("balance-form")! as HTMLFormElement;

console.log("Hello World");

form.addEventListener("submit", (event: SubmitEvent) => {
    event.preventDefault();
    sendData(form).then(data => generateTable(data)).catch(err => console.error(err));
});

document.getElementById("tab-incomes")!.addEventListener("click", () => switchTab("incomes"));
document.getElementById("tab-expenses")!.addEventListener("click", () => switchTab("expenses"));

async function sendData(form: HTMLFormElement): Promise<BalanceReportResponse> {
    const formData = new FormData(form);

    const csrfToken = document.querySelector<HTMLMetaElement>("meta[name='_csrf']")!.content;
    const csrfHeader = document.querySelector<HTMLMetaElement>("meta[name='_csrf_header']")!.content;

    const body = {
        minimumDate: formData.get("minimumDate") as string,
        maximumDate: formData.get("maximumDate") as string,
    };

    const response = await fetch("/balance/filter", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify(body),
    });

    if (!response.ok) {
        throw new Error("Request failed: " + response.statusText);
    }

    return await response.json() as BalanceReportResponse;
}

function generateTable(data: BalanceReportResponse): void {
    renderIncomes(data.incomes);
    renderExpenses(data.expenses);

    document.getElementById("balance-tabs")!.style.display = "flex";
    switchTab("incomes");
}

function switchTab(tab: "incomes" | "expenses"): void {
    const incomesSection = document.getElementById("incomes-section")!;
    const expensesSection = document.getElementById("expenses-section")!;
    const tabIncomes = document.getElementById("tab-incomes")!;
    const tabExpenses = document.getElementById("tab-expenses")!;

    if (tab === "incomes") {
        incomesSection.style.display = "block";
        expensesSection.style.display = "none";
        tabIncomes.className = "btn btn-primary";
        tabExpenses.className = "btn btn-secondary";
    } else {
        incomesSection.style.display = "none";
        expensesSection.style.display = "block";
        tabIncomes.className = "btn btn-secondary";
        tabExpenses.className = "btn btn-primary";
    }
}

function renderIncomes(incomes: IncomesDetailResponse[]): void {
    const tbody = document.getElementById("incomes-tbody")!;
    const totalCell = document.getElementById("incomes-total")!;

    tbody.innerHTML = "";

    incomes.forEach(income => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${income.currency}</td>
            <td>${income.amount.toFixed(2)}</td>
            <td>${income.paymentForm}</td>
            <td>${income.date}</td>
            <td>${income.extraInfo ?? ""}</td>
        `;
        tbody.appendChild(tr);
    });

    const total = incomes.reduce((sum, i) => sum + i.amount, 0);
    totalCell.textContent = `Total: ${total.toFixed(2)}`;
}

function renderExpenses(expenses: ExpenseResponse[]): void {
    const tbody = document.getElementById("expenses-tbody")!;
    const totalCell = document.getElementById("expenses-total")!;

    tbody.innerHTML = "";

    expenses.forEach(expense => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${expense.currency}</td>
            <td>${expense.paymentMethod}</td>
            <td>${expense.amount.toFixed(2)}</td>
            <td>${expense.category}</td>
            <td>${expense.date}</td>
            <td>${expense.info ?? ""}</td>
        `;
        tbody.appendChild(tr);
    });

    const total = expenses.reduce((sum, e) => sum + e.amount, 0);
    totalCell.textContent = `Total: ${total.toFixed(2)}`;
}