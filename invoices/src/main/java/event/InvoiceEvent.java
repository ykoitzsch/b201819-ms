package event;

import java.io.Serializable;
import java.sql.Timestamp;

import com.jhipster.bachelor.invoices.domain.Invoice;

public class InvoiceEvent implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7991373239880180472L;

  public InvoiceEvent() {
  }

  public InvoiceEvent(Invoice invoice, String event) {
    this.invoice = invoice;
    this.event = event;
    this.timestamp = new Timestamp(System.currentTimeMillis());
  }

  private Invoice invoice;

  private String event;

  private Timestamp timestamp;

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public Invoice getInvoice() {
    return invoice;
  }

  public void setInvoice(Invoice invoice) {
    this.invoice = invoice;
  }

  @Override
  public String toString() {
    return "InvoiceEvent [invoice=" + invoice + ", event=" + event + ", timestamp=" + timestamp + "]";
  }

}
