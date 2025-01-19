/**
 * author @bhupendrasambare
 * Date   :18/01/25
 * Time   :11:58 am
 * Project:shopping-bucket
 **/
package com.example.application.controller;

import com.example.application.dto.response.Response;
import com.example.application.entity.Items;
import com.example.application.services.ItemService;
import com.example.application.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/items")
@Tag(name = "Items Controller", description = "APIs for managing items")
public class ItemsController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    @Operation(summary = "Create an Item", description = "Create a new item in the system")
    @ApiResponse(responseCode = "200", description = "Item created successfully")
    public ResponseEntity<Response> createItem(@RequestBody @Valid Items requestEntity) {
        try {
            return itemService.createItem(requestEntity);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(
                    Constants.Status.FAIL,
                    Constants.INTERNAL_SERVER_ERROR_CODE,
                    Constants.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an Item", description = "Update an existing item in the system")
    @ApiResponse(responseCode = "200", description = "Item updated successfully")
    public ResponseEntity<Response> updateItem(@PathVariable Integer id, @RequestBody Items requestEntity) {
        try {
            return itemService.updateItem(id, requestEntity);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(
                    Constants.Status.FAIL,
                    Constants.INTERNAL_SERVER_ERROR_CODE,
                    Constants.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @Operation(summary = "Get all Items by pagination", description = "Retrieve all items with pagination")
    @ApiResponse(responseCode = "200", description = "Items retrieved successfully")
    public ResponseEntity<Response> getAllItemsByPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            return itemService.getAllItems(page, size);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(
                    Constants.Status.FAIL,
                    Constants.INTERNAL_SERVER_ERROR_CODE,
                    Constants.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Items", description = "Retrieve all items with pagination")
    @ApiResponse(responseCode = "200", description = "Items retrieved successfully")
    public ResponseEntity<Response> getAllItems() {
        try {
            return itemService.getAllItems();
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(
                    Constants.Status.FAIL,
                    Constants.INTERNAL_SERVER_ERROR_CODE,
                    Constants.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an Item by ID", description = "Retrieve a specific item by its ID")
    @ApiResponse(responseCode = "200", description = "Item retrieved successfully")
    public ResponseEntity<Response> getItemById(@PathVariable Integer id) {
        try {
            return itemService.getItemById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(
                    Constants.Status.FAIL,
                    Constants.INTERNAL_SERVER_ERROR_CODE,
                    Constants.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an Item", description = "Delete a specific item by its ID")
    @ApiResponse(responseCode = "200", description = "Item deleted successfully")
    public ResponseEntity<Response> deleteItemById(@PathVariable Integer id) {
        try {
            return itemService.deleteItem(id);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(
                    Constants.Status.FAIL,
                    Constants.INTERNAL_SERVER_ERROR_CODE,
                    Constants.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}