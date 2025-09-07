package com.Soham.MoneyManager.Service;

import com.Soham.MoneyManager.DTO.ExpenseDTO;
import com.Soham.MoneyManager.Entities.Category;
import com.Soham.MoneyManager.Entities.Expense;
import com.Soham.MoneyManager.Entities.Profile;

import java.util.List;

public interface ExpenseService {
    ExpenseDTO addExpense(ExpenseDTO expenseDTO);
    Expense toEntity(ExpenseDTO expenseDTO, Profile profile, Category category);
    ExpenseDTO toDTO(Expense expense);
    List<ExpenseDTO> getCurrentMonthExpenseForCurrentUser();
    void deleteExpense(Long expenseId);
}
