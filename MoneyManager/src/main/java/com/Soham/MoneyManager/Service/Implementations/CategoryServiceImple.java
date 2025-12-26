package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.DTO.CategoryDTO;
import com.Soham.MoneyManager.Entities.Category;
import com.Soham.MoneyManager.Entities.Profile;
import com.Soham.MoneyManager.Repositories.CategoryRepository;
import com.Soham.MoneyManager.Service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j

public class CategoryServiceImple implements com.Soham.MoneyManager.Service.CategoryService {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
      Profile profile=  profileService.getCurrentProfile();
      log.info("Attempting to save category '{}' for profile {}", categoryDTO.getName(), profile.getId());
        if(categoryRepository.existsByNameAndProfileId(categoryDTO.getName(),profile.getId())){
            log.warn("Category '{}' already exists for profile {}", categoryDTO.getName(), profile.getId());
            throw new RuntimeException("Category with this name already exsists");

        }
        Category newcategory=toEntity(categoryDTO,profile);
        newcategory=categoryRepository.save(newcategory);
        log.info("Category '{}' saved successfully for profile {}", newcategory.getName(), profile.getId());
        return  toDTO(newcategory);



    }

    @Override
    public List<CategoryDTO> getCategoriesForCurrentUser() {
        Profile profile=profileService.getCurrentProfile();
        log.info("Fetching categories for profile {}", profile.getId());
        List<Category> categories=categoryRepository.findByProfileId(profile.getId());
        log.info("Fetched {} categories for profile {}", categories.size(), profile.getId());
        return categories.stream().map(this::toDTO).toList();
    }

    @Override
    public List<CategoryDTO> getCategoriesByTypeOFrCurrentUser(String type) {
    Profile profile=    profileService.getCurrentProfile();
        log.info("Fetching categories of type '{}' for profile {}", type, profile.getId());
 List<Category> entities=  categoryRepository.findByTypeAndProfileId(type, profile.getId());
        log.info("Fetched {} categories of type '{}' for profile {}", entities.size(), type, profile.getId());
 return entities.stream().map(this::toDTO).toList();
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
       Profile profile=profileService.getCurrentProfile();
        log.info("Updating category id '{}' for profile {}", categoryId, profile.getId());

     Category exsisiting=  categoryRepository.findByIdAndProfileId(categoryId,profile.getId()).orElseThrow(()-> new RuntimeException("Category not accesible"));
        log.warn("Category id '{}' not accessible for profile {}", categoryId, profile.getId());
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
