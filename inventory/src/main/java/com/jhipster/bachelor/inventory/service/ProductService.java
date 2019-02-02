package com.jhipster.bachelor.inventory.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhipster.bachelor.inventory.domain.Product;
import com.jhipster.bachelor.inventory.domain.ProductCategory;
import com.jhipster.bachelor.inventory.repository.ProductRepository;

import event.product.ProductEvent;
import event.productCategory.CategoryEvent;

/**
 * Service Implementation for managing Product.
 */
@Service
@Transactional
public class ProductService {

  private final Logger log = LoggerFactory.getLogger(ProductService.class);

  private ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  /**
   * Save a product.
   *
   * @param product the entity to save
   * @return the persisted entity
   */
  public Product save(Product product) {
    log.debug("Request to save Product : {}", product);
    return productRepository.save(product);
  }

  /**
   * Get all the products.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<Product> findAll() {
    log.debug("Request to get all Products");
    return productRepository.findAll();
  }

  /**
   * Get one product by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Optional<Product> findOne(Long id) {
    log.debug("Request to get Product : {}", id);
    return productRepository.findById(id);
  }

  /**
   * Delete the product by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete Product : {}", id);
    productRepository.deleteById(id);
  }

  public void addProductEvent(ProductEvent productEvent) {
    event.product.EventProducer eventProducer = new event.product.EventProducer();
    eventProducer.send(productEvent);
  }

  public void addCategoryEvent(CategoryEvent categoryEvent) {
    event.productCategory.EventProducer eventProducer = new event.productCategory.EventProducer();
    eventProducer.send(categoryEvent);
  }

  public List<Product> aggregateProductEvents() {
    event.product.EventConsumer eventConsumer = new event.product.EventConsumer();
    log.info("~aggregateProductEvents");
    List<Product> productList = new ArrayList<>();
    List<ProductEvent> productEvents = eventConsumer.consume();
    productEvents.forEach(event -> {
      if (event.getEvent().equals("PRODUCT_CREATED")) {
        productList.add(event.getProduct());
      }
      if (event.getEvent().equals("PRODUCT_DELETED")) {
        productList.remove(event.getProduct());
      }
      if (event.getEvent().equals("PRODUCT_UPDATED")) {
        for (int i = 0; i < productList.size(); i++ ) {
          if (productList.get(i).getId().equals(event.getProduct().getId())) {
            productList.set(i, event.getProduct());
          }
        }
      }
    });

    return productList;
  }

  public List<ProductCategory> aggregateCategoryEvents() {
    event.productCategory.EventConsumer eventConsumer = new event.productCategory.EventConsumer();
    log.info("~aggregateCategoryEvents");
    List<ProductCategory> categoryList = new ArrayList<>();
    List<CategoryEvent> categoryEvents = eventConsumer.consume();
    categoryEvents.forEach(event -> {
      if (event.getEvent().equals("CATEGORY_CREATED")) {
        categoryList.add(event.getCategory());
      }
      if (event.getEvent().equals("CATEGORY_DELETED")) {
        categoryList.remove(event.getCategory());
      }
      if (event.getEvent().equals("CATEGORY_UPDATED")) {
        for (int i = 0; i < categoryList.size(); i++ ) {
          if (categoryList.get(i).getId().equals(event.getCategory().getId())) {
            categoryList.set(i, event.getCategory());
          }
        }
      }
    });

    return categoryList;
  }
}
