package com.jhipster.bachelor.orders.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jhipster.bachelor.orders.domain.Basket;

import event.ConsumerChannel;
import event.basket.BasketEvent;

/**
 * Service Implementation for managing Basket.
 */
@Service
@Transactional
public class BasketService {

  private final Logger log = LoggerFactory.getLogger(BasketService.class);

  private ProductOrderService productOrderService;

  public BasketService(ProductOrderService productOrderService) {
    this.productOrderService = productOrderService;
  }

  //
  //  public Basket save(Basket basket) {
  //    log.debug("Request to save Basket : {}", basket);
  //    Optional<Basket> optTempBasket = findOne(basket.getId());
  //    if (optTempBasket.isPresent()) {
  //      Basket tempBasket = optTempBasket.get();
  //      log.info(tempBasket.getProductOrders().size() + " vs " + basket.getProductOrders().size());
  //      if (tempBasket.getProductOrders().size() < basket.getProductOrders().size()) { //adding new ProductOrders
  //        Set<ProductOrder> tempOrders = new HashSet<>();
  //        if (basket.getProductOrders() != null) {
  //          for (ProductOrder pO : basket.getProductOrders()) { //clone
  //            if (pO.getBasket() == null)
  //              tempOrders.add(pO);
  //          }
  //          tempOrders.forEach(p -> {
  //            p.setBasket(basket);
  //            basket.getProductOrders().add(productOrderRepository.save(p));
  //          });
  //        }
  //      } else if (tempBasket.getProductOrders().size() > basket.getProductOrders().size()) { //removing ProductOrders
  //        log.info("~removing productOrders!");
  //        for (ProductOrder p : tempBasket.getProductOrders()) { //iterate over all exising productOrders in db and check which ones are missing 
  //          if ( !basket.getProductOrders().contains(p)) {
  //            log.info("~delete " + p.toString());
  //            //tempBasket.removeProductOrder(p);
  //            productOrderRepository.delete(p);
  //          }
  //        }
  //      }
  //    }
  //    return basketRepository.save(basket);
  //  }

  public void addBasketEvent(BasketEvent basketEvent) {
    event.basket.EventProducer eventProducer = new event.basket.EventProducer();
    eventProducer.send(basketEvent);
  }

  public List<Basket> aggregateBasketEvents() {
    event.basket.EventConsumer eventConsumer = new event.basket.EventConsumer();
    log.info("~aggregateBasketEvents");
    List<Basket> basketList = new ArrayList<>();
    List<BasketEvent> basketEvents = eventConsumer.consume();
    basketEvents.forEach(event -> {
      if (event.getEvent().equals("BASKET_CREATED")) {
        basketList.add(event.getBasket());
      }
      if (event.getEvent().equals("BASKET_DELETED")) {
        basketList.remove(event.getBasket());
      }
      if (event.getEvent().equals("BASKET_UPDATED")) {
        for (int i = 0; i < basketList.size(); i++ ) {
          if (basketList.get(i).getId().equals(event.getBasket().getId())) {
            basketList.set(i, event.getBasket());
          }
        }
      }
    });

    return basketList;
  }

  @StreamListener(ConsumerChannel.INPUT)
  public void something(@Payload String payload) {
    JsonObject obj = new JsonParser().parse(payload).getAsJsonObject();
    if ("CUSTOMER_CREATED".equals(obj.get("event").getAsString())) {
      Basket basket = new Basket();
      basket.setId(obj.getAsJsonObject("customer").get("id").getAsLong());
      basket.setCustomerId(obj.getAsJsonObject("customer").get("id").getAsLong());
      addBasketEvent(new BasketEvent(basket, "BASKET_CREATED"));
    }
  }
}
