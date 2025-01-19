/**
 * author @bhupendrasambare
 * Date   :18/01/25
 * Time   :12:05 pm
 * Project:shopping-bucket
 **/
package com.example.application.repository;

import com.example.application.dto.dto.SalesData;
import com.example.application.entity.SaleDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface SaleDetailRepository extends JpaRepository<SaleDetail,Integer> {
    @Query("SELECT COUNT(u) > 0 FROM SaleDetail u WHERE u.items.id = :itemId")
    boolean existsByItemsId(@Param("itemId") Integer itemId);

    @Query("SELECT s FROM SaleDetail s WHERE " +
            "(:itemName IS NULL OR s.items.itemName LIKE %:itemName%) AND " +
            "(:customerName IS NULL OR s.customerName LIKE %:customerName%) AND " +
            "(:phone IS NULL OR s.phone LIKE %:phone%) AND " +
            "(:startAmount IS NULL OR s.payAmount >= :startAmount) AND " +
            " (:endAmount IS NULL OR s.payAmount <= :endAmount) order by s.id DESC")
    Page<SaleDetail> searchSales(@Param("itemName") String itemName,
                                 @Param("customerName") String customerName,
                                 @Param("phone") String phone,
                                 @Param("startAmount") Double startAmount,
                                 @Param("endAmount") Double endAmount,
                                 Pageable pageable);

    @Query("SELECT u FROM SaleDetail u WHERE u.items.id = :itemId")
    List<SaleDetail> findByItemsId(@Param("itemId") Integer itemId);

    @Transactional
    @Modifying
    @Query("update SaleDetail s set s.email = ?1 where s.phone = ?2")
    int updateEmailByPhone(String email, String phone);

    @Transactional
    @Modifying
    @Query("update SaleDetail s set s.customerName = ?1 where s.phone = ?2")
    int updateCustomerNameByPhone(String customerName, String phone);



    @Query("SELECT new com.example.application.dto.dto.SalesData(s.items.itemName, s.customerName, " +
            "MONTH(s.shopDate) AS monthEndDate, " +
            "SUM(CASE WHEN MONTH(s.shopDate) < MONTH(CURRENT_DATE) THEN s.payAmount ELSE 0 END) AS totalPaymentLastMonth, " +
            "SUM(CASE WHEN MONTH(s.shopDate) = MONTH(CURRENT_DATE) THEN s.payAmount ELSE 0 END) AS currentMonthPayment, " +
            "SUM(s.payAmount) AS totalPaymentCurrentMonth) " +
            "FROM SaleDetail s " +
            "GROUP BY s.items.itemName, s.customerName, MONTH(s.shopDate)")
    List<SalesData> getItemwiseCustomerwisePayments();

    // Get Top 5 Customers Based on Payment Collected
    @Query("SELECT s.customerName, SUM(s.payAmount) AS totalPaymentCollected " +
            "FROM SaleDetail s " +
            "GROUP BY s.customerName ORDER BY totalPaymentCollected DESC")
    List<Object[]> getTop5CustomersByPayment();

    // Get Top 10 Customers Based on Number of Purchases
    @Query("SELECT s.customerName, COUNT(s) AS purchaseCount " +
            "FROM SaleDetail s " +
            "GROUP BY s.customerName ORDER BY purchaseCount DESC")
    List<Object[]> getTop10CustomersByShoppingFrequency();
}
