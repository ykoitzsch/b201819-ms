package com.jhipster.bachelor.orders.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.orders.domain.Basket;
import com.jhipster.bachelor.orders.domain.CompleteOrder;
import com.jhipster.bachelor.orders.domain.ProductOrder;
import com.jhipster.bachelor.orders.service.BasketService;
import com.jhipster.bachelor.orders.service.CompleteOrderService;
import com.jhipster.bachelor.orders.service.ProductOrderService;

import event.basket.BasketEvent;
import event.completeOrder.CompleteOrderEvent;
import event.productOrder.ProductOrderEvent;

@RestController
@RequestMapping("/api")
public class EventResource {

  private BasketService basketService;

  private CompleteOrderService completeOrderService;

  private ProductOrderService productOrderService;

  public EventResource(BasketService basketService, CompleteOrderService completeOrderService, ProductOrderService productOrderService) {
    this.productOrderService = productOrderService;
    this.completeOrderService = completeOrderService;
    this.basketService = basketService;
  }

  @PostMapping("/productOrder-events/{event}")
  @Timed
  public ResponseEntity<Object> addCustomerEvent(@RequestBody ProductOrder productOrder, @PathVariable("event") String event)
      throws Exception {
    if ("PRODUCT_ORDER_DELETED".equals(event) || "PRODUCT_ORDER_UPDATED".equals(event) || "PRODUCT_ORDER_CREATED".equals(event)) {
      productOrderService.addProductOrderEvent(new ProductOrderEvent(productOrder, event));
    } else
      throw new Exception("Unknown Event");
    return new ResponseEntity<>(HttpStatus.OK);

  }

  @PostMapping("/completeOrder-events/{event}")
  @Timed
  public ResponseEntity<Object> addCustomerEvent(@RequestBody CompleteOrder completeOrder, @PathVariable("event") String event)
      throws Exception {
    if ("COMPLETE_ORDER_DELETED".equals(event) || "COMPLETE_ORDER_UPDATED".equals(event) || "COMPLETE_ORDER_CREATED".equals(event)) {
      completeOrderService.addCompleteOrderEvent(new CompleteOrderEvent(completeOrder, event));
    } else
      throw new Exception("Unknown Event");
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/basket-events/{event}")
  @Timed
  public ResponseEntity<Object> addCustomerEvent(@RequestBody Basket basket, @PathVariable("event") String event) throws Exception {
    if ("BASKET_DELETED".equals(event) || "BASKET_UPDATED".equals(event) || "BASKET_CREATED".equals(event)) {
      basketService.addBasketEvent(new BasketEvent(basket, event));
    } else
      throw new Exception("Unknown Event");
    return new ResponseEntity<>(HttpStatus.OK);

  }

}
