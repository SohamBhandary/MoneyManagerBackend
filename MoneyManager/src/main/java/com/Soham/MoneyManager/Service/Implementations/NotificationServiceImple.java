package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.DTO.ExpenseDTO;
import com.Soham.MoneyManager.Entities.Profile;
import com.Soham.MoneyManager.Repositories.ProfileRepository;

import com.Soham.MoneyManager.Service.EmailService;
import com.Soham.MoneyManager.Service.ExpenseService;
import com.Soham.MoneyManager.Service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class NotificationServiceImple implements NotificationService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ExpenseService expenseService;

    @Value("${money.manager.frontend.url}")
    private String froentendUrl;

    @Scheduled(cron = "0 0 22 * * * ", zone = "IST")
    public void sendDailyIncomeExpenseReminder() {
        log.info("job started:sendDailyIncomeExpenseReminder()");
        List<Profile> profileList = profileRepository.findAll();
        for (Profile profile : profileList) {
            String body = "Hi " + profile.getFullName() + ",<br><br>"
                    + "This is a friendly reminder to add your income and expenses for today in Money Manager.<br><br>"
                    + "<a href=" + froentendUrl + " style='display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'>Go to Money Manager</a>"
                    + "<br><br>Best regards,<br>Money Manager Team";
            emailService.sendEmail(profile.getEmail(), "Daily reminder: Add your income and expenses", body);
        }
        log.info("Job completed:sendDailyIncomeExpenseReminder() ");
    }

    @Scheduled(cron = "0 0 23 * * * ", zone = "IST")
    public void sendDailyExpenseSummary() {
        log.info("Job started:sendDailyExpenseSummary() ");
        List<Profile> profileList = profileRepository.findAll();
        for (Profile profile : profileList) {
            List<ExpenseDTO> todaysExpenses = expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());
            if (!todaysExpenses.isEmpty()) {
                StringBuilder table = new StringBuilder();
                table.append("<table style='border-collapse:collapse;width:100%;'>");
                table.append("<tr style='background-color:#f2f2f2;'><th style='border:1px solid #ddd;padding:8px;'>S.No</th><th style='border:1px solid #ddd;padding:8px;'>Name</th><th style='border:1px solid #ddd;padding:8px;'>Amount</th><th style='border:1px solid #ddd;padding:8px;'>Category</th></tr>");
                int i = 1;
                for (ExpenseDTO expense : todaysExpenses) {
                    table.append("<tr>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(i++).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getName()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getAmount()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getCategoryId() != null ? expense.getCategoryName() : "N/A").append("</td>");
                    table.append("</tr>");
                }
                table.append("</table>");
                String body = "Hi " + profile.getFullName() + ",<br/><br/> Here is a summary of your expenses for today:<br/><br/>" + table + "<br/><br/>Best regards,<br/>Money Manager Team";
                emailService.sendEmail(profile.getEmail(), "Your daily Expense summary", body);
            }
        }
        log.info("Job completed: sendDailyExpenseSummary()");
    }
}





