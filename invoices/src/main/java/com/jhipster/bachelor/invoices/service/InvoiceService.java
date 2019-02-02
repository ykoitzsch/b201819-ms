package com.jhipster.bachelor.invoices.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhipster.bachelor.invoices.domain.Invoice;

import event.EventConsumer;
import event.EventProducer;
import event.InvoiceEvent;

/**
 * Service Implementation for managing Invoice.
 */
@Service
@Transactional
public class InvoiceService {

  private final Logger log = LoggerFactory.getLogger(InvoiceService.class);

  public InvoiceService() {
  }

  public void addInvoiceEvent(InvoiceEvent invoiceEvent) {
    EventProducer eventProducer = new EventProducer();
    eventProducer.send(invoiceEvent);
  }

  public List<Invoice> aggregateCustomerEvents() {
    EventConsumer eventConsumer = new EventConsumer();
    log.info("~aggregateInvoiceEvents");
    List<Invoice> invoiceList = new ArrayList<>();
    List<InvoiceEvent> invoiceEvents = eventConsumer.consume();
    invoiceEvents.forEach(event -> {
      if (event.getEvent().equals("INVOICE_CREATED")) {
        invoiceList.add(event.getInvoice());
      }
      if (event.getEvent().equals("INVOICE_DELETED")) {
        invoiceList.remove(event.getInvoice());
      }
      if (event.getEvent().equals("INVOICE_UPDATED")) {
        for (int i = 0; i < invoiceList.size(); i++ ) {
          if (invoiceList.get(i).getId().equals(event.getInvoice().getId())) {
            invoiceList.set(i, event.getInvoice());
          }
        }
      }
    });

    return invoiceList;
  }

}
