package com.jhipster.bachelor.inventory.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhipster.bachelor.inventory.domain.Product;

import event.product.ProductEvent;

/**
 * Service Implementation for managing Product.
 */
@Service
@Transactional
public class ProductService {

  private final Logger log = LoggerFactory.getLogger(ProductService.class);

  public ProductService() {
  }

  public void addProductEvent(ProductEvent productEvent) {
    event.product.EventProducer eventProducer = new event.product.EventProducer();
    eventProducer.send(productEvent);
  }

  public List<Product> aggregateProductEvents() {
    event.product.EventConsumer eventConsumer = new event.product.EventConsumer();
    log.info("~aggregateProductEvents");
    List<Product> productList = new ArrayList<>();
    List<ProductEvent> productEvents = eventConsumer.consume();
    productEvents.forEach(event -> {
      if (event.getEvent().equals("PRODUCT_CREATED")) {
        productList.add(event.getProduct());
      }
      if (event.getEvent().equals("PRODUCT_DELETED")) {
        productList.remove(event.getProduct());
      }
      if (event.getEvent().equals("PRODUCT_UPDATED")) {
        for (int i = 0; i < productList.size(); i++ ) {
          if (productList.get(i).getId().equals(event.getProduct().getId())) {
            productList.set(i, event.getProduct());
          }
        }
      }
    });

    return productList;
  }
}
