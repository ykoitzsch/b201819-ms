package com.jhipster.bachelor.ratings.service;

import com.jhipster.bachelor.ratings.domain.Rating;
import com.jhipster.bachelor.ratings.repository.RatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Rating.
 */
@Service
@Transactional
public class RatingService {

    private final Logger log = LoggerFactory.getLogger(RatingService.class);

    private RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    /**
     * Save a rating.
     *
     * @param rating the entity to save
     * @return the persisted entity
     */
    public Rating save(Rating rating) {
        log.debug("Request to save Rating : {}", rating);
        return ratingRepository.save(rating);
    }

    /**
     * Get all the ratings.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Rating> findAll() {
        log.debug("Request to get all Ratings");
        return ratingRepository.findAll();
    }


    /**
     * Get one rating by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Rating> findOne(Long id) {
        log.debug("Request to get Rating : {}", id);
        return ratingRepository.findById(id);
    }

    /**
     * Delete the rating by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Rating : {}", id);
        ratingRepository.deleteById(id);
    }
}
