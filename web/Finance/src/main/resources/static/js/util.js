

function allValuesAreNumbers(str) {


    for (let i = 0; i < str.length; i++) {
        if (isNaN(str[i]) && str[i].trim() != '') {
            return false;
        }

        return true;
    }

}