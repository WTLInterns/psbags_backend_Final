package com.psbags.PSBags.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psbags.PSBags.DTO.SubcategoryRequest;
import com.psbags.PSBags.DTO.SubcategoryResponse;
import com.psbags.PSBags.Model.Subcategory;
import com.psbags.PSBags.Repo.SubcategoryRepo;

@Service
public class SubcategoryService {

    @Autowired
    private SubcategoryRepo subcategoryRepo;

    public SubcategoryResponse createSubcategory(SubcategoryRequest request) {
        // Check if subcategory already exists
        if (subcategoryRepo.existsByCategoryNameAndSubcategoryName(
                request.getCategoryName(), request.getSubcategoryName())) {
            throw new RuntimeException("Subcategory already exists for this category");
        }

        Subcategory subcategory = new Subcategory();
        subcategory.setCategoryName(request.getCategoryName());
        subcategory.setSubcategoryName(request.getSubcategoryName());

        Subcategory savedSubcategory = subcategoryRepo.save(subcategory);
        return convertToResponse(savedSubcategory);
    }

    public List<SubcategoryResponse> getAllSubcategories() {
        return subcategoryRepo.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<SubcategoryResponse> getSubcategoriesByCategory(String categoryName) {
        return subcategoryRepo.findByCategoryName(categoryName)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public SubcategoryResponse updateSubcategory(int id, SubcategoryRequest request) {
        Subcategory subcategory = subcategoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subcategory not found"));

        // Check if updated subcategory already exists (excluding current one)
        if (!subcategory.getCategoryName().equals(request.getCategoryName()) ||
            !subcategory.getSubcategoryName().equals(request.getSubcategoryName())) {
            if (subcategoryRepo.existsByCategoryNameAndSubcategoryName(
                    request.getCategoryName(), request.getSubcategoryName())) {
                throw new RuntimeException("Subcategory already exists for this category");
            }
        }

        subcategory.setCategoryName(request.getCategoryName());
        subcategory.setSubcategoryName(request.getSubcategoryName());

        Subcategory updatedSubcategory = subcategoryRepo.save(subcategory);
        return convertToResponse(updatedSubcategory);
    }

    public void deleteSubcategory(int id) {
        if (!subcategoryRepo.existsById(id)) {
            throw new RuntimeException("Subcategory not found");
        }
        subcategoryRepo.deleteById(id);
    }

    private SubcategoryResponse convertToResponse(Subcategory subcategory) {
        SubcategoryResponse response = new SubcategoryResponse();
        response.setId(subcategory.getId());
        response.setCategoryName(subcategory.getCategoryName());
        response.setSubcategoryName(subcategory.getSubcategoryName());
        response.setCreatedAt(subcategory.getCreatedAt());
        response.setUpdatedAt(subcategory.getUpdatedAt());
        return response;
    }
}