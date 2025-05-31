
package com.github.Finance.dtos.forms;

public record AddCardForm(
    String lastFourDigits,
    String firstSixDigits,
    String cardholderName,
    int expirationMonth,
    int expirationYear,
    String cardType,
    String brandName

) {
}