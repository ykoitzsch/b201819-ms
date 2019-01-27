package event;

import java.io.Serializable;
import java.sql.Timestamp;

import com.jhipster.bachelor.customers.domain.Customer;

public class CustomerEvent implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7991373239880180472L;

  public CustomerEvent() {
  }

  public CustomerEvent(Customer customer, String event) {
    this.customer = customer;
    this.event = event;
    this.timestamp = new Timestamp(System.currentTimeMillis());
  }

  private Customer customer;

  private String event;

  private Timestamp timestamp;

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "CustomerEvent [customer=" + customer + ", event=" + event + ", timestamp=" + timestamp + "]";
  }

}
