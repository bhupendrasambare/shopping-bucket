/**
 * author @bhupendrasambare
 * Date   :19/01/25
 * Time   :6:53 pm
 * Project:shopping-bucket
 **/
package com.example.application.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SalesData {
    private Object itemName;
    private Object customerName;
    private Object monthEndDate;
    private Object totalPaymentLastMonth;
    private Object currentMonthPayment;
    private Object totalPaymentCurrentMonth;

    // Constructor, Getters, Setters
//    public SalesData(String itemName, String customerName, String monthEndDate,
//                     Double totalPaymentLastMonth, Double currentMonthPayment, Double totalPaymentCurrentMonth) {
//        this.itemName = itemName;
//        this.customerName = customerName;
//        this.monthEndDate = monthEndDate;
//        this.totalPaymentLastMonth = totalPaymentLastMonth;
//        this.currentMonthPayment = currentMonthPayment;
//        this.totalPaymentCurrentMonth = totalPaymentCurrentMonth;
//    }

    public SalesData(Object itemName, Object customerName, Object monthEndDate,
                     Object totalPaymentLastMonth, Object currentMonthPayment, Object totalPaymentCurrentMonth) {
        this.itemName = itemName;
        this.customerName = customerName;
        this.monthEndDate = monthEndDate;
        this.totalPaymentLastMonth = totalPaymentLastMonth;
        this.currentMonthPayment = currentMonthPayment;
        this.totalPaymentCurrentMonth = totalPaymentCurrentMonth;
    }
}
