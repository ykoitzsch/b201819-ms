package event.productOrder;

import java.io.Serializable;
import java.sql.Timestamp;

import com.jhipster.bachelor.orders.domain.ProductOrder;

public class ProductOrderEvent implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7991373239880180472L;

  public ProductOrderEvent() {
  }

  public ProductOrderEvent(ProductOrder productOrder, String event) {
    this.productOrder = productOrder;
    this.event = event;
    this.timestamp = new Timestamp(System.currentTimeMillis());
  }

  private ProductOrder productOrder;

  private String event;

  private Timestamp timestamp;

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public ProductOrder getProductOrder() {
    return productOrder;
  }

  public void setProductOrder(ProductOrder productOrder) {
    this.productOrder = productOrder;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "ProductOrderEvent [productOrder=" + productOrder + ", event=" + event + ", timestamp=" + timestamp + "]";
  }

}
