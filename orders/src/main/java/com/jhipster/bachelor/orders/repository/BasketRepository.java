package com.jhipster.bachelor.orders.repository;

import com.jhipster.bachelor.orders.domain.Basket;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Basket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

}
