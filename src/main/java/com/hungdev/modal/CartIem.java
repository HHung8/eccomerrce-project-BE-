package com.hungdev.modal;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CartIem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @ManyToOne
    @JsonIgnore
    private Cart cart;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    private String size;
    private int quantity = 1;
    private Integer mrpPrice;
    private Integer sellingPrice;
    private Long userId;
}
