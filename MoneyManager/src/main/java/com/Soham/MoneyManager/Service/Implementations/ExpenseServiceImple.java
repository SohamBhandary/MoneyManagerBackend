package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.DTO.ExpenseDTO;
import com.Soham.MoneyManager.Entities.Category;
import com.Soham.MoneyManager.Entities.Expense;
import com.Soham.MoneyManager.Entities.Profile;
import com.Soham.MoneyManager.Repositories.CategoryRepository;
import com.Soham.MoneyManager.Repositories.ExpenseRepository;
import com.Soham.MoneyManager.Repositories.IncomeReposiotry;
import com.Soham.MoneyManager.Service.CategoryService;
import com.Soham.MoneyManager.Service.ExpenseService;
import com.Soham.MoneyManager.Service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j


public class ExpenseServiceImple  implements ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private IncomeReposiotry incomeReposiotry;
    @Autowired
    private ProfileService profileService;

    public ExpenseDTO addExpense(ExpenseDTO expenseDTO){
        log.info("Adding new expense: {}", expenseDTO);
        Profile profile=   profileService.getCurrentProfile();
        Category category= categoryRepository.findById(expenseDTO.getCategoryId()).orElseThrow(()->new RuntimeException("Category Not found"));
        log.error("Category not found with id {}", expenseDTO.getCategoryId());
      Expense newExpense=  toEntity(expenseDTO,profile,category);
      newExpense=expenseRepository.save(newExpense);
        log.info("Expense added successfully with id {}", newExpense.getId());
      return  toDTO(newExpense);
    }

    public List<ExpenseDTO> getCurrentMonthExpenseForCurrentUser(){
        log.info("Fetching current month expenses for current user");
     Profile profile=   profileService.getCurrentProfile();
        LocalDate now =LocalDate.now();
      LocalDate start=  now.withDayOfMonth(1);
        LocalDate end=  now.withDayOfMonth(now.lengthOfMonth());
       List<Expense> list= expenseRepository.findByProfileIdAndDateBetween(profile.getId(),start,end);
        log.info("Found {} expenses for current month", list.size());
       return list.stream().map(this::toDTO).toList();



    }
    public void deleteExpense(Long expenseId){
        log.info("Deleting expense with id {}", expenseId);
        Profile profile=profileService.getCurrentProfile();
   Expense  expense=     expenseRepository.findById(expenseId).orElseThrow(()->new RuntimeException("Expense not found"));


   if(!expense.getProfile().getId().equals(profile.getId())){
       log.error("Unauthorized attempt to delete expense with id {}", expenseId);
       throw new RuntimeException("Unauthorized to delete this expense");


   }

   expenseRepository.delete(expense);
        log.info("Expense with id {} deleted successfully", expenseId);


    }

    public List<ExpenseDTO> getLatest5expensenseForCurrentUser(){
        log.info("Fetching latest 5 expenses for current user");
        Profile profile=profileService.getCurrentProfile();
      List<Expense> list=  expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
      return list.stream().map(this::toDTO).toList();
    }

    public BigDecimal getTotalExpenseForCurrentUser(){
        log.info("Calculating total expenses for current user");

        Profile profile=  profileService.getCurrentProfile();
   BigDecimal total=   expenseRepository.findTotalExpenseByProfileId(profile.getId());
   log.info("Total expense: {}", total != null ? total : BigDecimal.ZERO);
   return  total!=null?total:BigDecimal.ZERO;

    }

    public List<ExpenseDTO> filterExpenses(LocalDate start, LocalDate end, String keyword, Sort sort){
        log.info("Filtering expenses from {} to {} with keyword '{}'", start, end, keyword);

      Profile  profile= profileService.getCurrentProfile();
   List<Expense> list=   expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),start,end,keyword,sort);
        log.info("Found {} filtered expenses", list.size());
  return list.stream().map(this::toDTO).toList();
    }

    public List<ExpenseDTO> getExpensesForUserOnDate(Long profileId,LocalDate date){
        log.info("Fetching expenses for user {} on date {}", profileId, date);
      List<Expense> list=  expenseRepository.findByProfileIdAndDate(profileId,date);
        log.info("Found {} expenses for user {} on date {}", list.size(), profileId, date);
     return list.stream().map(this::toDTO).toList();

    }




    public Expense toEntity(ExpenseDTO expenseDTO, Profile profile, Category category){
     return    Expense.builder()
             .name(expenseDTO.getName())
             .icon(expenseDTO.getIcon())
             .amount(expenseDTO.getAmount())
             .date(expenseDTO.getDate())
             .profile(profile)
             .category(category)
             .build();

    }
    public ExpenseDTO toDTO(Expense expense){
      return   ExpenseDTO.builder()
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
