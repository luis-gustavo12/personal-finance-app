
package com.github.Finance.dtos.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddCardForm(
    @NotBlank @Size(min = 1, max = 255) String stripeToken,
    @Size(min = 1, max = 40) String cardDescription,
    @NotBlank @Size(min = 1, max = 255) String cardType,
    @NotBlank @Size(min = 1, max = 255) String cardholderName

) {
}