package com.github.Finance.dtos.response;

import java.time.LocalDate;

public record IncomesDetailResponse (

    String currency,
    Double amount,
    String paymentForm,
    LocalDate date,
    String extraInfo,
    Long id

) {
}
