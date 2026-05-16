package com.psbags.PSBags.Repo;

import com.psbags.PSBags.Model.Cart;
import com.psbags.PSBags.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
