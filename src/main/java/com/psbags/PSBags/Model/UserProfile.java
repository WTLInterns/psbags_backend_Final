package com.psbags.PSBags.Model;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    private String preferredLanguage;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    @ToString.Exclude  // Prevent circular reference in toString()
    private User user;

}
