package com.psbags.PSBags.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.psbags.PSBags.Model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>{
    public Optional<User> findByEmail(String email);

        boolean existsByEmail(String email);

}
