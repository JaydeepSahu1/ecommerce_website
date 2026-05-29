package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne   // ✅ Correct
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    private Set<Product> products = new HashSet<>();

}
