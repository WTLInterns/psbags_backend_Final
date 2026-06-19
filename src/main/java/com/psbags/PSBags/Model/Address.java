package com.psbags.PSBags.Model;

import java.util.List;

import javax.annotation.processing.Generated;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String steet;

    private String city;

    private String landmark;

    private String pincode;

    private String address;

    private String state;

    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude  // Prevent circular reference in toString()
    private User user;


   @OneToMany(mappedBy = "address")
   @ToString.Exclude  // Prevent circular reference in toString()
    private List<UserOrders> userOrders;

    

    
}
