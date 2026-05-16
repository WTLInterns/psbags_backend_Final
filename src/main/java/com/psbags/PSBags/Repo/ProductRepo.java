package com.psbags.PSBags.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.psbags.PSBags.Model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer>{
    
        List<Product> findTop4ByOrderByDateDescTimeDesc();

}
