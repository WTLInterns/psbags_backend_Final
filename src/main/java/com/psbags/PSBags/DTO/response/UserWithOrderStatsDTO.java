package com.psbags.PSBags.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithOrderStatsDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String phoneNumber;
    private long pendingCount;
    private long processingCount;
    private long shippedCount;
    private long deliveredCount;
    private long cancelledCount;
}

