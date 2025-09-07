package com.Soham.MoneyManager.Controllers;

import com.Soham.MoneyManager.DTO.ExpenseDTO;
import com.Soham.MoneyManager.DTO.IncomeDTO;
import com.Soham.MoneyManager.Service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incomes")
public class IncomeController {
    @Autowired
    private IncomeService incomeService;


    @PostMapping
    public ResponseEntity<IncomeDTO> addExpense(@RequestBody IncomeDTO expenseDTO){
        IncomeDTO saved=   incomeService.addIncome(expenseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }
    @GetMapping
    private ResponseEntity<List<IncomeDTO>> getExpenses(){
        List<IncomeDTO> expenses=incomeService.getCurrentMonthIncomeForCurrentUser();
        return ResponseEntity.ok(expenses);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delIncome(@PathVariable Long id){

        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }
}
