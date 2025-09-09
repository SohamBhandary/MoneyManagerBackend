package com.Soham.MoneyManager.Controllers;

import com.Soham.MoneyManager.Entities.Profile;
import com.Soham.MoneyManager.Service.*;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private ExcelService excelService;
    @Autowired
    private IncomeService incomeService;
    @Autowired
   private ExpenseService expenseService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ProfileService profileService;
    @GetMapping("/incomeExcel")
    public ResponseEntity<Void> emailIncomeExcel() throws MessagingException {
        Profile profile=profileService.getCurrentProfile();
        ByteArrayOutputStream boas=new ByteArrayOutputStream();
        excelService.writeIncomesToExcel(boas,incomeService.getCurrentMonthIncomeForCurrentUser());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        excelService.writeIncomesToExcel(baos, incomeService.getCurrentMonthIncomeForCurrentUser());
        emailService.sendEmailWithAttachment(profile.getEmail(),
                "Your Income Excel Report",
                "Please find attached your income report",
                baos.toByteArray(),
                "income.xlsx");
        return ResponseEntity.ok(null);



    }
    @GetMapping("/expense-excel")
    public ResponseEntity<Void> emailExpenseExcel() throws IOException, MessagingException {
        Profile profile = profileService.getCurrentProfile();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        excelService.writeExpensesToExcel(baos, expenseService.getCurrentMonthExpenseForCurrentUser());
        emailService.sendEmailWithAttachment(
                profile.getEmail(),
                "Your Expense Excel Report",
                "Please find attached your expense report.",
                baos.toByteArray(),
                "expenses.xlsx");
        return ResponseEntity.ok(null);
    }

}
