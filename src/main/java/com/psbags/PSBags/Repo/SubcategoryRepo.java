package com.psbags.PSBags.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.psbags.PSBags.Model.Subcategory;

@Repository
public interface SubcategoryRepo extends JpaRepository<Subcategory, Integer> {
    
    List<Subcategory> findByCategoryName(String categoryName);
    
    Optional<Subcategory> findByCategoryNameAndSubcategoryName(String categoryName, String subcategoryName);
    
    boolean existsByCategoryNameAndSubcategoryName(String categoryName, String subcategoryName);
}