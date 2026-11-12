package com.hungdev.modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hungdev.domain.AccountStatus;
import com.hungdev.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sellerName;
    private String mobile;

    @Column(unique = true, nullable = false)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();

    @Embedded
    private BankDetails bankDetails = new BankDetails();

    @OneToOne(cascade = CascadeType.ALL)
    private Address pickupAddress = new Address();

    private String GSTIN;

    private USER_ROLE role = USER_ROLE.ROLE_SELLER;

    private boolean isEmailVerified = false;

    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;

}
