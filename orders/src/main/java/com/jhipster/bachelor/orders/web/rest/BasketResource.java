package com.jhipster.bachelor.orders.web.rest;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.orders.domain.Basket;
import com.jhipster.bachelor.orders.domain.ProductOrder;
import com.jhipster.bachelor.orders.service.BasketService;
import com.jhipster.bachelor.orders.service.ProductOrderService;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Basket.
 */
@RestController
@RequestMapping("/api")
public class BasketResource {

  private final Logger log = LoggerFactory.getLogger(BasketResource.class);

  private BasketService basketService;

  private ProductOrderService productOrderService;

  public BasketResource(BasketService basketService, ProductOrderService productOrderService) {
    this.basketService = basketService;
    this.productOrderService = productOrderService;
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
    Basket basket = getAllBaskets().stream().filter(c -> c.getId().equals(id)).findFirst().get();
    Set<ProductOrder> pos = productOrderService
      .aggregateProductOrderEvents()
      .stream()
      .filter(po -> po.getCustomerId() != null)
      .filter(po -> po.getCustomerId().equals(basket.getId()))
      .filter(po -> po.getCompleteOrder() == null)
      .collect(Collectors.toSet());

    basket.getProductOrders().clear();
    basket.setProductOrders(pos);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(basket));
  }

}
