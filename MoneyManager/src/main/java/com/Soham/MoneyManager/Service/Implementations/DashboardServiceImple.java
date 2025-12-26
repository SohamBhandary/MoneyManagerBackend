package com.Soham.MoneyManager.Service.Implementations;


import com.Soham.MoneyManager.DTO.ExpenseDTO;
import com.Soham.MoneyManager.DTO.IncomeDTO;
import com.Soham.MoneyManager.DTO.RecentTransacationsDTO;
import com.Soham.MoneyManager.Entities.Profile;
import com.Soham.MoneyManager.Service.DashboardService;
import com.Soham.MoneyManager.Service.ExpenseService;
import com.Soham.MoneyManager.Service.IncomeService;
import com.Soham.MoneyManager.Service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.DoubleStream.concat;

@Service
@Slf4j
public class DashboardServiceImple implements DashboardService {
    @Autowired
    private IncomeService incomeService;
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private ProfileService profileService;

    public Map<String,Object> getDashboardData(){
        log.info("Fetching dashboard data for current user");
        Profile profile=profileService.getCurrentProfile();
        Map<String,Object> returnVal= new LinkedHashMap<>();
        log.info("Fetching latest 5 incomes and current month expenses for profile {}", profile.getId());
     List<IncomeDTO> latestIncome= incomeService.getLatest5expensenseForCurrentUser();
     List<ExpenseDTO> latestExpense= expenseService.getCurrentMonthExpenseForCurrentUser();
        List<RecentTransacationsDTO> recentTransactions = Stream
                .concat(
                        latestIncome.stream().map(income -> RecentTransacationsDTO.builder()
                                .id(income.getId())
                                .profileId(profile.getId())
                                .icon(income.getIcon())
                                .name(income.getName())
                                .amount(income.getAmount())
                                .date(income.getDate())
                                .createdAt(income.getCreatedAt())
                                .updatedAt(income.getUpdatedAt())
                                .type("income")
                                .build()
                        ),
                        latestExpense.stream().map(expense -> RecentTransacationsDTO.builder()
                                .id(expense.getId())
                                .profileId(profile.getId())
                                .icon(expense.getIcon())
                                .name(expense.getName())
                                .amount(expense.getAmount())
                                .date(expense.getDate())
                                .createdAt(expense.getCreatedAt())
                                .updatedAt(expense.getUpdatedAt())
                                .type("expense")
                                .build()
                        )
                ).sorted((a,b)->{
                    int cmp=b.getDate().compareTo(a.getDate());
                    if(cmp==0 && a.getCreatedAt()!=null && b.getCreatedAt()!=null){
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;
                }).collect(Collectors.toList());
        returnVal.put("totalBalance",incomeService.getTotalExpenseForCurrentUser()
                .subtract(expenseService.getTotalExpenseForCurrentUser()));

        log.info("Calculating total balance, income, and expense");

        returnVal.put("totalIncome",incomeService.getTotalExpenseForCurrentUser());
        returnVal.put("totalExpense",expenseService.getTotalExpenseForCurrentUser());
        returnVal.put("recent5Expenses",latestExpense);
        returnVal.put("recent5Incomes",latestIncome);
        returnVal.put("recenTransactions",recentTransactions);
        log.info("Dashboard data prepared successfully for profile {}", profile.getId());
        return  returnVal;



    }
}
