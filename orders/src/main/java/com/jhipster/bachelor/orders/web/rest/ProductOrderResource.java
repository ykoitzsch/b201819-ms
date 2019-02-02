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
import com.jhipster.bachelor.orders.domain.ProductOrder;
import com.jhipster.bachelor.orders.service.ProductOrderService;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing ProductOrder.
 */
@RestController
@RequestMapping("/api")
public class ProductOrderResource {

  private final Logger log = LoggerFactory.getLogger(ProductOrderResource.class);

  private ProductOrderService productOrderService;

  public ProductOrderResource(ProductOrderService productOrderService) {
    this.productOrderService = productOrderService;
  }

  @GetMapping("/product-orders")
  @Timed
  public List<ProductOrder> getAllProductOrders() {
    log.debug("REST request to get all ProductOrders");
    return productOrderService.aggregateProductOrderEvents();
  }

  @GetMapping("/product-orders/{id}")
  @Timed
  public ResponseEntity<ProductOrder> getProductOrder(@PathVariable Long id) {
    log.debug("REST request to get ProductOrder : {}", id);
    return ResponseUtil.wrapOrNotFound(getAllProductOrders().stream().filter(c -> c.getId().equals(id)).findFirst());

  }

}
