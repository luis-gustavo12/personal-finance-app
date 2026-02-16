

export function isDecimal(inputString: string): boolean {

    const decimalRegex = /^[+-]?(\d+\.?\d*|\.\d+|\d+)$/;

    return decimalRegex.test(inputString);
}


export class DecimalDisplayObject {

    private numbers: Array<number>;
    private display: string;
    private count: number;

    constructor() {
        this.numbers = new Array<number>(0, 0, 0, 0);
        this.display = "0.00";
        this.count = 0;
    }

    update (newNumber: string) : {success: true} | {success: false, reason: string} {

        if (newNumber.length != 1) {
            return { success: false, reason: "Input must be a single character!!"};
        }

        if (newNumber < "0" || newNumber > "9") {
            return { success: false, reason: "Number must be a digit"};
        }

        const digit = parseInt(newNumber, 10);

        if (this.count == 0) {
            if (digit == 0) {
                return {success: false, reason: "First digit cannot be zero!"};
            }
            this.numbers[this.numbers.length - this.count - 1] = digit;
            this.count += 1;
            return {success: true};
        }

        if (this.count < 4) {
            let length = this.numbers.length;
            let count = this.count;
            for (let i = length - count; i < length; i++ ) {
                this.numbers[i - 1] = this.numbers[i];
            }
            this.numbers[length - 1] = digit;
            this.count++;
        } else {
            this.numbers.push(digit);
            this.count++;
        }

        return { success: true };

    }

    show() : string {

        let tempArr = this.numbers.map(num => num.toString());
        tempArr.splice(tempArr.length - 2, 0, ".");

        return tempArr.join("");

    }

    remove() {

        if (this.count == 0)
            return;

        if (this.numbers.length > 4) {
            this.numbers.pop();
            this.count--;
            return;
        }
        // 1 2 5 9
        let newArr = new Array<number>(0, 0, 0, 0);
        for (let i = 0; i < this.count - 1; i++) {
            newArr[i + 1] = this.numbers[i];
        }
        this.numbers = newArr;
        this.count--;
    }

}

