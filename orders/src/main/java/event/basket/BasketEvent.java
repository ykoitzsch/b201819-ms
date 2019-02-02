package event.basket;

import java.io.Serializable;
import java.sql.Timestamp;

import com.jhipster.bachelor.orders.domain.Basket;

public class BasketEvent implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7991373239880180472L;

  public BasketEvent() {
  }

  public BasketEvent(Basket basket, String event) {
    this.basket = basket;
    this.event = event;
    this.timestamp = new Timestamp(System.currentTimeMillis());
  }

  private Basket basket;

  private String event;

  private Timestamp timestamp;

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public Basket getBasket() {
    return basket;
  }

  public void setBasket(Basket basket) {
    this.basket = basket;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "BasketEvent [basket=" + basket + ", event=" + event + ", timestamp=" + timestamp + "]";
  }

}
