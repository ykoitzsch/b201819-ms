package com.jhipster.bachelor.invoices.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.invoices.domain.Invoice;
import com.jhipster.bachelor.invoices.service.InvoiceService;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Invoice.
 */
@RestController
@RequestMapping("/api")
public class InvoiceResource {

  private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);

  private InvoiceService invoiceService;

  public InvoiceResource(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  @GetMapping("/invoices")
  @Timed
  public List<Invoice> getAllInvoices() {
    log.debug("REST request to get all Invoices");
    return invoiceService.aggregateCustomerEvents();
  }

  @GetMapping("/invoices/{id}")
  @Timed
  public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
    log.debug("REST request to get Invoice : {}", id);
    return ResponseUtil.wrapOrNotFound(getAllInvoices().stream().filter(c -> c.getId().equals(id)).findFirst());

  }

}
