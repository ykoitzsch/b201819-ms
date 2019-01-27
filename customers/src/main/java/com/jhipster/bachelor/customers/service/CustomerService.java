package com.jhipster.bachelor.customers.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhipster.bachelor.customers.domain.Customer;
import com.jhipster.bachelor.customers.repository.CustomerRepository;

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

  private CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;

  }

  /**
   * Save a customer.
   *
   * @param customer the entity to save
   * @return the persisted entity
   */
  public Customer save(Customer customer) {
    log.debug("Request to save Customer : {}", customer);
    return customerRepository.save(customer);
  }

  /**
   * Get all the customers.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<Customer> findAll() {
    log.debug("Request to get all Customers");
    return customerRepository.findAll();
  }

  /**
   * Get one customer by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Optional<Customer> findOne(Long id) {
    log.debug("Request to get Customer : {}", id);
    return customerRepository.findById(id);
  }

  /**
   * Delete the customer by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete Customer : {}", id);
    customerRepository.deleteById(id);
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
