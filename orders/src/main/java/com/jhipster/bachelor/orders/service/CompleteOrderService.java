package com.jhipster.bachelor.orders.service;

import com.jhipster.bachelor.orders.domain.CompleteOrder;
import com.jhipster.bachelor.orders.repository.CompleteOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing CompleteOrder.
 */
@Service
@Transactional
public class CompleteOrderService {

    private final Logger log = LoggerFactory.getLogger(CompleteOrderService.class);

    private CompleteOrderRepository completeOrderRepository;

    public CompleteOrderService(CompleteOrderRepository completeOrderRepository) {
        this.completeOrderRepository = completeOrderRepository;
    }

    /**
     * Save a completeOrder.
     *
     * @param completeOrder the entity to save
     * @return the persisted entity
     */
    public CompleteOrder save(CompleteOrder completeOrder) {
        log.debug("Request to save CompleteOrder : {}", completeOrder);
        return completeOrderRepository.save(completeOrder);
    }

    /**
     * Get all the completeOrders.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CompleteOrder> findAll() {
        log.debug("Request to get all CompleteOrders");
        return completeOrderRepository.findAll();
    }


    /**
     * Get one completeOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<CompleteOrder> findOne(Long id) {
        log.debug("Request to get CompleteOrder : {}", id);
        return completeOrderRepository.findById(id);
    }

    /**
     * Delete the completeOrder by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CompleteOrder : {}", id);
        completeOrderRepository.deleteById(id);
    }
}
