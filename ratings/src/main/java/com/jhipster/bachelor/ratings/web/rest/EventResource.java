package com.jhipster.bachelor.ratings.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.ratings.domain.Rating;
import com.jhipster.bachelor.ratings.service.RatingService;

import event.RatingEvent;

@RestController
@RequestMapping("/api")
public class EventResource {

  private RatingService ratingService;

  public EventResource(RatingService ratingService) {
    this.ratingService = ratingService;
  }

  @PostMapping("/events/{event}")
  @Timed
  public ResponseEntity<Rating> addCustomerEvent(@RequestBody Rating rating, @PathVariable("event") String event) throws Exception {
    if ("RATING_DELETED".equals(event) || "RATING_UPDATED".equals(event) || "RATING_CREATED".equals(event)) {
      ratingService.addRatingEvent(new RatingEvent(rating, event));
    } else
      throw new Exception("Unknown Event");
    return new ResponseEntity<>(rating, HttpStatus.OK);
  }

}
