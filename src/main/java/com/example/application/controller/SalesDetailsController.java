/**
 * author @bhupendrasambare
 * Date   :18/01/25
 * Time   :1:17 pm
 * Project:shopping-bucket
 **/
package com.example.application.controller;

import com.example.application.dto.request.SaleSearchRequest;
import com.example.application.dto.request.SalesDetailsRequest;
import com.example.application.dto.response.Response;
import com.example.application.services.SaleDetailService;
import com.example.application.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/sales-details")
@Tag(name = "Sales details Controller", description = "APIs for managing Sales details")
public class SalesDetailsController {

    @Autowired
    private SaleDetailService saleDetailService;

    @PostMapping
    @Operation(summary = "Create Sale Entry", description = "Create a new sale entry in the sales table")
    @ApiResponse(responseCode = "200", description = "Sale entry created successfully")
    public ResponseEntity<Response> createSale(@RequestBody @Valid SalesDetailsRequest saleDetail) {
        try {
            return saleDetailService.createSale(saleDetail);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(
                    Constants.Status.FAIL,
                    Constants.INTERNAL_SERVER_ERROR_CODE,
                    Constants.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel Sale Entry", description = "Cancel a sale entry by its ID")
    @ApiResponse(responseCode = "200", description = "Sale entry cancelled successfully")
    public ResponseEntity<Response> cancelSale(@PathVariable Integer id) {
        try {
            return saleDetailService.cancelSale(id);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(
                    Constants.Status.FAIL,
                    Constants.INTERNAL_SERVER_ERROR_CODE,
                    Constants.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Sale Entry", description = "Update an existing sale entry by its ID")
    @ApiResponse(responseCode = "200", description = "Sale entry updated successfully")
    public ResponseEntity<Response> updateSale(@PathVariable Integer id, @RequestBody SalesDetailsRequest updatedSale) {
        try {
            return saleDetailService.updateSale(id, updatedSale);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(
                    Constants.Status.FAIL,
                    Constants.INTERNAL_SERVER_ERROR_CODE,
                    Constants.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/item")
    @Operation(summary = "Get Sale Details by Item ID", description = "Retrieve sale details by item ID")
    @ApiResponse(responseCode = "200", description = "Sale details retrieved successfully")
    public ResponseEntity<Response> getSalesByItemId(@RequestBody SaleSearchRequest saleSearchRequest) {
        try {
            return saleDetailService.getSalesByItemId(saleSearchRequest);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(
                    Constants.Status.FAIL,
                    Constants.INTERNAL_SERVER_ERROR_CODE,
                    Constants.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/search")
    @Operation(summary = "Search Sale Details", description = "Search for sales by item name, customer name, mobile number, or amount")
    @ApiResponse(responseCode = "200", description = "Sale details retrieved successfully")
    public ResponseEntity<Response> searchSales(@RequestBody SaleSearchRequest saleSearchRequest) {
        try {
            return saleDetailService.searchSales(saleSearchRequest);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(
                    Constants.Status.FAIL,
                    Constants.INTERNAL_SERVER_ERROR_CODE,
                    Constants.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
