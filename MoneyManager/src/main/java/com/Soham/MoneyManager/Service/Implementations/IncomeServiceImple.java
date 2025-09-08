package com.Soham.MoneyManager.Service.Implementations;


import com.Soham.MoneyManager.DTO.ExpenseDTO;
import com.Soham.MoneyManager.DTO.IncomeDTO;
import com.Soham.MoneyManager.Entities.Category;


import com.Soham.MoneyManager.Entities.Expense;
import com.Soham.MoneyManager.Entities.Income;
import com.Soham.MoneyManager.Entities.Profile;
import com.Soham.MoneyManager.Repositories.CategoryRepository;
import com.Soham.MoneyManager.Repositories.IncomeReposiotry;
import com.Soham.MoneyManager.Service.CategoryService;
import com.Soham.MoneyManager.Service.IncomeService;
import com.Soham.MoneyManager.Service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncomeServiceImple implements IncomeService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private IncomeReposiotry incomeReposiotry;
    @Autowired
    private ProfileService profileService;

    public List<IncomeDTO> getCurrentMonthIncomeForCurrentUser(){
        Profile profile=   profileService.getCurrentProfile();
        LocalDate now =LocalDate.now();
        LocalDate start=  now.withDayOfMonth(1);
        LocalDate  end=  now.withDayOfMonth(now.lengthOfMonth());
        List<Income> list= incomeReposiotry.findByProfileIdAndDateBetween(profile.getId(),start,end);
        return list.stream().map(this::toDTO).toList();



    }
    public void deleteIncome(Long expenseId){
        Profile profile=profileService.getCurrentProfile();
        Income  expense=     incomeReposiotry.findById(expenseId).orElseThrow(()->new RuntimeException("Income not found"));

        if(!expense.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("Unauthorized to delete this income");

        }

        incomeReposiotry.delete(expense);


    }


    public IncomeDTO addIncome(IncomeDTO expenseDTO){
        Profile profile=   profileService.getCurrentProfile();
        Category category= categoryRepository.findById(expenseDTO.getCategoryId()).orElseThrow(()->new RuntimeException("Category Not found"));
        Income newExpense=  toEntity(expenseDTO,profile,category);
        newExpense=incomeReposiotry.save(newExpense);
        return  toDTO(newExpense);






    }
    public List<IncomeDTO> getLatest5expensenseForCurrentUser(){
        Profile profile=profileService.getCurrentProfile();
        List<Income> list=  incomeReposiotry.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }

    public BigDecimal getTotalExpenseForCurrentUser(){
        Profile profile=  profileService.getCurrentProfile();
        BigDecimal total=   incomeReposiotry.findTotalExpenseByProfileId(profile.getId());
        return  total!=null?total:BigDecimal.ZERO;

    }
    public List<IncomeDTO> filterExpenses(LocalDate start, LocalDate end, String keyword, Sort sort){

        Profile  profile= profileService.getCurrentProfile();
        List<Income> list=   incomeReposiotry.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), start, end, keyword, sort);
        return list.stream().map(this::toDTO).toList();




    }


    public Income toEntity(IncomeDTO dto, Profile profile, Category category){
        return    Income.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profile)
                .category(category)
                .build();

    }

    public IncomeDTO toDTO(Income expense){
        return   IncomeDTO.builder()
                .id(expense.getId())
                .name(expense.getName())
                .icon(expense.getIcon())
                .categoryId(expense.getCategory()!=null?expense.getCategory().getId():null)
                .categoryName(expense.getCategory()!=null?expense.getCategory().getName():"N/A")
                .amount(expense.getAmount())
                .date(expense.getDate())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .build();



    }

}
