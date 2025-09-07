package com.Soham.MoneyManager.Controllers;

import com.Soham.MoneyManager.DTO.CategoryDTO;

import com.Soham.MoneyManager.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO){
        CategoryDTO saveCategory=categoryService.saveCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveCategory);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories(){
      List<CategoryDTO> categoryDTOS=  categoryService.getCategoriesForCurrentUser();
      return ResponseEntity.ok(categoryDTOS);

    }


    @GetMapping("/{type}")
    public ResponseEntity<List<CategoryDTO>> getCategoryByTypeForCurrentUser(@PathVariable String type){
      List<CategoryDTO> list=  categoryService.getCategoriesByTypeOFrCurrentUser(type);
      return ResponseEntity.ok(list);


    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId,@RequestBody CategoryDTO categoryDTO){
      CategoryDTO updateCategory=  categoryService.updateCategory(categoryId,categoryDTO);
       return  ResponseEntity.ok(updateCategory);
    }


}
