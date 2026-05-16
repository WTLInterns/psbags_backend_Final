package com.psbags.PSBags.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psbags.PSBags.Model.Category;
import com.psbags.PSBags.Repo.CategoryRepo;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepo categoryRepo;

    public Category addCategory(Category category) {
        return categoryRepo.save(category);
    }

    public void deleteCategory(int id) {
        categoryRepo.deleteById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    public Category updateCategory(int id, Category category) {
        Category existingCategory = categoryRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        
        existingCategory.setCategoryName(category.getCategoryName());
        
        return categoryRepo.save(existingCategory);
    }
}
