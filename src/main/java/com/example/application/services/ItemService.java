/**
 * author @bhupendrasambare
 * Date   :18/01/25
 * Time   :12:01 pm
 * Project:shopping-bucket
 **/
package com.example.application.services;

import com.example.application.dto.response.PaginationData;
import com.example.application.dto.response.Response;
import com.example.application.entity.Items;
import com.example.application.repository.ItemRepository;
import com.example.application.repository.SaleDetailRepository;
import com.example.application.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SaleDetailRepository saleDetailRepository;

    // Get item by ID
    public ResponseEntity<Response> getItemById(Integer id) {
        Optional<Items> itemOptional = itemRepository.findById(id);
        if (itemOptional.isPresent()) {
            Items item = itemOptional.get();
            return ResponseEntity.ok(new Response(Constants.OPERATION_SUCCESS, item));
        } else {
            return new ResponseEntity<>(new Response(Constants.Status.FAIL,
                    Constants.ITEM_NOT_FOUND_CODE,
                    Constants.ITEM_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    // Create a new item
    public ResponseEntity<Response> createItem(Items item) {
        item.setCreatedDate(LocalDateTime.now());
        item.setUpdateDate(LocalDateTime.now());
        Items savedItem = itemRepository.save(item);
        return ResponseEntity.ok(new Response(Constants.ITEM_CREATED_SUCCESS, savedItem));
    }

    // Update item with partial update
    public ResponseEntity<Response> updateItem(Integer id, Items updatedItem) {
        Optional<Items> itemOptional = itemRepository.findById(id);
        if (itemOptional.isPresent()) {
            Items existingItem = itemOptional.get();

            // Update fields only if provided in the request
            if (updatedItem.getItemName() != null && !updatedItem.getItemName().isEmpty()) {
                existingItem.setItemName(updatedItem.getItemName());
            }
            if (updatedItem.getItemQty() != null) {
                existingItem.setItemQty(updatedItem.getItemQty());
            }

            // Update the updateDate field
            existingItem.setUpdateDate(LocalDateTime.now());

            Items savedItem = itemRepository.save(existingItem);
            return ResponseEntity.ok(new Response(Constants.ITEM_UPDATED_SUCCESS, savedItem));
        } else {
            return new ResponseEntity<>(new Response(Constants.Status.FAIL,
                    Constants.ITEM_NOT_FOUND_CODE,
                    Constants.ITEM_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    // Get all items
    public ResponseEntity<Response> getAllItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Items> pagedItems = itemRepository.findAll(pageable);

        PaginationData<Items> paginationData = new PaginationData<>(
                pagedItems.getContent(),
                page,
                pagedItems.getTotalElements(),
                size
        );

        return ResponseEntity.ok(new Response(Constants.ITEMS_RETRIEVED_SUCCESS, paginationData));
    }

    public ResponseEntity<Response> getAllItems() {
        return ResponseEntity.ok(new Response(Constants.ITEMS_RETRIEVED_SUCCESS, itemRepository.findAll()));
    }

    // Delete item only if no SaleDetail references it
    public ResponseEntity<Response> deleteItem(Integer id) {
        if (itemRepository.existsById(id)) {
            if (saleDetailRepository.existsByItemsId(id)) {
                return new ResponseEntity<>(new Response(
                        Constants.Status.FAIL,
                        Constants.ITEM_CANNOT_DELETE_CODE,
                        Constants.ITEM_CANNOT_DELETE), HttpStatus.CONFLICT);
            }
            itemRepository.deleteById(id);
            return ResponseEntity.ok(new Response(Constants.ITEM_DELETED_SUCCESS));
        } else {
            return new ResponseEntity<>(new Response(Constants.Status.FAIL,
                    Constants.ITEM_NOT_FOUND_CODE,
                    Constants.ITEM_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }
}
