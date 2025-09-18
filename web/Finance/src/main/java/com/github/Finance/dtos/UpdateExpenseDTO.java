package com.github.Finance.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateExpenseDTO(
        Long currency,
        Long paymentMethod,
        BigDecimal amount,
        String extraInfo,
        LocalDate date,
        Long categoryId
) {}

