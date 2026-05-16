package com.psbags.PSBags.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.psbags.PSBags.Model.UserProfile;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile, Integer>{
    
}
