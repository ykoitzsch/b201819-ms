package com.jhipster.bachelor.orders.service;

import com.jhipster.bachelor.orders.domain.Basket;
import com.jhipster.bachelor.orders.repository.BasketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Basket.
 */
@Service
@Transactional
public class BasketService {

    private final Logger log = LoggerFactory.getLogger(BasketService.class);

    private BasketRepository basketRepository;

    public BasketService(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    /**
     * Save a basket.
     *
     * @param basket the entity to save
     * @return the persisted entity
     */
    public Basket save(Basket basket) {
        log.debug("Request to save Basket : {}", basket);
        return basketRepository.save(basket);
    }

    /**
     * Get all the baskets.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Basket> findAll() {
        log.debug("Request to get all Baskets");
        return basketRepository.findAll();
    }


    /**
     * Get one basket by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Basket> findOne(Long id) {
        log.debug("Request to get Basket : {}", id);
        return basketRepository.findById(id);
    }

    /**
     * Delete the basket by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Basket : {}", id);
        basketRepository.deleteById(id);
    }
}
