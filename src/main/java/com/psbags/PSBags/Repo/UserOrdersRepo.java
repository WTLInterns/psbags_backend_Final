package com.psbags.PSBags.Repo;

import com.psbags.PSBags.Model.UserOrders;
import com.psbags.PSBags.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrdersRepo extends JpaRepository<UserOrders, Integer> {
    
    List<UserOrders> findByUser(User user);
    List<UserOrders> findByUserOrderByIdDesc(User user);
}
