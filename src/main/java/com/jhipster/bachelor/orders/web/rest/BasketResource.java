package com.jhipster.bachelor.orders.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.orders.domain.Basket;
import com.jhipster.bachelor.orders.repository.BasketRepository;
import com.jhipster.bachelor.orders.web.rest.errors.BadRequestAlertException;
import com.jhipster.bachelor.orders.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Basket.
 */
@RestController
@RequestMapping("/api")
public class BasketResource {

    private final Logger log = LoggerFactory.getLogger(BasketResource.class);

    private static final String ENTITY_NAME = "testBasket";

    private BasketRepository basketRepository;

    public BasketResource(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    /**
     * POST  /baskets : Create a new basket.
     *
     * @param basket the basket to create
     * @return the ResponseEntity with status 201 (Created) and with body the new basket, or with status 400 (Bad Request) if the basket has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/baskets")
    @Timed
    public ResponseEntity<Basket> createBasket(@RequestBody Basket basket) throws URISyntaxException {
        log.debug("REST request to save Basket : {}", basket);
        if (basket.getId() != null) {
            throw new BadRequestAlertException("A new basket cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Basket result = basketRepository.save(basket);
        return ResponseEntity.created(new URI("/api/baskets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /baskets : Updates an existing basket.
     *
     * @param basket the basket to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated basket,
     * or with status 400 (Bad Request) if the basket is not valid,
     * or with status 500 (Internal Server Error) if the basket couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/baskets")
    @Timed
    public ResponseEntity<Basket> updateBasket(@RequestBody Basket basket) throws URISyntaxException {
        log.debug("REST request to update Basket : {}", basket);
        if (basket.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Basket result = basketRepository.save(basket);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, basket.getId().toString()))
            .body(result);
    }

    /**
     * GET  /baskets : get all the baskets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of baskets in body
     */
    @GetMapping("/baskets")
    @Timed
    public List<Basket> getAllBaskets() {
        log.debug("REST request to get all Baskets");
        return basketRepository.findAll();
    }

    /**
     * GET  /baskets/:id : get the "id" basket.
     *
     * @param id the id of the basket to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the basket, or with status 404 (Not Found)
     */
    @GetMapping("/baskets/{id}")
    @Timed
    public ResponseEntity<Basket> getBasket(@PathVariable Long id) {
        log.debug("REST request to get Basket : {}", id);
        Optional<Basket> basket = basketRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(basket);
    }

    /**
     * DELETE  /baskets/:id : delete the "id" basket.
     *
     * @param id the id of the basket to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/baskets/{id}")
    @Timed
    public ResponseEntity<Void> deleteBasket(@PathVariable Long id) {
        log.debug("REST request to delete Basket : {}", id);

        basketRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
