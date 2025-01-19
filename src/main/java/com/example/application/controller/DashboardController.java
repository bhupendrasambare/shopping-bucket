/**
 * author @bhupendrasambare
 * Date   :19/01/25
 * Time   :6:49 pm
 * Project:shopping-bucket
 **/
package com.example.application.controller;

import com.example.application.dto.dto.SalesData;
import com.example.application.services.DashboardService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // API 1: Get Itemwise and Customerwise Payment Data
    @GetMapping("/itemwise-customerwise-payments")
    public ResponseEntity<List<SalesData>> getItemwiseCustomerwisePayments() {

        List<SalesData> salesData = dashboardService.getItemwiseCustomerwisePayments( );
        return ResponseEntity.ok(salesData);
    }

    // API 2: Get Top 5 Customers Based on Payment Collected
    @GetMapping("/top-customers-payment")
    public ResponseEntity<List<Object[]>> getTop5CustomersByPayment() {
        List<Object[]> topCustomers = dashboardService.getTop5CustomersByPayment();
        return ResponseEntity.ok(topCustomers);
    }

    // API 3: Get Top 10 Customers Based on Number of Purchases
    @GetMapping("/top-customers-shopping")
    public ResponseEntity<List<Object[]>> getTop10CustomersByShoppingFrequency() {
        List<Object[]> topCustomers = dashboardService.getTop10CustomersByShoppingFrequency();
        return ResponseEntity.ok(topCustomers);
    }
}
