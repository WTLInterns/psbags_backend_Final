package com.psbags.PSBags.Service;

import com.psbags.PSBags.DTO.requests.EnquiryRequest;
import com.psbags.PSBags.DTO.response.EnquiryResponse;
import com.psbags.PSBags.Exception.CustomException;
import com.psbags.PSBags.Model.Enquiry;
import com.psbags.PSBags.Repo.EnquiryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnquiryService {

    private static final List<String> VALID_STATUSES =
            List.of("NEW", "CONTACTED", "FOLLOW UP", "QUOTATION SENT", "CLOSED");

    @Autowired
    private EnquiryRepo enquiryRepo;

    public EnquiryResponse submitEnquiry(EnquiryRequest request) {
        Enquiry enquiry = new Enquiry();
        enquiry.setFullName(request.getFullName().trim());
        enquiry.setMobile(request.getMobile().trim());
        enquiry.setCompanyName(request.getCompanyName().trim());
        enquiry.setProductRequirement(request.getProductRequirement().trim());
        enquiry.setLocation(request.getLocation().trim());
        enquiry.setProductType(request.getProductType().trim());
        enquiry.setProductCount(request.getProductCount().trim());
        enquiry.setStatus("NEW");

        Enquiry saved = enquiryRepo.save(enquiry);

        EnquiryResponse response = mapToResponse(saved);
        response.setSuccess(true);
        response.setMessage("Enquiry submitted successfully.");
        return response;
    }

    public Page<EnquiryResponse> getAllEnquiries(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Enquiry> enquiries = enquiryRepo.searchEnquiries(
                (search != null && !search.isBlank()) ? search.trim() : "",
                pageable
        );
        return enquiries.map(this::mapToResponse);
    }

    public EnquiryResponse getEnquiryById(Long id) {
        Enquiry enquiry = enquiryRepo.findById(id)
                .orElseThrow(() -> new CustomException("Enquiry not found with id: " + id));
        return mapToResponse(enquiry);
    }

    public EnquiryResponse updateStatus(Long id, String newStatus) {
        if (!VALID_STATUSES.contains(newStatus.trim())) {
            throw new CustomException("Invalid status. Allowed: " + String.join(", ", VALID_STATUSES));
        }
        Enquiry enquiry = enquiryRepo.findById(id)
                .orElseThrow(() -> new CustomException("Enquiry not found with id: " + id));
        enquiry.setStatus(newStatus.trim());
        return mapToResponse(enquiryRepo.save(enquiry));
    }

    private EnquiryResponse mapToResponse(Enquiry e) {
        EnquiryResponse r = new EnquiryResponse();
        r.setId(e.getId());
        r.setFullName(e.getFullName());
        r.setMobile(e.getMobile());
        r.setCompanyName(e.getCompanyName());
        r.setProductRequirement(e.getProductRequirement());
        r.setLocation(e.getLocation());
        r.setProductType(e.getProductType());
        r.setProductCount(e.getProductCount());
        r.setStatus(e.getStatus());
        r.setCreatedAt(e.getCreatedAt());
        r.setUpdatedAt(e.getUpdatedAt());
        return r;
    }
}
