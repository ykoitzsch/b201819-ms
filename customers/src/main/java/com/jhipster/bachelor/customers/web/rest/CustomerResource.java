package com.jhipster.bachelor.customers.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.customers.domain.Customer;
import com.jhipster.bachelor.customers.service.CustomerService;
import com.jhipster.bachelor.customers.web.rest.errors.BadRequestAlertException;
import com.jhipster.bachelor.customers.web.rest.util.HeaderUtil;

import event.CustomerEvent;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Customer.
 */
@RestController
@RequestMapping("/api")
public class CustomerResource {

  private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

  private static final String ENTITY_NAME = "customersCustomer";

  private CustomerService customerService;

  public CustomerResource(CustomerService customerService) {
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

  /**
   * POST /customers : Create a new customer.
   *
   * @param customer the customer to create
   * @return the ResponseEntity with status 201 (Created) and with body the new customer, or with status 400 (Bad
   *         Request) if the customer has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/customers")
  @Timed
  public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) throws URISyntaxException {
    log.debug("REST request to save Customer : {}", customer);
    Customer result = customerService.save(customer);
    return ResponseEntity
      .created(new URI("/api/customers/" + result.getId()))
      .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
      .body(result);
  }

  /**
   * PUT /customers : Updates an existing customer.
   *
   * @param customer the customer to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated customer, or with status 400 (Bad
   *         Request) if the customer is not valid, or with status 500 (Internal Server Error) if the customer couldn't
   *         be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/customers")
  @Timed
  public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) throws URISyntaxException {
    log.debug("REST request to update Customer : {}", customer);
    if (customer.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    Customer result = customerService.save(customer);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customer.getId().toString())).body(result);
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

  /**
   * DELETE /customers/:id : delete the "id" customer.
   *
   * @param id the id of the customer to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/customers/{id}")
  @Timed
  public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
    log.debug("REST request to delete Customer : {}", id);
    customerService.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }
}
