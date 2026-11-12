package com.hungdev.modal;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDateTime;

@Embeddable
@Data
public class PaymentDetails {
    private String paymentMethod;
    private String paymentId;
    private String transactionId;
    private LocalDateTime paymentDate;
}
