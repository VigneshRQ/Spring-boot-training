package com.example.ormTester;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "BANK_ADDRESS", schema = "LUCIFER")
public class Address {
    @Id
    @Column(name = "POSTAL_CODE", length = 100)
    private String pincode;

    @Column(name = "CITY", length = 100)
    private String city;

    @Column(name = "STATE", length = 100)
    private String state;

    // If you want bidirectional relationship (optional)
    @OneToMany(mappedBy = "address")  // ✅ This should be on a COLLECTION
    private List<BankAccountDetails> bankAccounts = new ArrayList<>();
}
