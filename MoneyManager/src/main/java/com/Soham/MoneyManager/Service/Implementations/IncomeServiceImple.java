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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class IncomeServiceImple implements IncomeService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private IncomeReposiotry incomeReposiotry;
    @Autowired
    private ProfileService profileService;

    public List<IncomeDTO> getCurrentMonthIncomeForCurrentUser(){
        log.info("Fetching current month incomes for current user");
        Profile profile=   profileService.getCurrentProfile();
        LocalDate now =LocalDate.now();
        LocalDate start=  now.withDayOfMonth(1);
        LocalDate  end=  now.withDayOfMonth(now.lengthOfMonth());
        List<Income> list= incomeReposiotry.findByProfileIdAndDateBetween(profile.getId(),start,end);
        log.info("Found {} incomes for current month", list.size());
        return list.stream().map(this::toDTO).toList();



    }
    public void deleteIncome(Long expenseId){
        log.info("Deleting income with id {}", expenseId);
        Profile profile=profileService.getCurrentProfile();
        Income  expense=     incomeReposiotry.findById(expenseId).orElseThrow(()->

                new RuntimeException("Income not found"));

        if(!expense.getProfile().getId().equals(profile.getId())){
            log.error("Unauthorized attempt to delete income with id {}", profile.getId());
            throw new RuntimeException("Unauthorized to delete this income");

        }

        incomeReposiotry.delete(expense);
        log.info("Income with id {} deleted successfully", expenseId);


    }


    public IncomeDTO addIncome(IncomeDTO expenseDTO){
        log.info("Adding new income: {}", expenseDTO);
        Profile profile=   profileService.getCurrentProfile();
        Category category= categoryRepository.findById(expenseDTO.getCategoryId()).orElseThrow(()->new RuntimeException("Category Not found"));
        Income newExpense=  toEntity(expenseDTO,profile,category);
        newExpense=incomeReposiotry.save(newExpense);
        log.info("Income added successfully with id {}", newExpense.getId());
        return  toDTO(newExpense);






    }
    public List<IncomeDTO> getLatest5expensenseForCurrentUser(){

        log.info("Fetching latest 5 incomes for current user");
        Profile profile=profileService.getCurrentProfile();
        List<Income> list=  incomeReposiotry.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }

    public BigDecimal getTotalExpenseForCurrentUser(){
        log.info("Calculating total income for current user");
        Profile profile=  profileService.getCurrentProfile();
        BigDecimal total=   incomeReposiotry.findTotalExpenseByProfileId(profile.getId());
        log.info("Total income: {}", total != null ? total : BigDecimal.ZERO);
        return  total!=null?total:BigDecimal.ZERO;

    }
    public List<IncomeDTO> filterExpenses(LocalDate start, LocalDate end, String keyword, Sort sort){
        log.info("Filtering incomes from {} to {} with keyword '{}'", start, end, keyword);

        Profile  profile= profileService.getCurrentProfile();
        List<Income> list=   incomeReposiotry.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), start, end, keyword, sort);
        log.info("Found {} filtered incomes", list.size());
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
