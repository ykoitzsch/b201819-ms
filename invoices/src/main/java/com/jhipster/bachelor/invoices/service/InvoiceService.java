package com.jhipster.bachelor.invoices.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jhipster.bachelor.invoices.domain.Invoice;
import com.jhipster.bachelor.invoices.domain.enumeration.InvoiceStatus;

import event.ConsumerChannel;
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

  @StreamListener(ConsumerChannel.INPUT)
  public void something(@Payload String payload) {
    JsonObject obj = new JsonParser().parse(payload).getAsJsonObject();
    if ("COMPLETE_ORDER_UPDATED".equals(obj.get("event").getAsString())) {
      if (obj.getAsJsonObject("completeOrder").get("status").getAsString().equals("COMPLETED") &&
        obj.getAsJsonObject("completeOrder").get("invoiceId").toString().equals("null")) {
        Invoice invoice = new Invoice();
        invoice.setId(obj.getAsJsonObject("completeOrder").get("id").getAsLong());
        invoice.setCustomerId(obj.getAsJsonObject("completeOrder").get("customerId").getAsLong());
        invoice.setDueDate("2019-31-12");
        invoice.setCode(UUID.randomUUID().toString());
        invoice.setAmount(obj.getAsJsonObject("completeOrder").get("totalPrice").getAsDouble());
        invoice.setOrderId(invoice.getId());
        invoice.setPaymentDate(LocalDateTime.now().toString());
        invoice.setStatus(InvoiceStatus.PAID);
        addInvoiceEvent(new InvoiceEvent(invoice, "INVOICE_CREATED"));
      }
    }
  }

}
