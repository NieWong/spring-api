package com.shoponline.ecommerce.dtos.user;

import lombok.Data;

@Data
public class ChangePasswordRequest {
     private String oldPassword;
     private String newPassword;
}
