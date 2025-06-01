
package com.github.Finance.dtos.forms;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddCardForm(
    @NotBlank @Size(max = 4, min = 4) String lastFourDigits,
    @Size(max = 6, min = 6) String firstSixDigits,
    @NotBlank String cardholderName,
    @NotNull @Max(12) @Min(1) Integer expirationMonth,
    @NotNull @Min(2002) Integer expirationYear,
    @NotBlank String cardType,
    @NotBlank String brandName

) {
}