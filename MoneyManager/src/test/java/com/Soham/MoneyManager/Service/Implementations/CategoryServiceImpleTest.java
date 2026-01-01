package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.DTO.CategoryDTO;
import com.Soham.MoneyManager.Entities.Category;
import com.Soham.MoneyManager.Entities.Profile;
import com.Soham.MoneyManager.Repositories.CategoryRepository;
import com.Soham.MoneyManager.Service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImpleTest {

    @Mock
    private ProfileService profileService;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImple categoryService;
    private Profile profile;
    private CategoryDTO categoryDTO;
    private Category category;

    @BeforeEach
    void setUp() {
        profile = Profile.builder()
                .id(1L)
                .build();

        categoryDTO = CategoryDTO.builder()
                .name("Food")
                .icon("🍔")
                .type("EXPENSE")
                .build();

        category = Category.builder()
                .id(10L)
                .name("Food")
                .icon("🍔")
                .type("EXPENSE")
                .profile(profile)
                .build();
    }



    @Test
    void saveCategory_success() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(categoryRepository.existsByNameAndProfileId("Food", 1L))
                .thenReturn(false);
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        CategoryDTO result = categoryService.saveCategory(categoryDTO);

        assertNotNull(result);
        assertEquals("Food", result.getName());
        assertEquals("EXPENSE", result.getType());

        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void saveCategory_duplicateCategory_throwsException() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(categoryRepository.existsByNameAndProfileId("Food", 1L))
                .thenReturn(true);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> categoryService.saveCategory(categoryDTO)
        );

        assertEquals("Category with this name already exsists", ex.getMessage());
        verify(categoryRepository, never()).save(any());
    }


    @Test
    void getCategoriesForCurrentUser_success() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(categoryRepository.findByProfileId(1L))
                .thenReturn(List.of(category));

        List<CategoryDTO> result = categoryService.getCategoriesForCurrentUser();

        assertEquals(1, result.size());
        assertEquals("Food", result.get(0).getName());
    }



    @Test
    void getCategoriesByTypeForCurrentUser_success() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(categoryRepository.findByTypeAndProfileId("EXPENSE", 1L))
                .thenReturn(List.of(category));

        List<CategoryDTO> result =
                categoryService.getCategoriesByTypeOFrCurrentUser("EXPENSE");

        assertEquals(1, result.size());
        assertEquals("EXPENSE", result.get(0).getType());
    }



    @Test
    void updateCategory_success() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(categoryRepository.findByIdAndProfileId(10L, 1L))
                .thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CategoryDTO updateDTO = CategoryDTO.builder()
                .name("Groceries")
                .icon("🛒")
                .build();

        CategoryDTO result = categoryService.updateCategory(10L, updateDTO);

        assertEquals("Groceries", result.getName());
        assertEquals("🛒", result.getIcon());
    }

    @Test
    void updateCategory_notAccessible_throwsException() {
        when(profileService.getCurrentProfile()).thenReturn(profile);
        when(categoryRepository.findByIdAndProfileId(10L, 1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> categoryService.updateCategory(10L, categoryDTO)
        );

        assertEquals("Category not accesible", ex.getMessage());
    }
}
