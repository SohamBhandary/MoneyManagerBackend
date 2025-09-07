package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.DTO.CategoryDTO;
import com.Soham.MoneyManager.Entities.Category;
import com.Soham.MoneyManager.Entities.Profile;
import com.Soham.MoneyManager.Repositories.CategoryRepository;
import com.Soham.MoneyManager.Service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service

public class CategoryServiceImple implements com.Soham.MoneyManager.Service.CategoryService {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
      Profile profile=  profileService.getCurrentProfile();
        if(categoryRepository.existsByNameAndProfileId(categoryDTO.getName(),profile.getId())){
            throw new RuntimeException("Category with this name already exsists");


        }
        Category newcategory=toEntity(categoryDTO,profile);
        newcategory=categoryRepository.save(newcategory);
        return  toDTO(newcategory);



    }

    @Override
    public List<CategoryDTO> getCategoriesForCurrentUser() {
        Profile profile=profileService.getCurrentProfile();
        List<Category> categories=categoryRepository.findByProfileId(profile.getId());
        return categories.stream().map(this::toDTO).toList();
    }

    @Override
    public List<CategoryDTO> getCategoriesByTypeOFrCurrentUser(String type) {
    Profile profile=    profileService.getCurrentProfile();
 List<Category> entities=  categoryRepository.findByTypeAndProfileId(type, profile.getId());
 return entities.stream().map(this::toDTO).toList();
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
       Profile profile=profileService.getCurrentProfile();
     Category exsisiting=  categoryRepository.findByIdAndProfileId(categoryId,profile.getId()).orElseThrow(()-> new RuntimeException("Category not accesible"));
     exsisiting.setName(categoryDTO.getName());
     exsisiting.setIcon(categoryDTO.getIcon());
    exsisiting= categoryRepository.save(exsisiting);
     return toDTO(exsisiting);
    }


    @Override
    public Category toEntity(CategoryDTO categoryDTO, Profile profile) {
        return Category.builder().name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .profile(profile)
                .type(categoryDTO.getType())
                .build();

    }

    @Override
    public CategoryDTO toDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .profileId(category.getProfile()!=null?category.getProfile().getId():null)
                .name(category.getName())
                .icon(category.getIcon())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .type(category.getType())
                .build();

    }


}
