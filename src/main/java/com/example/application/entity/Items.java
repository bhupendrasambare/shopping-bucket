/**
 * author @bhupendrasambare
 * Date   :18/01/25
 * Time   :11:34 am
 * Project:shopping-bucket
 **/
package com.example.application.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_name")
    @NotNull(message = "item name is required")
    @NotBlank(message = "item name is required")
    private  String itemName;

    @Column(name = "item_qty")
    @NotNull(message = "item quantity is required")
    private Integer itemQty;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updateDate;
}
