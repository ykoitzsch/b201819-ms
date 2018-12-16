package com.jhipster.bachelor.orders.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.orders.domain.CompleteOrder;
import com.jhipster.bachelor.orders.service.CompleteOrderService;
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
 * REST controller for managing CompleteOrder.
 */
@RestController
@RequestMapping("/api")
public class CompleteOrderResource {

    private final Logger log = LoggerFactory.getLogger(CompleteOrderResource.class);

    private static final String ENTITY_NAME = "ordersCompleteOrder";

    private CompleteOrderService completeOrderService;

    public CompleteOrderResource(CompleteOrderService completeOrderService) {
        this.completeOrderService = completeOrderService;
    }

    /**
     * POST  /complete-orders : Create a new completeOrder.
     *
     * @param completeOrder the completeOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new completeOrder, or with status 400 (Bad Request) if the completeOrder has already an ID
     * @throws Exception 
     */
    @PostMapping("/complete-orders")
    @Timed
    public ResponseEntity<CompleteOrder> createCompleteOrder(@RequestBody CompleteOrder completeOrder) throws Exception {
        log.debug("REST request to save CompleteOrder : {}", completeOrder);
        if (completeOrder.getId() != null) {
            throw new BadRequestAlertException("A new completeOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompleteOrder result = completeOrderService.save(completeOrder);
        return ResponseEntity.created(new URI("/api/complete-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /complete-orders : Updates an existing completeOrder.
     *
     * @param completeOrder the completeOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated completeOrder,
     * or with status 400 (Bad Request) if the completeOrder is not valid,
     * or with status 500 (Internal Server Error) if the completeOrder couldn't be updated
     * @throws Exception 
     */
    @PutMapping("/complete-orders")
    @Timed
    public ResponseEntity<CompleteOrder> updateCompleteOrder(@RequestBody CompleteOrder completeOrder) throws Exception {
        log.debug("REST request to update CompleteOrder : {}", completeOrder);
        if (completeOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CompleteOrder result = completeOrderService.save(completeOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, completeOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /complete-orders : get all the completeOrders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of completeOrders in body
     */
    @GetMapping("/complete-orders")
    @Timed
    public List<CompleteOrder> getAllCompleteOrders() {
        log.debug("REST request to get all CompleteOrders");
        return completeOrderService.findAll();
    }

    /**
     * GET  /complete-orders/:id : get the "id" completeOrder.
     *
     * @param id the id of the completeOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the completeOrder, or with status 404 (Not Found)
     */
    @GetMapping("/complete-orders/{id}")
    @Timed
    public ResponseEntity<CompleteOrder> getCompleteOrder(@PathVariable Long id) {
        log.debug("REST request to get CompleteOrder : {}", id);
        Optional<CompleteOrder> completeOrder = completeOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(completeOrder);
    }

    /**
     * DELETE  /complete-orders/:id : delete the "id" completeOrder.
     *
     * @param id the id of the completeOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/complete-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompleteOrder(@PathVariable Long id) {
        log.debug("REST request to delete CompleteOrder : {}", id);
        completeOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
