package com.psbags.PSBags.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.psbags.PSBags.Model.Blog;

@Repository
public interface BlogRepo extends JpaRepository<Blog, Integer> {
    
    // Find blog by slug for SEO-friendly URLs
    Optional<Blog> findBySlug(String slug);
    
    // Find latest 4 blogs ordered by date and time (matching Product pattern)
    List<Blog> findTop4ByOrderByDateDescTimeDesc();
    
    // Find blogs by active status
    List<Blog> findByIsActive(String isActive);
}