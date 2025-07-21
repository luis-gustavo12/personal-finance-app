

export function isDecimal(inputString: string): boolean {

    const decimalRegex = /^[+-]?(\d+\.?\d*|\.\d+|\d+)$/;

    return decimalRegex.test(inputString);
}