package com.Soham.MoneyManager.Service;

import com.Soham.MoneyManager.DTO.CategoryDTO;
import com.Soham.MoneyManager.DTO.ProfileDTO;
import com.Soham.MoneyManager.Entities.Category;
import com.Soham.MoneyManager.Entities.Profile;

import java.util.List;

public interface CategoryService {

    Category toEntity(CategoryDTO categoryDTO, Profile profile);
    CategoryDTO toDTO(Category category);
    CategoryDTO saveCategory(CategoryDTO categoryDTO);
    List<CategoryDTO> getCategoriesForCurrentUser();
    List<CategoryDTO> getCategoriesByTypeOFrCurrentUser(String type);
    CategoryDTO updateCategory(Long categoryId,CategoryDTO categoryDTO);





}
