package com.jhipster.bachelor.orders.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhipster.bachelor.orders.domain.CompleteOrder;

import event.completeOrder.CompleteOrderEvent;

/**
 * Service Implementation for managing CompleteOrder.
 */
@Service
@Transactional
public class CompleteOrderService {

  private final Logger log = LoggerFactory.getLogger(CompleteOrderService.class);

  private ProductOrderService productOrderService;

  public CompleteOrderService(ProductOrderService productOrderService) {

    this.productOrderService = productOrderService;
  }

  //  public CompleteOrder save(CompleteOrder completeOrder) throws Exception {
  //    log.debug("Request to save CompleteOrder : {}", completeOrder);
  //    CompleteOrder result;
  //    Optional<Basket> optBasket = basketRepository.findById(completeOrder.getCustomerId());
  //    if (optBasket.isPresent()) {
  //      Basket basket = optBasket.get();
  //      basket.getProductOrders().clear();
  //      this.basketRepository.save(basket); //clear basket
  //      result = completeOrderRepository.save(completeOrder);
  //      completeOrder.getProductOrders().forEach(p -> {
  //        p.setBasket(null);
  //        p.setCompleteOrder(completeOrder);
  //        this.productOrderService.save(p);
  //      });
  //    } else
  //      throw new Exception("Basket with ID " + completeOrder.getCustomerId() + " does not exist");
  //
  //    return result;
  //  }

  public void addCompleteOrderEvent(CompleteOrderEvent completeOrderEvent) {
    event.completeOrder.EventProducer eventProducer = new event.completeOrder.EventProducer();
    eventProducer.send(completeOrderEvent);
  }

  public List<CompleteOrder> aggregateCompleteOrderEvents() {
    event.completeOrder.EventConsumer eventConsumer = new event.completeOrder.EventConsumer();
    log.info("~aggregateCompleteOrderEvents");
    List<CompleteOrder> completeOrderList = new ArrayList<>();
    List<CompleteOrderEvent> completeOrderEvents = eventConsumer.consume();
    completeOrderEvents.forEach(event -> {
      if (event.getEvent().equals("COMPLETE_ORDER_CREATED")) {
        completeOrderList.add(event.getCompleteOrder());
      }
      if (event.getEvent().equals("COMPLETE_ORDER_DELETED")) {
        completeOrderList.remove(event.getCompleteOrder());
      }
      if (event.getEvent().equals("COMPLETE_ORDER_UPDATED")) {
        for (int i = 0; i < completeOrderList.size(); i++ ) {
          if (completeOrderList.get(i).getId().equals(event.getCompleteOrder().getId())) {
            completeOrderList.set(i, event.getCompleteOrder());
          }
        }
      }
    });

    return completeOrderList;
  }
}
