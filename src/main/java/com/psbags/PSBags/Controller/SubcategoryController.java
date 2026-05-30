package com.psbags.PSBags.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.psbags.PSBags.DTO.SubcategoryRequest;
import com.psbags.PSBags.DTO.SubcategoryResponse;
import com.psbags.PSBags.Service.SubcategoryService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class SubcategoryController {

    @Autowired
    private SubcategoryService subcategoryService;

    // Admin APIs
    @PostMapping("/admin/subcategory")
    public ResponseEntity<?> createSubcategory(@Valid @RequestBody SubcategoryRequest request) {
        try {
            SubcategoryResponse response = subcategoryService.createSubcategory(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/admin/subcategory")
    public ResponseEntity<List<SubcategoryResponse>> getAllSubcategories() {
        List<SubcategoryResponse> subcategories = subcategoryService.getAllSubcategories();
        return ResponseEntity.ok(subcategories);
    }

    @PutMapping("/admin/subcategory/{id}")
    public ResponseEntity<?> updateSubcategory(@PathVariable int id, 
                                             @Valid @RequestBody SubcategoryRequest request) {
        try {
            SubcategoryResponse response = subcategoryService.updateSubcategory(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/admin/subcategory/{id}")
    public ResponseEntity<?> deleteSubcategory(@PathVariable int id) {
        try {
            subcategoryService.deleteSubcategory(id);
            return ResponseEntity.ok("Subcategory deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Public APIs
    @GetMapping("/public/subcategory")
    public ResponseEntity<List<SubcategoryResponse>> getSubcategoriesByCategory(
            @RequestParam String category) {
        List<SubcategoryResponse> subcategories = subcategoryService.getSubcategoriesByCategory(category);
        return ResponseEntity.ok(subcategories);
    }
}