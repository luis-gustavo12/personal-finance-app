import {DecimalDisplayObject} from "./numeric.js";

let amountField = document.getElementById('amount') as HTMLInputElement | null ;
let decimalObject = new DecimalDisplayObject();
console.log("Hello World");

if (amountField) {
    amountField.value = decimalObject.show();
    amountField.addEventListener('beforeinput', (event: InputEvent) => {
        event.preventDefault();
        if (event.inputType == "deleteContentBackward") {
            decimalObject.remove();
            amountField.value = decimalObject.show();
            return;
        }
        const typedValue: string | null = event.data;
        if (!typedValue) return;
        console.log("Current value:", typedValue);

        let rsl = decimalObject.update(typedValue);

        if (rsl.success) { // Changed from (rsl == {success: true})
            amountField!.value = decimalObject.show();
        }



    });
}




