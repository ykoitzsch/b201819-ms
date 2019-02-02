package com.jhipster.bachelor.customers.web.rest;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.customers.domain.Customer;
import com.jhipster.bachelor.customers.service.CustomerService;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Customer.
 */
@RestController
@RequestMapping("/api")
public class CustomerResource {

  private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

  private CustomerService customerService;

  public CustomerResource(CustomerService customerService) {
    this.customerService = customerService;
  }

  /**
   * GET /customers : get all the customers.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of customers in body
   * @throws ExecutionException
   * @throws InterruptedException
   */
  @GetMapping("/customers")
  @Timed
  public List<Customer> getAllCustomers() throws InterruptedException, ExecutionException {
    log.debug("REST request to get all Customers");
    return customerService.aggregateCustomerEvents();
  }

  /**
   * GET /customers/:id : get the "id" customer.
   *
   * @param id the id of the customer to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the customer, or with status 404 (Not Found)
   * @throws ExecutionException
   * @throws InterruptedException
   */
  @GetMapping("/customers/{id}")
  @Timed
  public ResponseEntity<Customer> getCustomer(@PathVariable Long id) throws InterruptedException, ExecutionException {
    log.debug("REST request to get Customer : {}", id);
    return ResponseUtil.wrapOrNotFound(getAllCustomers().stream().filter(c -> c.getId().equals(id)).findFirst());
  }
}
