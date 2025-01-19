/**
 * author @bhupendrasambare
 * Date   :18/01/25
 * Time   :1:21 pm
 * Project:shopping-bucket
 **/
package com.example.application.services;

import com.example.application.dto.request.SaleSearchRequest;
import com.example.application.dto.request.SalesDetailsRequest;
import com.example.application.dto.response.PaginationData;
import com.example.application.dto.response.Response;
import com.example.application.entity.Items;
import com.example.application.entity.SaleDetail;
import com.example.application.repository.ItemRepository;
import com.example.application.repository.SaleDetailRepository;
import com.example.application.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Slf4j
public class SaleDetailService {

    @Autowired
    private SaleDetailRepository saleDetailRepository;

    @Autowired
    private ItemRepository itemRepository;

    public ResponseEntity<Response> createSale(SalesDetailsRequest request) {
        if (request.getItemId() != null && request.getItemId() != 0) {
            Items item = itemRepository.findById(request.getItemId()).orElse(null);

            if (item != null) {
                if (item.getItemQty() >= request.getQuantity()) {
                    SaleDetail detail = new SaleDetail();
                    detail.setItems(item);

                    if (!isValidEmail(request.getEmail())) {
                        return new ResponseEntity<>(new Response(Constants.Status.FAIL,
                                Constants.INVALID_EMAIL_CODE, Constants.INVALID_EMAIL), HttpStatus.BAD_REQUEST);
                    }

                    if (!isValidPhoneNumber(request.getPhone())) {
                        return new ResponseEntity<>(new Response(Constants.Status.FAIL,
                                Constants.INVALID_PHONE_NUMBER_CODE, Constants.INVALID_PHONE_NUMBER), HttpStatus.BAD_REQUEST);
                    }

                    double finalPrice = request.getPrice();
                    if ("Maharashtra".equalsIgnoreCase(request.getState())) {
                        finalPrice = finalPrice * 0.8;
                        if (Boolean.TRUE.equals(detail.getMinor()) && finalPrice > 1000) {
                            return new ResponseEntity<>(new Response(Constants.Status.FAIL,
                                    Constants.MINOR_PRICE_EXCEEDED_CODE, Constants.MINOR_PRICE_EXCEEDED), HttpStatus.BAD_REQUEST);
                        }
                    }

                    if (request.getDateOfBirth() != null) {
                        int age = calculateAge(request.getDateOfBirth());
                        if (age < 18) {
                            detail.setMinor(true);
                            if (finalPrice > 1000) {
                                return new ResponseEntity<>(new Response(Constants.Status.FAIL,
                                        Constants.MINOR_PRICE_EXCEEDED_CODE, Constants.MINOR_PRICE_EXCEEDED), HttpStatus.BAD_REQUEST);
                            }
                        } else {
                            detail.setMinor(false);
                        }
                    }

                    detail.setPrice(request.getPrice());
                    detail.setPayAmount(finalPrice);
                    detail.setCustomerName(request.getCustomerName());
                    detail.setEmail(request.getEmail());
                    detail.setPhone(request.getPhone());
                    detail.setAddress(request.getAddress());
                    detail.setState(request.getState());
                    detail.setDateOfBirth(request.getDateOfBirth());
                    detail.setQuantity(request.getQuantity());
                    detail.setShopDate(LocalDateTime.now());

                    SaleDetail savedSale = saleDetailRepository.save(detail);
                    try {
                        updateSaleCustomer(savedSale,savedSale.getPhone());
                    }catch (Exception e){
                        log.error(" while updating previous customer details");
                    }
                    item.setItemQty(item.getItemQty() - request.getQuantity());
                    itemRepository.save(item);

                    return ResponseEntity.ok(new Response(Constants.SALE_CREATED_SUCCESS, savedSale));
                } else {
                    return new ResponseEntity<>(new Response(Constants.Status.FAIL,
                            Constants.REQUESTED_QUANTITY_NOT_AVAILABLE_CODE,
                            Constants.REQUESTED_QUANTITY_NOT_AVAILABLE), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(new Response(Constants.Status.FAIL,
                        Constants.ITEM_NOT_FOUND_CODE, Constants.ITEM_NOT_FOUND), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new Response(Constants.Status.FAIL,
                    Constants.ITEM_NOT_FOUND_CODE, Constants.ITEM_NOT_FOUND), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Response> searchSales(SaleSearchRequest req) {
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize());
        Page<SaleDetail> salesPage = saleDetailRepository.searchSales(req.getItemName(),
                req.getCustomerName(),
                req.getPhone(),
                req.getStartAmount(),
                req.getEmdAmount(),
                pageable);
        PaginationData<SaleDetail> data = new PaginationData<>(salesPage.getContent(),req.getPage(),salesPage.getTotalElements(),req.getSize());
        return ResponseEntity.ok(new Response(Constants.SALES_RETRIEVED_SUCCESS, data));
    }

    // 2. Cancel Sale Entry
    public ResponseEntity<Response> cancelSale(Integer saleId) {
        SaleDetail saleOptional = saleDetailRepository.findById(saleId).orElse(null);
        if (saleOptional!=null) {
            Items items =saleOptional.getItems();
            Integer total = saleOptional.getQuantity();
            saleDetailRepository.deleteById(saleId); // Or set a 'cancelled' flag
            if(total!=null){
                items.setItemQty(items.getItemQty() + total);
                itemRepository.save(items);
            }
            return ResponseEntity.ok(new Response(Constants.SALE_CANCELLED_SUCCESS));
        } else {
            return new ResponseEntity<>(new Response(Constants.Status.FAIL,
                    Constants.SALE_NOT_FOUND_CODE,
                    Constants.SALE_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    // 3. Update Sale Entry
    public ResponseEntity<Response> updateSale(Integer saleId, SalesDetailsRequest updatedSale) {
        Optional<SaleDetail> saleOptional = saleDetailRepository.findById(saleId);
        if (saleOptional.isPresent()) {
            SaleDetail existingSale = saleOptional.get();
            boolean customerUpdated = false;

            // Validate and update Customer Name
            if (updatedSale.getCustomerName() != null && !updatedSale.getCustomerName().isEmpty()) {
                existingSale.setCustomerName(updatedSale.getCustomerName());
                customerUpdated = true;
            }

            // Validate and update Phone Number
            if (updatedSale.getPhone() != null && isValidPhoneNumber(updatedSale.getPhone())) {
                existingSale.setPhone(updatedSale.getPhone());
            }

            // Validate and update Price
            if (updatedSale.getPrice() != null && updatedSale.getPrice() > 0) {
                existingSale.setPrice(updatedSale.getPrice());

                // Recalculate Pay Amount based on state
                double finalPrice = updatedSale.getPrice();
                if ("Maharashtra".equalsIgnoreCase(existingSale.getState())) {
                    finalPrice = finalPrice * 0.8;
                    if (Boolean.TRUE.equals(existingSale.getMinor()) && finalPrice > 1000) {
                        return new ResponseEntity<>(new Response(Constants.Status.FAIL,
                                Constants.MINOR_PRICE_EXCEEDED_CODE, Constants.MINOR_PRICE_EXCEEDED), HttpStatus.BAD_REQUEST);
                    }
                }
                existingSale.setPayAmount(finalPrice);
            }

            // Update Item Quantity
            if (updatedSale.getQuantity() != null && updatedSale.getQuantity() > 0) {
                Items item = existingSale.getItems();
                if (item != null) {
                    int quantityDifference = updatedSale.getQuantity() - existingSale.getQuantity();
                    if (item.getItemQty() >= quantityDifference) {
                        item.setItemQty(item.getItemQty() - quantityDifference);
                        existingSale.setQuantity(updatedSale.getQuantity());
                        itemRepository.save(item);
                    } else {
                        return new ResponseEntity<>(new Response(Constants.Status.FAIL,
                                Constants.REQUESTED_QUANTITY_NOT_AVAILABLE_CODE,
                                Constants.REQUESTED_QUANTITY_NOT_AVAILABLE), HttpStatus.BAD_REQUEST);
                    }
                }
            }

            // If Name or Email is updated, update the customer details
            if (customerUpdated || (updatedSale.getEmail() != null && isValidEmail(updatedSale.getEmail()))) {
                try {
                    updateSaleCustomer(existingSale, existingSale.getPhone());
                } catch (Exception e) {
                    log.error("Error while updating customer details: {}", e.getMessage());
                }
            }

            // Save updated sale details
            SaleDetail savedSale = saleDetailRepository.save(existingSale);
            return ResponseEntity.ok(new Response(Constants.SALE_UPDATED_SUCCESS, savedSale));

        } else {
            // Sale not found
            return new ResponseEntity<>(new Response(Constants.Status.FAIL,
                    Constants.SALE_NOT_FOUND_CODE,
                    Constants.SALE_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    // 4. Get Sale Details by Item ID
    public ResponseEntity<Response> getSalesByItemId(SaleSearchRequest saleSearchRequest) {
        List<SaleDetail> sales = saleDetailRepository.findByItemsId(saleSearchRequest.getItemId());
        if (!sales.isEmpty()) {
            return ResponseEntity.ok(new Response(Constants.SALES_RETRIEVED_SUCCESS, sales));
        } else {
            return new ResponseEntity<>(new Response(Constants.Status.FAIL,
                    Constants.SALES_NOT_FOUND_CODE,
                    Constants.SALES_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }

    private boolean isValidPhoneNumber(String phone) {
        String phoneRegex = "^[6-9]\\d{9}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        return phone != null && pattern.matcher(phone).matches();
    }

    private int calculateAge(Date birthDate) {
        if (birthDate == null) {
            return 0;
        }
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthDate);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    private void updateSaleCustomer(SaleDetail savedSale, String phone) {
        saleDetailRepository.updateEmailByPhone(savedSale.getEmail(),phone);
        saleDetailRepository.updateCustomerNameByPhone(savedSale.getCustomerName(),phone);
    }
}

