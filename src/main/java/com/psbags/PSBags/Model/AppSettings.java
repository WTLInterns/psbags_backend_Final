package com.psbags.PSBags.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_settings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppSettings {

    @Id
    private Integer id = 1; // single-row table

    private Double gstPercentage;

    @Column(name = "business_name", length = 200)
    private String businessName;

    @Column(name = "business_email", length = 200)
    private String businessEmail;

    @Column(name = "business_mobile", length = 20)
    private String businessMobile;

    @Column(name = "business_whatsapp", length = 20)
    private String businessWhatsapp;
}
