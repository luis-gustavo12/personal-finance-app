const subscriptionModalButton = document.getElementById('open-modal-button');
const subscriptionModal = document.querySelector('dialog#subscription-modal') as HTMLDialogElement;
const closeSubscriptionModalButton = document.getElementById('closeModalButton');

const installmentsModalButton = document.getElementById('installmentsModal');
const installmentsModal = document.querySelector('dialog#installments-modal') as HTMLDialogElement;
const closeInstallmentsModalButton = document.getElementById('closeInstallment');


if (subscriptionModalButton && subscriptionModal) {
    subscriptionModalButton.addEventListener('click', () => {
        subscriptionModal.showModal();
    });
}

if (closeSubscriptionModalButton && subscriptionModal) {
    closeSubscriptionModalButton.addEventListener('click', () => {

        subscriptionModal.classList.add('closing');

        subscriptionModal.addEventListener('animationend', () => {

            subscriptionModal.classList.remove('closing');
            subscriptionModal.close();
        }, { once: true });
    });
}


if (installmentsModalButton && installmentsModal) {
    installmentsModalButton.addEventListener('click', () => {
        installmentsModal.showModal();
    });
}

if (closeInstallmentsModalButton && installmentsModal) {
    closeInstallmentsModalButton.addEventListener('click', () => {

        installmentsModal.classList.add('closing');

        installmentsModal.addEventListener('animationend', () => {
            // Then, close the dialog and remove the class
            installmentsModal.classList.remove('closing');
            installmentsModal.close();
        }, { once: true });
    });
}