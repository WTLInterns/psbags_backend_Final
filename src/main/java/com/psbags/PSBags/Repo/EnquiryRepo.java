package com.psbags.PSBags.Repo;

import com.psbags.PSBags.Model.Enquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnquiryRepo extends JpaRepository<Enquiry, Long> {

    @Query("SELECT e FROM Enquiry e WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(e.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.mobile) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.companyName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.location) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY e.createdAt DESC")
    Page<Enquiry> searchEnquiries(@Param("search") String search, Pageable pageable);
}
