package com.garja.Garja.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garja.Garja.Model.Category;
import com.garja.Garja.Service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
    

    @Autowired
    private CategoryService categoryService;


    @PostMapping("/add")
    public Category addCategory(Category category) {
        return categoryService.addCategory(category);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/all")
    public List<Category> getAllCategories() { 
        return categoryService.getAllCategories();
    }

    
}
