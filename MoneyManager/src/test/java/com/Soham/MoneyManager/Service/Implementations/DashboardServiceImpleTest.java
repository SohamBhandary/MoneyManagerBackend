package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.DTO.ExpenseDTO;
import com.Soham.MoneyManager.DTO.IncomeDTO;
import com.Soham.MoneyManager.DTO.RecentTransacationsDTO;
import com.Soham.MoneyManager.Entities.Profile;
import com.Soham.MoneyManager.Service.ExpenseService;
import com.Soham.MoneyManager.Service.IncomeService;
import com.Soham.MoneyManager.Service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImpleTest {

    @Mock
    private IncomeService incomeService;
    @Mock
    private ExpenseService expenseService;
    @Mock
    private ProfileService profileService;
    @InjectMocks
    private DashboardServiceImple dashboardService;

    private Profile profile;
    private IncomeDTO income;
    private ExpenseDTO expense;

    @BeforeEach
    void setUp() {
        profile = Profile.builder()
                .id(1L)
                .build();

        income = IncomeDTO.builder()
                .id(10L)
                .name("Salary")
                .icon("💰")
                .amount(new BigDecimal("50000"))
                .date(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .build();

        expense = ExpenseDTO.builder()
                .id(20L)
                .name("Food")
                .icon("🍔")
                .amount(new BigDecimal("10000"))
                .date(LocalDate.now().minusDays(1))
                .createdAt(LocalDateTime.now().minusHours(1))
                .build();
    }

    @Test
    void getDashboardData_success() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(incomeService.getLatest5expensenseForCurrentUser())
                .thenReturn(List.of(income));
        when(expenseService.getCurrentMonthExpenseForCurrentUser())
                .thenReturn(List.of(expense));
        when(incomeService.getTotalExpenseForCurrentUser())
                .thenReturn(new BigDecimal("50000"));

        when(expenseService.getTotalExpenseForCurrentUser())
                .thenReturn(new BigDecimal("10000"));

        Map<String, Object> result = dashboardService.getDashboardData();


        assertNotNull(result);

        assertEquals(
                new BigDecimal("40000"),
                result.get("totalBalance")
        );

        assertEquals(
                new BigDecimal("50000"),
                result.get("totalIncome")
        );

        assertEquals(
                new BigDecimal("10000"),
                result.get("totalExpense")
        );

        List<IncomeDTO> recentIncomes =
                (List<IncomeDTO>) result.get("recent5Incomes");

        List<ExpenseDTO> recentExpenses =
                (List<ExpenseDTO>) result.get("recent5Expenses");

        assertEquals(1, recentIncomes.size());
        assertEquals(1, recentExpenses.size());

        List<RecentTransacationsDTO> transactions =
                (List<RecentTransacationsDTO>) result.get("recenTransactions");

        assertEquals(2, transactions.size());
        assertEquals("income", transactions.get(0).getType());
        assertEquals("expense", transactions.get(1).getType());


        verify(profileService).getCurrentProfile();
        verify(incomeService).getLatest5expensenseForCurrentUser();
        verify(expenseService).getCurrentMonthExpenseForCurrentUser();
        verify(incomeService, times(2)).getTotalExpenseForCurrentUser();
        verify(expenseService, times(2)).getTotalExpenseForCurrentUser();
    }
}
