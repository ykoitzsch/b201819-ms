package com.jhipster.bachelor.orders.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.orders.domain.Basket;
import com.jhipster.bachelor.orders.service.BasketService;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Basket.
 */
@RestController
@RequestMapping("/api")
public class BasketResource {

  private final Logger log = LoggerFactory.getLogger(BasketResource.class);

  private BasketService basketService;

  public BasketResource(BasketService basketService) {
    this.basketService = basketService;
  }

  @GetMapping("/baskets")
  @Timed
  public List<Basket> getAllBaskets() {
    log.debug("REST request to get all Baskets");
    return basketService.aggregateBasketEvents();
  }

  @GetMapping("/baskets/{id}")
  @Timed
  public ResponseEntity<Basket> getBasket(@PathVariable Long id) {
    log.debug("REST request to get Basket : {}", id);
    return ResponseUtil.wrapOrNotFound(getAllBaskets().stream().filter(c -> c.getId().equals(id)).findFirst());

  }

}
