package com.jhipster.bachelor.customers.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.customers.domain.Customer;
import com.jhipster.bachelor.customers.service.CustomerService;

import event.CustomerEvent;

@RestController
@RequestMapping("/api")
public class EventResource {

  private CustomerService customerService;

  public EventResource(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping("/events/{event}")
  @Timed
  public ResponseEntity<Object> addCustomerEvent(@RequestBody Customer customer, @PathVariable("event") String event) throws Exception {
    if ("CUSTOMER_DELETED".equals(event) || "CUSTOMER_UPDATED".equals(event) || "CUSTOMER_CREATED".equals(event)) {
      customerService.addCustomerEvent(new CustomerEvent(customer, event));
    } else
      throw new Exception("Unknown Event");
    return new ResponseEntity<>(HttpStatus.OK);

  }

}
