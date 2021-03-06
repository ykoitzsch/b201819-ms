package com.jhipster.bachelor.orders.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhipster.bachelor.orders.domain.ProductOrder;

import event.productOrder.ProductOrderEvent;

/**
 * Service Implementation for managing ProductOrder.
 */
@Service
@Transactional
public class ProductOrderService {

  private final Logger log = LoggerFactory.getLogger(ProductOrderService.class);

  public ProductOrderService() {
  }

  public void addProductOrderEvent(ProductOrderEvent productOrderEvent) {
    //    if (productOrderEvent.getProductOrder().getCustomerId() != null) {
    //      if (productOrderEvent.getEvent().equals("PRODUCT_ORDER_CREATED")) {
    //        long basketId = productOrderEvent.getProductOrder().getCustomerId();
    //        Basket basket = basketService.aggregateBasketEvents().stream().filter(b -> b.getId().equals(basketId)).findFirst().get();
    //        basket.addProductOrder(productOrderEvent.getProductOrder());
    //        basketService.addBasketEvent(new BasketEvent(basket, "BASKET_UPDATED"));
    //      }
    //      if (productOrderEvent.getEvent().equals("PRODUCT_ORDER_DELETED")) {
    //        long basketId = productOrderEvent.getProductOrder().getCustomerId();
    //        Basket basket = basketService.aggregateBasketEvents().stream().filter(b -> b.getId().equals(basketId)).findFirst().get();
    //        basket.removeProductOrder(productOrderEvent.getProductOrder());
    //        basketService.addBasketEvent(new BasketEvent(basket, "BASKET_UPDATED"));
    //      }
    //      if (productOrderEvent.getEvent().equals("PRODUCT_ORDER_UPDATED")) {
    //        long basketId = productOrderEvent.getProductOrder().getCustomerId();
    //        Basket basket = basketService.aggregateBasketEvents().stream().filter(b -> b.getId().equals(basketId)).findFirst().get();
    //        basket.getProductOrders().remove(productOrderEvent.getProductOrder())
    //        }
    //        basketService.addBasketEvent(new BasketEvent(basket, "BASKET_UPDATED"));
    //      }

    event.productOrder.EventProducer eventProducer = new event.productOrder.EventProducer();
    eventProducer.send(productOrderEvent);
  }

  public List<ProductOrder> aggregateProductOrderEvents() {
    event.productOrder.EventConsumer eventConsumer = new event.productOrder.EventConsumer();
    log.info("~aggregateProductOrderEvents");
    List<ProductOrder> productOrderList = new ArrayList<>();
    List<ProductOrderEvent> productOrderEvents = eventConsumer.consume();
    productOrderEvents.forEach(event -> {
      if (event.getEvent().equals("PRODUCT_ORDER_CREATED")) {
        productOrderList.add(event.getProductOrder());
      }
      if (event.getEvent().equals("PRODUCT_ORDER_DELETED")) {
        productOrderList.remove(event.getProductOrder());
      }
      if (event.getEvent().equals("PRODUCT_ORDER_UPDATED")) {
        for (int i = 0; i < productOrderList.size(); i++ ) {
          if (productOrderList.get(i).getId().equals(event.getProductOrder().getId())) {
            productOrderList.set(i, event.getProductOrder());
          }
        }
      }
    });

    return productOrderList;
  }
}
