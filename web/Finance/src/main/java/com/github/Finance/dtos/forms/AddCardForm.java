
package com.github.Finance.dtos.forms;

public record AddCardForm(
    String cardNumber,
    String cardholderName,
    int expirationMonth,
    int expirationYear,
    String cardType,
    String brandName

) {
}