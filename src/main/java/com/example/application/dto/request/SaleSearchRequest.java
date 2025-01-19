/**
 * author @bhupendrasambare
 * Date   :18/01/25
 * Time   :1:25 pm
 * Project:shopping-bucket
 **/
package com.example.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleSearchRequest {
    private Integer itemId;
    private String itemName;
    private String customerName;
    private String phone;
    private Double startAmount;
    private Double emdAmount;
    private int page;
    private int size;
}
