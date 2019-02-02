package com.jhipster.bachelor.invoices.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.invoices.domain.Invoice;
import com.jhipster.bachelor.invoices.service.InvoiceService;

import event.InvoiceEvent;

@RestController
@RequestMapping("/api")
public class EventResource {

  private InvoiceService invoiceService;

  public EventResource(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  @PostMapping("/events/{event}")
  @Timed
  public ResponseEntity<Object> addCustomerEvent(@RequestBody Invoice invoice, @PathVariable("event") String event) throws Exception {
    if ("INVOICE_DELETED".equals(event) || "INVOICE_UPDATED".equals(event) || "INVOICE_CREATED".equals(event)) {
      invoiceService.addInvoiceEvent(new InvoiceEvent(invoice, event));
    } else
      throw new Exception("Unknown Event");
    return new ResponseEntity<>(HttpStatus.OK);

  }

}
