/**
 * author @bhupendrasambare
 * Date   :18/01/25
 * Time   :12:01 pm
 * Project:shopping-bucket
 **/
package com.example.application.repository;

import com.example.application.entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Items, Integer> {
}
