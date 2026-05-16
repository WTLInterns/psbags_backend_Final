package com.psbags.PSBags.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.psbags.PSBags.Model.AdminProfile;

@Repository
public interface AdminProfileRepo extends JpaRepository<AdminProfile, Integer>{
    
}
