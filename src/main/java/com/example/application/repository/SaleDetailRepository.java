/**
 * author @bhupendrasambare
 * Date   :18/01/25
 * Time   :12:05 pm
 * Project:shopping-bucket
 **/
package com.example.application.repository;

import com.example.application.entity.SaleDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
