package event.product;

import java.io.Serializable;
import java.sql.Timestamp;

import com.jhipster.bachelor.inventory.domain.Product;

public class ProductEvent implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7991373239880180472L;

  public ProductEvent() {
  }

  public ProductEvent(Product product, String event) {
    this.product = product;
    this.event = event;
    this.timestamp = new Timestamp(System.currentTimeMillis());
  }

  private Product product;

  private String event;

  private Timestamp timestamp;

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "InventoryEvent [product=" + product + ", event=" + event + ", timestamp=" + timestamp + "]";
  }

}
