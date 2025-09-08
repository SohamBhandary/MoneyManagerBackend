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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
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
        Profile profile=   profileService.getCurrentProfile();
        Category category= categoryRepository.findById(expenseDTO.getCategoryId()).orElseThrow(()->new RuntimeException("Category Not found"));
      Expense newExpense=  toEntity(expenseDTO,profile,category);
      newExpense=expenseRepository.save(newExpense);
      return  toDTO(newExpense);
    }

    public List<ExpenseDTO> getCurrentMonthExpenseForCurrentUser(){
     Profile profile=   profileService.getCurrentProfile();
        LocalDate now =LocalDate.now();
      LocalDate start=  now.withDayOfMonth(1);
        LocalDate end=  now.withDayOfMonth(now.lengthOfMonth());
       List<Expense> list= expenseRepository.findByProfileIdAndDateBetween(profile.getId(),start,end);
       return list.stream().map(this::toDTO).toList();



    }
    public void deleteExpense(Long expenseId){
        Profile profile=profileService.getCurrentProfile();
   Expense  expense=     expenseRepository.findById(expenseId).orElseThrow(()->new RuntimeException("Expense not found"));

   if(!expense.getProfile().getId().equals(profile.getId())){
       throw new RuntimeException("Unauthorized to delete this expense");

   }

   expenseRepository.delete(expense);


    }

    public List<ExpenseDTO> getLatest5expensenseForCurrentUser(){
        Profile profile=profileService.getCurrentProfile();
      List<Expense> list=  expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
      return list.stream().map(this::toDTO).toList();
    }

    public BigDecimal getTotalExpenseForCurrentUser(){
      Profile profile=  profileService.getCurrentProfile();
   BigDecimal total=   expenseRepository.findTotalExpenseByProfileId(profile.getId());
   return  total!=null?total:BigDecimal.ZERO;

    }

    public List<ExpenseDTO> filterExpenses(LocalDate start, LocalDate end, String keyword, Sort sort){

      Profile  profile= profileService.getCurrentProfile();
   List<Expense> list=   expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),start,end,keyword,sort);
  return list.stream().map(this::toDTO).toList();
    }

    public List<ExpenseDTO> getExpensesForUserOnDate(Long profileId,LocalDate date){
      List<Expense> list=  expenseRepository.findByProfileIdAndDate(profileId,date);
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
