package com.jhipster.bachelor.orders.web.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.orders.domain.CompleteOrder;
import com.jhipster.bachelor.orders.security.SecurityUtils;
import com.jhipster.bachelor.orders.service.CompleteOrderService;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing CompleteOrder.
 */
@RestController
@RequestMapping("/api")
public class CompleteOrderResource {

  private final Logger log = LoggerFactory.getLogger(CompleteOrderResource.class);

  private CompleteOrderService completeOrderService;

  public CompleteOrderResource(CompleteOrderService completeOrderService) {
    this.completeOrderService = completeOrderService;
  }

  /**
   * GET /complete-orders : get all the completeOrders.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of completeOrders in body
   */
  @GetMapping("/complete-orders")
  @Timed
  public List<CompleteOrder> getAllCompleteOrders() {
    return completeOrderService.aggregateCompleteOrderEvents();
  }

  @GetMapping("/my-orders")
  @Timed
  public ResponseEntity<List<CompleteOrder>> getCompleteOrdersByCustomerId(
      @RequestParam(value = "customerId", required = true) String customerId,
      @RequestParam(value = "login", required = true) String login) {
    if ( !login.equals(SecurityUtils.getCurrentUserLogin().get())) {
      return ResponseEntity.status(401).build();
    }
    return ResponseEntity
      .ok()
      .body(
        completeOrderService
          .aggregateCompleteOrderEvents()
          .stream()
          .filter(c -> String.valueOf(c.getCustomerId()).equals(customerId))
          .collect(Collectors.toList()));
  }

  /**
   * GET /complete-orders/:id : get the "id" completeOrder.
   *
   * @param id the id of the completeOrder to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the completeOrder, or with status 404 (Not Found)
   */
  @GetMapping("/complete-orders/{id}")
  @Timed
  public ResponseEntity<CompleteOrder> getCompleteOrder(@PathVariable Long id) {
    log.debug("REST request to get CompleteOrder : {}", id);
    return ResponseUtil.wrapOrNotFound(getAllCompleteOrders().stream().filter(c -> c.getId().equals(id)).findFirst());
  }
}
