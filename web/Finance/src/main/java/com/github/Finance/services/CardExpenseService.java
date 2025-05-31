package com.github.Finance.services;

import org.springframework.stereotype.Service;

import com.github.Finance.dtos.forms.AddExpenseDetailsForm;
import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.models.Card;
import com.github.Finance.models.CardExpense;
import com.github.Finance.models.Expense;
import com.github.Finance.models.ExpenseCardEnum;
import com.github.Finance.repositories.CardExpenseRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CardExpenseService {

    private final CardExpenseRepository repository;
    private final CardService cardService;
    private final ExpenseService expenseService;

    public CardExpenseService (CardExpenseRepository cardExpenseRepository, CardService cardService, ExpenseService expenseService) {
        this.repository = cardExpenseRepository;
        this.cardService = cardService;
        this.expenseService = expenseService;
    }


    /**
     * 
     * Gets the expense on CardExpense table. Note that the function
     * returns null instead of an execpetion. I think it's better,
     * because there are scenarios where null can be a possibility,
     * and forcing above classes that calls this functions to use
     * try catch, may gets dirtier
     * 
     * @param id
     * @return
     */
    public CardExpense findCardExpense(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void processExpenseDetails(AddExpenseDetailsForm form, Long expenseId) {

        Card card;
        Expense expense;
        CardExpense cardExpense = new CardExpense();
        String firstDigits = form.cardsSelect().split("#")[0];
        String lastDigits = form.cardsSelect().split("#")[1];
        
        try {
            card = cardService.getCardByFirstAndLastDigits(firstDigits, lastDigits);
            expense = expenseService.findExpenseById(expenseId);
        } catch (ResourceNotFoundException ex) {
            log.error("Resource not found: {}", ex.getMessage());
            return;
        }

        cardExpense.setCard(card);
        cardExpense.setExpense(expense);

        if (form.installmentCheck() != null) {
            cardExpense.setSplits(form.splits());
            if (form.interestRate() != null) {
                cardExpense.setHasInterestRate(true);
                cardExpense.setInterestRate(form.interestRate());
            }
        }

        cardExpense.setStatus(ExpenseCardEnum.valueOf(form.status()) );
        cardExpense.setTransationcDate(form.transactionDate());
        
        cardExpense = repository.save(cardExpense);
        log.debug("Card expense with ID {} created", cardExpense);

    }

    
}
