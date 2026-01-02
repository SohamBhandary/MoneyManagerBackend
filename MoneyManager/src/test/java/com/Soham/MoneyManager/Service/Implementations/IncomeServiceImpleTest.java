package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.DTO.IncomeDTO;
import com.Soham.MoneyManager.Entities.Category;
import com.Soham.MoneyManager.Entities.Income;
import com.Soham.MoneyManager.Entities.Profile;
import com.Soham.MoneyManager.Repositories.CategoryRepository;
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
class IncomeServiceImpleTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private IncomeReposiotry incomeReposiotry;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private IncomeServiceImple incomeService;

    private Profile profile;
    private Category category;
    private Income income;
    private IncomeDTO incomeDTO;

    @BeforeEach
    void setUp() {
        profile = Profile.builder()
                .id(1L)
                .build();

        category = Category.builder()
                .id(2L)
                .name("Salary")
                .build();

        incomeDTO = IncomeDTO.builder()
                .name("August Salary")
                .icon("💰")
                .amount(new BigDecimal("50000"))
                .date(LocalDate.now())
                .categoryId(2L)
                .build();

        income = Income.builder()
                .id(10L)
                .name("August Salary")
                .icon("💰")
                .amount(new BigDecimal("50000"))
                .date(LocalDate.now())
                .profile(profile)
                .category(category)
                .build();
    }



    @Test
    void addIncome_success() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(incomeReposiotry.save(any(Income.class))).thenReturn(income);

        IncomeDTO result = incomeService.addIncome(incomeDTO);

        assertNotNull(result);
        assertEquals("August Salary", result.getName());
        assertEquals("Salary", result.getCategoryName());

        verify(incomeReposiotry).save(any(Income.class));
    }

    @Test
    void addIncome_categoryNotFound() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> incomeService.addIncome(incomeDTO));
    }



    @Test
    void getCurrentMonthIncomeForCurrentUser_success() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(incomeReposiotry.findByProfileIdAndDateBetween(
                anyLong(), any(), any()))
                .thenReturn(List.of(income));

        List<IncomeDTO> result =
                incomeService.getCurrentMonthIncomeForCurrentUser();

        assertEquals(1, result.size());
        assertEquals("August Salary", result.get(0).getName());
    }



    @Test
    void deleteIncome_success() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(incomeReposiotry.findById(10L))
                .thenReturn(Optional.of(income));

        incomeService.deleteIncome(10L);

        verify(incomeReposiotry).delete(income);
    }

    @Test
    void deleteIncome_unauthorized() {
        Profile otherProfile = Profile.builder().id(99L).build();
        income.setProfile(otherProfile);

        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(incomeReposiotry.findById(10L))
                .thenReturn(Optional.of(income));

        assertThrows(RuntimeException.class,
                () -> incomeService.deleteIncome(10L));
    }



    @Test
    void getLatest5IncomeForCurrentUser_success() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(incomeReposiotry.findTop5ByProfileIdOrderByDateDesc(1L))
                .thenReturn(List.of(income));

        List<IncomeDTO> result =
                incomeService.getLatest5expensenseForCurrentUser();

        assertEquals(1, result.size());
    }



    @Test
    void getTotalIncomeForCurrentUser_whenNull_returnZero() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(incomeReposiotry.findTotalExpenseByProfileId(1L))
                .thenReturn(null);

        BigDecimal result =
                incomeService.getTotalExpenseForCurrentUser();

        assertEquals(BigDecimal.ZERO, result);
    }


    @Test
    void filterIncome_success() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(incomeReposiotry
                .findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
                        eq(1L),
                        any(),
                        any(),
                        eq("salary"),
                        any(Sort.class)))
                .thenReturn(List.of(income));

        List<IncomeDTO> result =
                incomeService.filterExpenses(
                        LocalDate.now().minusDays(10),
                        LocalDate.now(),
                        "salary",
                        Sort.by("date").descending()
                );

        assertEquals(1, result.size());
        assertEquals("August Salary", result.get(0).getName());
    }
}
