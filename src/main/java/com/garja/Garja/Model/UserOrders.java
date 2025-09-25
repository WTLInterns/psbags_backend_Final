package com.garja.Garja.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrders {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;



    private String orderDate;
    private double totalAmount;
    private String status;
    private String productName;
    private int quantity;
    private String size;
    private String image;
    private String paymentStatus;
    private String paymentType;
    private Long shiprocketOrderId;
    private Long shipmentId;
    private Long awbCode;
    private String trackingUrl;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

     @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    

}
