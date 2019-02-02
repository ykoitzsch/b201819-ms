package com.jhipster.bachelor.ratings.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhipster.bachelor.ratings.domain.Rating;

import event.EventConsumer;
import event.EventProducer;
import event.RatingEvent;

/**
 * Service Implementation for managing Rating.
 */
@Service
@Transactional
public class RatingService {

  private final Logger log = LoggerFactory.getLogger(RatingService.class);

  public RatingService() {

  }

  public void addRatingEvent(RatingEvent ratingEvent) {
    EventProducer eventProducer = new EventProducer();
    eventProducer.send(ratingEvent);
  }

  public List<Rating> aggregateRatingEvents() {
    EventConsumer eventConsumer = new EventConsumer();
    log.info("~aggregateRatingEvents");
    List<Rating> ratingList = new ArrayList<>();
    List<RatingEvent> ratingEvents = eventConsumer.consume();
    ratingEvents.forEach(event -> {
      if (event.getEvent().equals("RATING_CREATED")) {
        ratingList.add(event.getRating());
      }
      if (event.getEvent().equals("RATING_DELETED")) {
        ratingList.remove(event.getRating());
      }
      if (event.getEvent().equals("RATING_UPDATED")) {
        for (int i = 0; i < ratingList.size(); i++ ) {
          if (ratingList.get(i).getId().equals(event.getRating().getId())) {
            ratingList.set(i, event.getRating());
          }
        }
      }
    });

    return ratingList;
  }

  public List<Rating> aggregateRatingEventsForProduct(String productId) {
    EventConsumer eventConsumer = new EventConsumer();
    log.info("~aggregateRatingEventsForProduct");
    List<Rating> ratingList = new ArrayList<>();
    List<RatingEvent> ratingEvents = eventConsumer.consume();
    ratingEvents.stream().filter(r -> r.getRating().getProductId().toString().equals(productId)).forEach(event -> {
      if (event.getEvent().equals("RATING_CREATED")) {
        ratingList.add(event.getRating());
      }
      if (event.getEvent().equals("RATING_DELETED")) {
        ratingList.remove(event.getRating());
      }
      if (event.getEvent().equals("RATING_UPDATED")) {
        for (int i = 0; i < ratingList.size(); i++ ) {
          if (ratingList.get(i).getId().equals(event.getRating().getId())) {
            ratingList.set(i, event.getRating());
          }
        }
      }
    });

    return ratingList;
  }

}
