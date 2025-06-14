


export function allValuesAreNumbers(str) {


    for (let i = 0; i < str.length; i++) {
        if (isNaN(str[i]) && str[i].trim() != '') {
            return false;
        }

        return true;
    }

}


export function isFloat(value) {

    let decimalRegex = new RegExp("^\\d+([.,]\\d{1,3})?$");

    if (value.trim() !== '') {
        return decimalRegex.test(value);

    }

    return false;
}