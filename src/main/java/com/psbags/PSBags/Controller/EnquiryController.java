package com.psbags.PSBags.Controller;

import com.psbags.PSBags.DTO.requests.EnquiryRequest;
import com.psbags.PSBags.DTO.response.EnquiryResponse;
import com.psbags.PSBags.Service.EnquiryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class EnquiryController {

    @Autowired
    private EnquiryService enquiryService;

    // ─── Public: submit enquiry ───────────────────────────────────────────────

    @PostMapping("/public/enquiry")
    public ResponseEntity<EnquiryResponse> submitEnquiry(
            @Valid @RequestBody EnquiryRequest request) {
        EnquiryResponse response = enquiryService.submitEnquiry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─── Admin: list with search + pagination ─────────────────────────────────

    @GetMapping("/admin/enquiries")
    public ResponseEntity<Page<EnquiryResponse>> getAllEnquiries(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getName(); // confirms ADMIN role via SecurityConfig
        Page<EnquiryResponse> result = enquiryService.getAllEnquiries(search, page, size);
        return ResponseEntity.ok(result);
    }

    // ─── Admin: get single enquiry ────────────────────────────────────────────

    @GetMapping("/admin/enquiries/{id}")
    public ResponseEntity<EnquiryResponse> getEnquiryById(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getName();
        return ResponseEntity.ok(enquiryService.getEnquiryById(id));
    }

    // ─── Admin: update status ─────────────────────────────────────────────────

    @PutMapping("/admin/enquiries/{id}/status")
    public ResponseEntity<EnquiryResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getName();
        return ResponseEntity.ok(enquiryService.updateStatus(id, status));
    }
}
