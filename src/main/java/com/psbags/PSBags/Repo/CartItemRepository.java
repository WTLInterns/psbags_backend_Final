package com.psbags.PSBags.Repo;

import com.psbags.PSBags.Model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
