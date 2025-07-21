export function isDecimal(inputString) {
    const decimalRegex = /^[+-]?(\d+\.?\d*|\.\d+|\d+)$/;
    return decimalRegex.test(inputString);
}
