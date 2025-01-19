/**
 * author @bhupendrasambare
 * Date   :18/01/25
 * Time   :11:37 am
 * Project:shopping-bucket
 **/
package com.example.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sales_details")
public class SaleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "customer_name")
    private String customerName;

    private String address;

    private String state;

    private Boolean minor;

    @Column(name = "shop_date")
    private LocalDateTime shopDate;

    @ManyToOne
    @JoinColumn(name = "items_id")
    private Items items;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    private Integer quantity;

    private Double price;

    @Column(name = "pay_amount")
    private Double payAmount;

}
