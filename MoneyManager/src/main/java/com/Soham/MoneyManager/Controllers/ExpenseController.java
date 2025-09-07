package com.Soham.MoneyManager.Controllers;

import com.Soham.MoneyManager.DTO.ExpenseDTO;
import com.Soham.MoneyManager.Service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO expenseDTO){
     ExpenseDTO saved=   expenseService.addExpense(expenseDTO);
     return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }
    @GetMapping
    private ResponseEntity<List<ExpenseDTO>> getExpenses(){
        List<ExpenseDTO> expenses=expenseService.getCurrentMonthExpenseForCurrentUser();
        return ResponseEntity.ok(expenses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delExpense(@PathVariable Long id){

        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }






}
