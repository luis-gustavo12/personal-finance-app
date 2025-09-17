package com.github.Finance.dtos.views;

import com.github.Finance.models.Installment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class InstallmentView {

    private Installment installment;
    private LocalDate lastExpectedSplit;

    public InstallmentView(Installment installment) {
        this.installment = installment;
        this.lastExpectedSplit = installment.getFirstSplitDate().plusMonths((long) installment.getSplits());
    }


}
