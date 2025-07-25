package com.github.Finance.services;

import com.github.Finance.models.*;
import org.springframework.stereotype.Service;

import com.github.Finance.dtos.forms.AddExpenseDetailsForm;
import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.repositories.CardExpenseRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Service
@Slf4j
public class CardExpenseService {

    private final CardExpenseRepository repository;
    private final CardService cardService;
    private final ExpenseService expenseService;
    private final AuthenticationService authenticationService;

    public CardExpenseService (CardExpenseRepository cardExpenseRepository, CardService cardService, ExpenseService expenseService, AuthenticationService authenticationService) {
        this.repository = cardExpenseRepository;
        this.cardService = cardService;
        this.expenseService = expenseService;
        this.authenticationService = authenticationService;
    }


    /**
     * 
     * Gets the expense on CardExpense table. Note that the function
     * returns null instead of an exception. I think it's better,
     * because there are scenarios where null can be a possibility,
     * and forcing above classes that calls this functions to use
     * try catch, may get dirtier
     * 
     * @param id
     * @return
     */
    public CardExpense findCardExpense(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void processExpenseDetails(AddExpenseDetailsForm form, Long expenseId) {


        User user = authenticationService.getCurrentAuthenticatedUser();

        Card card = cardService.findCardById(form.cardsSelect());

        if (!card.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Not allowed!!");
        }

        Expense expense = expenseService.findExpenseById(expenseId);

        if (expense == null) {
            throw new ResourceNotFoundException("Expense with ID " + expenseId + " not found");
        }

        CardExpense cardExpense = new CardExpense();
        cardExpense.setCard(card);
        cardExpense.setExpense(expense);
        if (form.installmentCheck() != null) {
            cardExpense.setInstallment(true);
            cardExpense.setSplits(form.splits());
            if (form.interestRate() != null) {
                cardExpense.setInterestRate(form.interestRate());
            }
        }

        cardExpense.setStatus(ExpenseCardEnum.valueOf(form.status()));
        cardExpense.setTransactionDate(form.transactionDate());

        cardExpense = repository.save(cardExpense);

        log.info("New card expense created!!");



    }



    
}
