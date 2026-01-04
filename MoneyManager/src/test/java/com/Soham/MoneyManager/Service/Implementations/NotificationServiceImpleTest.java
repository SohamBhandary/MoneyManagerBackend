package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.DTO.ExpenseDTO;
import com.Soham.MoneyManager.Entities.Profile;
import com.Soham.MoneyManager.Repositories.ProfileRepository;
import com.Soham.MoneyManager.Service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImpleTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private NotificationServiceImple notificationService;

    private Profile profile;

    @BeforeEach
    void setUp() {
        profile = Profile.builder()
                .id(1L)
                .fullName("Soham Das")
                .email("soham@test.com")
                .build();

        // Inject @Value field manually
        ReflectionTestUtils.setField(
                notificationService,
                "froentendUrl",
                "http://localhost:3000"
        );
    }



    @Test
    void sendDailyIncomeExpenseReminder_shouldSendEmailToAllProfiles() {
        when(profileRepository.findAll()).thenReturn(List.of(profile));

        notificationService.sendDailyIncomeExpenseReminder();

        verify(emailService, times(1))
                .sendEmail(
                        eq("soham@test.com"),
                        eq("Daily reminder: Add your income and expenses"),
                        contains("Go to Money Manager")
                );
    }



    @Test
    void sendDailyExpenseSummary_shouldSendEmailWhenExpensesExist() {
        ExpenseDTO expenseDTO = ExpenseDTO.builder()
                .name("Food")
                .amount(BigDecimal.valueOf(250))
                .categoryName("Groceries")
                .build();

        when(profileRepository.findAll()).thenReturn(List.of(profile));
        when(expenseService.getExpensesForUserOnDate(
                profile.getId(),
                LocalDate.now()
        )).thenReturn(List.of(expenseDTO));

        notificationService.sendDailyExpenseSummary();

        verify(emailService, times(1))
                .sendEmail(
                        eq("soham@test.com"),
                        eq("Your daily Expense summary"),
                        contains("Food")
                );
    }

    @Test
    void sendDailyExpenseSummary_shouldNotSendEmailWhenNoExpenses() {
        when(profileRepository.findAll()).thenReturn(List.of(profile));
        when(expenseService.getExpensesForUserOnDate(
                profile.getId(),
                LocalDate.now()
        )).thenReturn(List.of());

        notificationService.sendDailyExpenseSummary();

        verify(emailService, never()).sendEmail(any(), any(), any());
    }
}
