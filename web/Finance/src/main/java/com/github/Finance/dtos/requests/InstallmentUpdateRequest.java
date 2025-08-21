package com.github.Finance.dtos.requests;

public record InstallmentUpdateRequest(
        Double amount,
        Integer splits,
        String description,
        Long paymentMethod
)

{
}
