package com.jhipster.bachelor.inventory.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhipster.bachelor.inventory.domain.ProductCategory;
import com.jhipster.bachelor.inventory.repository.ProductCategoryRepository;

import event.productCategory.CategoryEvent;

/**
 * Service Implementation for managing ProductCategory.
 */
@Service
@Transactional
public class ProductCategoryService {

  private final Logger log = LoggerFactory.getLogger(ProductCategoryService.class);

  private ProductCategoryRepository productCategoryRepository;

  public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
    this.productCategoryRepository = productCategoryRepository;
  }

  /**
   * Save a productCategory.
   *
   * @param productCategory the entity to save
   * @return the persisted entity
   */
  public ProductCategory save(ProductCategory productCategory) {
    log.debug("Request to save ProductCategory : {}", productCategory);
    return productCategoryRepository.save(productCategory);
  }

  /**
   * Get all the productCategories.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<ProductCategory> findAll() {
    log.debug("Request to get all ProductCategories");
    return productCategoryRepository.findAll();
  }

  /**
   * Get one productCategory by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Optional<ProductCategory> findOne(Long id) {
    log.debug("Request to get ProductCategory : {}", id);
    return productCategoryRepository.findById(id);
  }

  /**
   * Delete the productCategory by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete ProductCategory : {}", id);
    productCategoryRepository.deleteById(id);
  }

  public void addCategoryEvent(CategoryEvent categoryEvent) {
    event.productCategory.EventProducer eventProducer = new event.productCategory.EventProducer();
    eventProducer.send(categoryEvent);
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
