package com.psbags.PSBags.Repo;

import com.psbags.PSBags.Model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AnnouncementRepo extends JpaRepository<Announcement, Long> {
    
    @Query("SELECT a FROM Announcement a WHERE a.isActive = true ORDER BY a.updatedAt DESC")
    Optional<Announcement> findActiveAnnouncement();
    
    @Query("SELECT a FROM Announcement a ORDER BY a.updatedAt DESC")
    Optional<Announcement> findLatestAnnouncement();
}