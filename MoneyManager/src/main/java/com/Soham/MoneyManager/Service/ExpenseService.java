package com.Soham.MoneyManager.Service;

import com.Soham.MoneyManager.DTO.ExpenseDTO;
import com.Soham.MoneyManager.Entities.Category;
import com.Soham.MoneyManager.Entities.Expense;
import com.Soham.MoneyManager.Entities.Profile;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    ExpenseDTO addExpense(ExpenseDTO expenseDTO);
    Expense toEntity(ExpenseDTO expenseDTO, Profile profile, Category category);
    ExpenseDTO toDTO(Expense expense);
    List<ExpenseDTO> getCurrentMonthExpenseForCurrentUser();
    void deleteExpense(Long expenseId);
    public List<ExpenseDTO> getLatest5expensenseForCurrentUser();
    public BigDecimal getTotalExpenseForCurrentUser();
    public List<ExpenseDTO> filterExpenses(LocalDate start, LocalDate end, String keyword, Sort sort);
    public List<ExpenseDTO> getExpensesForUserOnDate(Long profileId,LocalDate date);
}
