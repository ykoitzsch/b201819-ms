package com.jhipster.bachelor.orders.service;

import com.jhipster.bachelor.orders.domain.Basket;
import com.jhipster.bachelor.orders.domain.ProductOrder;
import com.jhipster.bachelor.orders.repository.BasketRepository;
import com.jhipster.bachelor.orders.repository.ProductOrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing Basket.
 */
@Service
@Transactional
public class BasketService {

    private final Logger log = LoggerFactory.getLogger(BasketService.class);

    private BasketRepository basketRepository;
    private ProductOrderRepository productOrderRepository;

    public BasketService(BasketRepository basketRepository, ProductOrderRepository productOrderRepository) {
        this.basketRepository = basketRepository;
        this.productOrderRepository = productOrderRepository;
    }

    /**
     * Save a basket.
     *
     * @param basket the entity to save
     * @return the persisted entity
     */
    public Basket save(Basket basket) {
        log.debug("Request to save Basket : {}", basket);
    	Optional<Basket> optTempBasket = findOne(basket.getId());
    	if(optTempBasket.isPresent()) {
    		Basket tempBasket = optTempBasket.get();
	    	log.info(tempBasket.getProductOrders().size() + " vs " + basket.getProductOrders().size());
	    	if(tempBasket.getProductOrders().size() < basket.getProductOrders().size()) { //adding new ProductOrders
	            Set<ProductOrder> tempOrders = new HashSet<>();
	            if(basket.getProductOrders() != null) {
	            	for(ProductOrder pO : basket.getProductOrders()) { //clone
	            		if(pO.getBasket() == null)
	            			tempOrders.add(pO);
	            	}
	    	        tempOrders.forEach(p -> {
	    	        	p.setBasket(basket);
	    	        	basket.getProductOrders().add(productOrderRepository.save(p));
	    	        });
	            }	
	    	}
	    	else if(tempBasket.getProductOrders().size() > basket.getProductOrders().size()) { //removing ProductOrders
	    		log.info("~removing productOrders!");
	    		for(ProductOrder p : tempBasket.getProductOrders()) { //iterate over all exising productOrders in db and check which ones are missing 
	    			if(!basket.getProductOrders().contains(p)) {
	    				log.info("~delete " + p.toString());
	    				//tempBasket.removeProductOrder(p);
	    				productOrderRepository.delete(p);
	    			}
	    		}
	    	}
    	}
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
