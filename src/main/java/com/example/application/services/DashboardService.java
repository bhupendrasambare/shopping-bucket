/**
 * author @bhupendrasambare
 * Date   :19/01/25
 * Time   :6:54 pm
 * Project:shopping-bucket
 **/
package com.example.application.services;

import com.example.application.dto.dto.SalesData;
import com.example.application.repository.SaleDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private SaleDetailRepository dashboardRepository;

    public List<SalesData> getItemwiseCustomerwisePayments() {
        return dashboardRepository.getItemwiseCustomerwisePayments();
    }

    public List<Object[]> getTop5CustomersByPayment() {
        return dashboardRepository.getTop5CustomersByPayment();
    }

    public List<Object[]> getTop10CustomersByShoppingFrequency() {
        return dashboardRepository.getTop10CustomersByShoppingFrequency();
    }
}
