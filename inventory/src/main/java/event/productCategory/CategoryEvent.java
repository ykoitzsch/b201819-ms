package event.productCategory;

import java.io.Serializable;
import java.sql.Timestamp;

import com.jhipster.bachelor.inventory.domain.ProductCategory;

public class CategoryEvent implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7991373239880180472L;

  public CategoryEvent() {
  }

  public CategoryEvent(ProductCategory category, String event) {
    this.category = category;
    this.event = event;
    this.timestamp = new Timestamp(System.currentTimeMillis());
  }

  private ProductCategory category;

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

  public ProductCategory getCategory() {
    return category;
  }

  public void setCategory(ProductCategory category) {
    this.category = category;
  }

  @Override
  public String toString() {
    return "CategoryEvent [category=" + category + ", event=" + event + ", timestamp=" + timestamp + "]";
  }

}
