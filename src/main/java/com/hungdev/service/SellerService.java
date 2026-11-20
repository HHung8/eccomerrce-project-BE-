package com.hungdev.service;

import com.hungdev.modal.Seller;

public interface SellerService {
    Seller getSellerProfile(String jwt);

    Seller createSeller(Seller seller);

    Seller getSellerById(Long id);

    Seller getSellerByEmail(String email);
}
