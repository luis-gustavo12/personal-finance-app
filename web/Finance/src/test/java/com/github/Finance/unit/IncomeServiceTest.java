package com.github.Finance.unit;

import com.github.Finance.models.Income;
import com.github.Finance.repositories.IncomeRepository;
import com.github.Finance.services.IncomesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class IncomeServiceTest {

    @Mock
    private IncomeRepository incomeRepository;

    @InjectMocks
    private IncomesService incomeService;

    @Test
    void testFindById() {

        // given
        Income income = new Income();
        income.setId(1L);
        income.setAmount(BigDecimal.valueOf(25.00));

        // when

        when(incomeRepository.findById(income.getId())).thenReturn(Optional.of(income));

        Income result = incomeService.findIncomeById(income.getId());

        // assert

        assertEquals(result.getId(), income.getId());
        assertEquals(result.getAmount(), income.getAmount());

    }


}
