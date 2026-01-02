package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.DTO.ExpenseDTO;
import com.Soham.MoneyManager.Entities.Category;
import com.Soham.MoneyManager.Entities.Expense;
import com.Soham.MoneyManager.Entities.Profile;
import com.Soham.MoneyManager.Repositories.CategoryRepository;
import com.Soham.MoneyManager.Repositories.ExpenseRepository;
import com.Soham.MoneyManager.Repositories.IncomeReposiotry;
import com.Soham.MoneyManager.Service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceImpleTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private IncomeReposiotry incomeReposiotry;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ExpenseServiceImple expenseService;

    private Profile profile;
    private Category category;
    private Expense expense;
    private ExpenseDTO expenseDTO;

    @BeforeEach
    void setUp() {
        profile = Profile.builder()
                .id(1L)
                .build();

        category = Category.builder()
                .id(2L)
                .name("Food")
                .build();

        expenseDTO = ExpenseDTO.builder()
                .name("Lunch")
                .icon("🍔")
                .amount(new BigDecimal("250"))
                .date(LocalDate.now())
                .categoryId(2L)
                .build();

        expense = Expense.builder()
                .id(10L)
                .name("Lunch")
                .icon("🍔")
                .amount(new BigDecimal("250"))
                .date(LocalDate.now())
                .profile(profile)
                .category(category)
                .build();
    }



    @Test
    void addExpense_success() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        ExpenseDTO result = expenseService.addExpense(expenseDTO);

        assertNotNull(result);
        assertEquals("Lunch", result.getName());
        assertEquals("Food", result.getCategoryName());

        verify(expenseRepository).save(any(Expense.class));
    }

    @Test
    void addExpense_categoryNotFound() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> expenseService.addExpense(expenseDTO));
    }



    @Test
    void getCurrentMonthExpenseForCurrentUser_success() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(expenseRepository.findByProfileIdAndDateBetween(
                anyLong(), any(), any()))
                .thenReturn(List.of(expense));

        List<ExpenseDTO> result =
                expenseService.getCurrentMonthExpenseForCurrentUser();

        assertEquals(1, result.size());
        assertEquals("Lunch", result.get(0).getName());
    }



    @Test
    void deleteExpense_success() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(expenseRepository.findById(10L))
                .thenReturn(Optional.of(expense));

        expenseService.deleteExpense(10L);

        verify(expenseRepository).delete(expense);
    }

    @Test
    void deleteExpense_unauthorized() {
        Profile otherProfile = Profile.builder().id(99L).build();
        expense.setProfile(otherProfile);

        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(expenseRepository.findById(10L))
                .thenReturn(Optional.of(expense));

        assertThrows(RuntimeException.class,
                () -> expenseService.deleteExpense(10L));
    }



    @Test
    void getLatest5expensenseForCurrentUser_success() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(expenseRepository.findTop5ByProfileIdOrderByDateDesc(1L))
                .thenReturn(List.of(expense));

        List<ExpenseDTO> result =
                expenseService.getLatest5expensenseForCurrentUser();

        assertEquals(1, result.size());
    }



    @Test
    void getTotalExpenseForCurrentUser_whenNull_returnZero() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(expenseRepository.findTotalExpenseByProfileId(1L))
                .thenReturn(null);

        BigDecimal result =
                expenseService.getTotalExpenseForCurrentUser();

        assertEquals(BigDecimal.ZERO, result);
    }



    @Test
    void filterExpenses_success() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(expenseRepository
                .findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
                        eq(1L),
                        any(),
                        any(),
                        eq("lun"),
                        any(Sort.class)))
                .thenReturn(List.of(expense));

        List<ExpenseDTO> result =
                expenseService.filterExpenses(
                        LocalDate.now().minusDays(5),
                        LocalDate.now(),
                        "lun",
                        Sort.by("date").descending()
                );

        assertEquals(1, result.size());
        assertEquals("Lunch", result.get(0).getName());
    }
}
