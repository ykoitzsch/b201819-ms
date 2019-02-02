package com.jhipster.bachelor.customers.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhipster.bachelor.customers.domain.Customer;

import event.CustomerEvent;
import event.EventConsumer;
import event.EventProducer;

/**
 * Service Implementation for managing Customer.
 */
@Service
@Transactional
public class CustomerService {

  private final Logger log = LoggerFactory.getLogger(CustomerService.class);

  public CustomerService() {

  }

  public void addCustomerEvent(CustomerEvent customerEvent) {
    EventProducer eventProducer = new EventProducer();
    eventProducer.send(customerEvent);
  }

  public List<Customer> aggregateCustomerEvents() {
    EventConsumer eventConsumer = new EventConsumer();
    log.info("~aggregateCustomerEvents");
    List<Customer> customerList = new ArrayList<>();
    List<CustomerEvent> customerEvents = eventConsumer.consume();
    customerEvents.forEach(event -> {
      if (event.getEvent().equals("CUSTOMER_CREATED")) {
        customerList.add(event.getCustomer());
      }
      if (event.getEvent().equals("CUSTOMER_DELETED")) {
        customerList.remove(event.getCustomer());
      }
      if (event.getEvent().equals("CUSTOMER_UPDATED")) {
        for (int i = 0; i < customerList.size(); i++ ) {
          if (customerList.get(i).getId().equals(event.getCustomer().getId())) {
            customerList.set(i, event.getCustomer());
          }
        }
      }
    });

    return customerList;
  }
}
