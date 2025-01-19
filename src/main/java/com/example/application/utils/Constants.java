/**
 * author @bhupendrasambare
 * Date   :18/01/25
 * Time   :11:59 am
 * Project:shopping-bucket
 **/
package com.example.application.utils;

public interface Constants {
    String SUCCESS_CODE = "200";
    String FAILURE_CODE = "500";

    public enum Status {
        SUCCESS, FAIL
    }

    String ITEM_NOT_FOUND_CODE = "404";
    String SALES_NOT_FOUND_CODE = "404";
    String INTERNAL_SERVER_ERROR_CODE = "500_INTERNAL_SERVER_ERROR";
    String ITEM_CANNOT_DELETE_CODE = "409";
    String SALE_NOT_FOUND_CODE = "404";
    String REQUESTED_QUANTITY_NOT_AVAILABLE_CODE = "404";

    String OPERATION_SUCCESS = "Operation completed successfully";
    String ITEM_NOT_FOUND = "Item not found";
    String REQUESTED_QUANTITY_NOT_AVAILABLE = "Request quantity not available";
    String ITEMS_RETRIEVED_SUCCESS = "Items retrieved successfully";
    String ITEM_CREATED_SUCCESS = "Item created successfully";
    String ITEM_UPDATED_SUCCESS = "Item updated successfully";
    String ITEM_DELETED_SUCCESS = "Item deleted successfully";

    String SALES_NOT_FOUND = "Sale details not found";
    String SALES_RETRIEVED_SUCCESS = "Sales details retrieved successfully";

    String SOMETHING_WENT_WRONG = "Something went wrong. Please try again.";
    String ITEM_CANNOT_DELETE = "Item cannot be deleted as it has associated sales data.";

    String SALE_CREATED_SUCCESS = "Sale entry created successfully.";
    String SALE_CANCELLED_SUCCESS = "Sale entry cancelled successfully.";

    String SALE_UPDATED_SUCCESS = "Sale entry updated successfully";
    String SALE_NOT_FOUND = "Sale not found";

    // Error Messages
    public static final String INVALID_EMAIL = "Invalid email address.";
    public static final String INVALID_PHONE_NUMBER = "Invalid phone number.";
    public static final String MINOR_PRICE_EXCEEDED = "Price exceeds the allowable limit for minors.";
    // Error Codes
    public static final String INVALID_EMAIL_CODE = "INVALID_EMAIL";
    public static final String INVALID_PHONE_NUMBER_CODE = "INVALID_PHONE";
    public static final String MINOR_PRICE_EXCEEDED_CODE = "MINOR_PRICE_EXCEEDED";
    // Discount Information
    public static final double MAHARASHTRA_DISCOUNT_RATE = 0.2;
    public static final double MINOR_PRICE_LIMIT = 1000.0;
}
