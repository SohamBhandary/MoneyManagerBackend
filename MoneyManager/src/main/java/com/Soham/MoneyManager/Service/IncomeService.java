package com.Soham.MoneyManager.Service;

import com.Soham.MoneyManager.DTO.IncomeDTO;
import com.Soham.MoneyManager.Entities.Category;
import com.Soham.MoneyManager.Entities.Income;
import com.Soham.MoneyManager.Entities.Profile;

import java.util.List;

public interface IncomeService {
    IncomeDTO addIncome(IncomeDTO expenseDTO);
    Income toEntity(IncomeDTO dto, Profile profile, Category category);
    IncomeDTO toDTO(Income expense);
    List<IncomeDTO> getCurrentMonthIncomeForCurrentUser();
    void deleteIncome(Long expenseId);
}
