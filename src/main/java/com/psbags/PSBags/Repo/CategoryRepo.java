package com.psbags.PSBags.Repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.psbags.PSBags.Model.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    
}
