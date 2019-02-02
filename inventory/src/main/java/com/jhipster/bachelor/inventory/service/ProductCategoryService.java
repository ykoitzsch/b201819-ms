package com.jhipster.bachelor.inventory.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhipster.bachelor.inventory.domain.ProductCategory;

import event.productCategory.CategoryEvent;

/**
 * Service Implementation for managing ProductCategory.
 */
@Service
@Transactional
public class ProductCategoryService {

  private final Logger log = LoggerFactory.getLogger(ProductCategoryService.class);

  public ProductCategoryService() {
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
