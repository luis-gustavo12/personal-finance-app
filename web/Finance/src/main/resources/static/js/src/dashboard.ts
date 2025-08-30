

const modalButton = document.getElementById('modalButton');
const modal = document.querySelector('dialog');
const closeModalButton = document.getElementById('closeModal');

if (modalButton) {
    modalButton.addEventListener('click', () => {
        modal?.showModal();
    })
}


closeModalButton?.addEventListener('click', () => {
   modal?.close();
});