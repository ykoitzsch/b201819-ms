package event;

import java.io.Serializable;
import java.sql.Timestamp;

import com.jhipster.bachelor.ratings.domain.Rating;

public class RatingEvent implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7991373239880180472L;

  public RatingEvent() {
  }

  public RatingEvent(Rating rating, String event) {
    this.rating = rating;
    this.event = event;
    this.timestamp = new Timestamp(System.currentTimeMillis());
  }

  private Rating rating;

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

  public Rating getRating() {
    return rating;
  }

  public void setRating(Rating rating) {
    this.rating = rating;
  }

  @Override
  public String toString() {
    return "RatingEvent [event=" + event + ", timestamp=" + timestamp + "]";
  }

}
