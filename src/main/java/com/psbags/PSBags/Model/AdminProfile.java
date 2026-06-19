package com.psbags.PSBags.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    private String department; 

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude  // Prevent circular reference in toString()
    private User user; 
}
