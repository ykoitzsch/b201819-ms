package event.completeOrder;

import java.io.Serializable;
import java.sql.Timestamp;

import com.jhipster.bachelor.orders.domain.CompleteOrder;

public class CompleteOrderEvent implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7991373239880180472L;

  public CompleteOrderEvent() {
  }

  public CompleteOrderEvent(CompleteOrder completeOrder, String event) {
    this.completeOrder = completeOrder;
    this.event = event;
    this.timestamp = new Timestamp(System.currentTimeMillis());
  }

  private CompleteOrder completeOrder;

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

  public CompleteOrder getCompleteOrder() {
    return completeOrder;
  }

  public void setCompleteOrder(CompleteOrder completeOrder) {
    this.completeOrder = completeOrder;
  }

  @Override
  public String toString() {
    return "CompleteOrderEvent [event=" + event + ", timestamp=" + timestamp + "]";
  }

}
