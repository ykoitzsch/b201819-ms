package com.jhipster.bachelor.orders.service;

import com.jhipster.bachelor.orders.domain.Basket;
import com.jhipster.bachelor.orders.domain.CompleteOrder;
import com.jhipster.bachelor.orders.repository.BasketRepository;
import com.jhipster.bachelor.orders.repository.CompleteOrderRepository;
import com.jhipster.bachelor.orders.repository.ProductOrderRepository;
import com.jhipster.bachelor.orders.web.rest.errors.BadRequestAlertException;

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
    private BasketRepository basketRepository;
    private ProductOrderService productOrderService;

    public CompleteOrderService(CompleteOrderRepository completeOrderRepository, BasketRepository basketRepository, ProductOrderService productOrderService) {
        this.completeOrderRepository = completeOrderRepository;
        this.basketRepository = basketRepository;
        this.productOrderService = productOrderService;
    }

    /**
     * Save a completeOrder.
     *
     * @param completeOrder the entity to save
     * @return the persisted entity
     * @throws Exception 
     */
    public CompleteOrder save(CompleteOrder completeOrder) throws Exception {
        log.debug("Request to save CompleteOrder : {}", completeOrder);
        CompleteOrder result;
        Optional<Basket> optBasket = basketRepository.findById(completeOrder.getCustomerId());
        if(optBasket.isPresent()) {
        	Basket basket = optBasket.get();
        	basket.getProductOrders().clear();
        	this.basketRepository.save(basket); //clear basket
        	result = completeOrderRepository.save(completeOrder);
        	completeOrder.getProductOrders().forEach(p -> {
        		p.setBasket(null);
        		p.setCompleteOrder(completeOrder);
        		this.productOrderService.save(p);
        	});
        }
        else throw new Exception("Basket with ID " + completeOrder.getCustomerId() + " does not exist");
             
        return result;
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
