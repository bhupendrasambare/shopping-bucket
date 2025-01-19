/**
 * author @bhupendrasambare
 * Date   :19/01/25
 * Time   :11:26 am
 * Project:shopping-bucket
 **/
package com.example.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesDetailsRequest {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @NotNull(message = "Email cannot be null")
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian phone number")
    @NotBlank(message = "Phone number is required")
    @NotNull(message = "Phone number cannot be null")
    private String phone;

    @NotBlank(message = "Customer name is required")
    @NotNull(message = "Customer name cannot be null")
    @Size(max = 100, message = "Customer name must not exceed 100 characters")
    private String customerName;

    @NotBlank(message = "Address is required")
    @NotNull(message = "Address cannot be null")
    private String address;

    @NotBlank(message = "State is required")
    @NotNull(message = "State cannot be null")
    private String state;

    @NotNull(message = "Item ID is required")
    @Min(value = 1, message = "Item ID must be greater than 0")
    private Integer itemId;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth cannot be null")
    private Date dateOfBirth;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number")
    private Double price;
}
