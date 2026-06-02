package com.psbags.PSBags.Service;

import com.psbags.PSBags.Model.Announcement;
import com.psbags.PSBags.Repo.AnnouncementRepo;
import com.psbags.PSBags.DTO.requests.AnnouncementRequest;
import com.psbags.PSBags.DTO.response.AnnouncementResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {
    
    @Autowired
    private AnnouncementRepo announcementRepo;
    
    public AnnouncementResponse getActiveAnnouncements() {
        Optional<Announcement> announcement = announcementRepo.findActiveAnnouncement();
        
        if (announcement.isPresent()) {
            return mapToResponse(announcement.get(), true);
        }
        
        // Return default if none found
        AnnouncementResponse response = new AnnouncementResponse();
        List<String> defaultAnnouncements = new ArrayList<>();
        defaultAnnouncements.add("10% off when you subscribe to our emails. Brand exclusions apply. T&Cs apply");
        defaultAnnouncements.add("Guess what's just landed? Discover the latest arrivals now");
        defaultAnnouncements.add("All over india delivery and free returns - shop now");
        response.setAnnouncements(defaultAnnouncements);
        return response;
    }
    
    public AnnouncementResponse getCurrentAnnouncement() {
        Optional<Announcement> announcement = announcementRepo.findLatestAnnouncement();
        
        if (announcement.isPresent()) {
            return mapToResponse(announcement.get(), false);
        }
        
        // Return empty if none found
        return new AnnouncementResponse();
    }
    
    public AnnouncementResponse updateAnnouncement(AnnouncementRequest request, String updatedBy) {
        Optional<Announcement> existingOpt = announcementRepo.findLatestAnnouncement();
        
        Announcement announcement;
        if (existingOpt.isPresent()) {
            announcement = existingOpt.get();
        } else {
            announcement = new Announcement();
            announcement.setCreatedBy(updatedBy);
        }
        
        announcement.setText1(request.getText1());
        announcement.setText2(request.getText2());
        announcement.setText3(request.getText3());
        announcement.setText4(request.getText4());
        announcement.setIsActive(request.getIsActive());
        announcement.setUpdatedBy(updatedBy);
        
        Announcement saved = announcementRepo.save(announcement);
        return mapToResponse(saved, false);
    }
    
    private AnnouncementResponse mapToResponse(Announcement announcement, boolean publicView) {
        AnnouncementResponse response = new AnnouncementResponse();
        response.setId(announcement.getId());
        response.setText1(announcement.getText1());
        response.setText2(announcement.getText2());
        response.setText3(announcement.getText3());
        response.setText4(announcement.getText4());
        response.setIsActive(announcement.getIsActive());
        response.setCreatedAt(announcement.getCreatedAt());
        response.setUpdatedAt(announcement.getUpdatedAt());
        response.setCreatedBy(announcement.getCreatedBy());
        response.setUpdatedBy(announcement.getUpdatedBy());
        
        if (publicView) {
            List<String> announcements = new ArrayList<>();
            if (announcement.getText1() != null && !announcement.getText1().trim().isEmpty()) {
                announcements.add(announcement.getText1());
            }
            if (announcement.getText2() != null && !announcement.getText2().trim().isEmpty()) {
                announcements.add(announcement.getText2());
            }
            if (announcement.getText3() != null && !announcement.getText3().trim().isEmpty()) {
                announcements.add(announcement.getText3());
            }
            if (announcement.getText4() != null && !announcement.getText4().trim().isEmpty()) {
                announcements.add(announcement.getText4());
            }
            response.setAnnouncements(announcements);
        }
        
        return response;
    }
}