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
import com.jhipster.bachelor.orders.domain.CompleteOrder;
import com.jhipster.bachelor.orders.domain.ProductOrder;

import event.ConsumerChannelT;
import event.completeOrder.CompleteOrderEvent;
import event.productOrder.ProductOrderEvent;

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
    for (ProductOrder p : completeOrderEvent.getCompleteOrder().getProductOrders()) {
      p.setCompleteOrder(completeOrderEvent.getCompleteOrder());
      productOrderService.addProductOrderEvent(new ProductOrderEvent(p, "PRODUCT_ORDER_UPDATED"));
    }
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

  @StreamListener(ConsumerChannelT.INPUT)
  public void something(@Payload String payload) {
    JsonObject obj = new JsonParser().parse(payload).getAsJsonObject();
    if ("INVOICE_CREATED".equals(obj.get("event").getAsString())) {
      log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
      long invoiceId = obj.getAsJsonObject("invoice").get("id").getAsLong();
      long orderId = obj.getAsJsonObject("invoice").get("orderId").getAsLong();
      CompleteOrder co = aggregateCompleteOrderEvents().stream().filter(c -> c.getId().equals(orderId)).findFirst().get();
      co.setInvoiceId(invoiceId);
      addCompleteOrderEvent(new CompleteOrderEvent(co, "COMPLETE_ORDER_UPDATED"));
    }
  }
}
