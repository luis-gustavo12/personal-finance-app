package com.github.Finance.dtos.installments;

import com.github.Finance.models.Installment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InstallmentDTO {

    private Long id;
    private Double amount;
    private int splits;

    public InstallmentDTO(Installment installment) {
        this.id = installment.getId();
        this.amount = installment.getAmount().doubleValue();
        this.splits = installment.getSplits();
    }

}
