package com.example.ormTester;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "BANK_ACCOUNT_DETAILS", schema = "LUCIFER")
@Data
public class BankAccountDetails {
    @Id
    @Column(name = "ACCOUNT_ID", length = 20)
    private String accountId;

    @Column(name = "BRANCH_NAME", length = 100)
    private String branchName;

    @Column(name = "ACCOUNT_BALANCE", precision = 15, scale = 2)
    private BigDecimal accountBalance;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;
}
