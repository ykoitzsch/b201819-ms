package com.jhipster.bachelor.ratings.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.ratings.domain.Rating;
import com.jhipster.bachelor.ratings.service.RatingService;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Rating.
 */
@RestController
@RequestMapping("/api")
public class RatingResource {

  private final Logger log = LoggerFactory.getLogger(RatingResource.class);

  private RatingService ratingService;

  public RatingResource(RatingService ratingService, Processor processor) {
    this.ratingService = ratingService;
  }

  @GetMapping("/ratings")
  @Timed
  public List<Rating> getAllRatings(@RequestParam(value = "productId", required = false) String productId) {
    log.debug("REST request to get all Ratings");
    if (productId == null)
      return ratingService.aggregateRatingEvents();
    else
      return ratingService.aggregateRatingEventsForProduct(productId);
  }

  @GetMapping("/ratings/{id}")
  @Timed
  public ResponseEntity<Rating> getRating(@PathVariable Long id) {
    log.debug("REST request to get Rating : {}", id);
    return ResponseUtil.wrapOrNotFound(getAllRatings(null).stream().filter(c -> c.getId().equals(id)).findFirst());
  }
}
