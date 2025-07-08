package com.shoponline.ecommerce.dtos.user;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private String email;
    private String phoneNumber;
}
