package com.Soham.MoneyManager.Controllers;

import com.Soham.MoneyManager.DTO.ExpenseDTO;
import com.Soham.MoneyManager.DTO.FIlterDTO;
import com.Soham.MoneyManager.DTO.IncomeDTO;
import com.Soham.MoneyManager.Service.ExpenseService;
import com.Soham.MoneyManager.Service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/filter")
public class FilterController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private IncomeService incomeService;

    @PostMapping
    public ResponseEntity<?> filterTransactsions(@RequestBody FIlterDTO fIlterDTO) {
        LocalDate start = fIlterDTO.getStart() != null ? fIlterDTO.getStart() : LocalDate.MIN;
        LocalDate end = fIlterDTO.getEnd() != null ? fIlterDTO.getEnd() : LocalDate.now();
        String keyword = fIlterDTO.getKeyword() != null ? fIlterDTO.getKeyword() : "";
        String sortField = fIlterDTO.getSortField() != null ? fIlterDTO.getSortField() : "date";
        Sort.Direction direction = "desc".equalsIgnoreCase(fIlterDTO.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);

        if ("income".equalsIgnoreCase(fIlterDTO.getType())) {
            List<IncomeDTO> incomes = incomeService.filterExpenses(start, end, keyword, sort);
            return ResponseEntity.ok(incomes);
        } else if ("expense".equalsIgnoreCase(fIlterDTO.getType())) {
            List<ExpenseDTO> expenses = expenseService.filterExpenses(start, end, keyword, sort);
            return ResponseEntity.ok(expenses);
        } else {
            return ResponseEntity.badRequest().body("Invalid type, must be income or expense");
        }
    }
}
