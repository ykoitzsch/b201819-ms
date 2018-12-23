package com.jhipster.bachelor.ratings.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.ratings.domain.Rating;
import com.jhipster.bachelor.ratings.service.RatingService;
import com.jhipster.bachelor.ratings.web.rest.errors.BadRequestAlertException;
import com.jhipster.bachelor.ratings.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Rating.
 */
@RestController
@RequestMapping("/api")
public class RatingResource {

    private final Logger log = LoggerFactory.getLogger(RatingResource.class);

    private static final String ENTITY_NAME = "ratingsRating";

    private RatingService ratingService;
    
    private MessageChannel channel;

    public RatingResource(RatingService ratingService, Processor processor) {
        this.ratingService = ratingService;
        this.channel = processor.output();
    }

    /**
     * POST  /ratings : Create a new rating.
     *
     * @param rating the rating to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rating, or with status 400 (Bad Request) if the rating has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ratings")
    @Timed
    public ResponseEntity<Rating> createRating(@Valid @RequestBody Rating rating) throws URISyntaxException {
        log.debug("REST request to save Rating : {}", rating);
        if (rating.getId() != null) {
            throw new BadRequestAlertException("A new rating cannot already have an ID", ENTITY_NAME, "idexists");
        }
        log.debug("ich bin hier!!!");
        channel.send(MessageBuilder.withPayload(rating).build());
        return ResponseEntity.ok(rating);
    }
    
/*    @PostMapping("/ratings")
    @Timed
    public ResponseEntity<Rating> createRating(@Valid @RequestBody Rating rating) throws URISyntaxException {
        log.debug("REST request to save Rating : {}", rating);
        if (rating.getId() != null) {
            throw new BadRequestAlertException("A new rating cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rating result = ratingService.save(rating);
        return ResponseEntity.created(new URI("/api/ratings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/
    
    

    /**
     * PUT  /ratings : Updates an existing rating.
     *
     * @param rating the rating to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rating,
     * or with status 400 (Bad Request) if the rating is not valid,
     * or with status 500 (Internal Server Error) if the rating couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ratings")
    @Timed
    public ResponseEntity<Rating> updateRating(@Valid @RequestBody Rating rating) throws URISyntaxException {
        log.debug("REST request to update Rating : {}", rating);
        if (rating.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Rating result = ratingService.save(rating);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rating.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ratings : get all the ratings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ratings in body
     */
    @GetMapping("/ratings")
    @Timed
    public List<Rating> getAllRatings(@RequestParam(value="productId", required = false) String productId) {
        log.debug("REST request to get all Ratings");
        if(productId == null)
        	return ratingService.findAll();
        else 
        	return ratingService.findByProductId(productId);
    }

    /**
     * GET  /ratings/:id : get the "id" rating.
     *
     * @param id the id of the rating to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rating, or with status 404 (Not Found)
     */
    @GetMapping("/ratings/{id}")
    @Timed
    public ResponseEntity<Rating> getRating(@PathVariable Long id) {
        log.debug("REST request to get Rating : {}", id);
        Optional<Rating> rating = ratingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rating);
    }

    /**
     * DELETE  /ratings/:id : delete the "id" rating.
     *
     * @param id the id of the rating to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ratings/{id}")
    @Timed
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        log.debug("REST request to delete Rating : {}", id);
        ratingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
