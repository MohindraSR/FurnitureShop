package com.fs.main.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @NotBlank
    @Column(unique = true)
    private String userName;

    @NotBlank
    private String password;

    private String name;

    @Email
    private String email;

    private String address;

    public Customer(String userName, String password, String name, String email, String address) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.email = email;
        this.address = address;
    }
}
