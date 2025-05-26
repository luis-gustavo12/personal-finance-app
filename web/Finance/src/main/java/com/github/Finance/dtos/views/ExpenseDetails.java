package com.github.Finance.dtos.views;

import com.github.Finance.models.Expense;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * This class was created because of the necessity of a dynamic link
 * for each expense on details. So, instead of adding one more attribute
 * for each expense details, why not generating them dynamically?
 * 
 */

@Getter
@Setter
@NoArgsConstructor
public class ExpenseDetails extends Expense {

    String detailedLink;

    
}
