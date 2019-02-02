package com.jhipster.bachelor.inventory.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.inventory.domain.ProductCategory;
import com.jhipster.bachelor.inventory.service.ProductCategoryService;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing ProductCategory.
 */
@RestController
@RequestMapping("/api")
public class ProductCategoryResource {

  private final Logger log = LoggerFactory.getLogger(ProductCategoryResource.class);

  private ProductCategoryService productCategoryService;

  public ProductCategoryResource(ProductCategoryService productCategoryService) {
    this.productCategoryService = productCategoryService;
  }

  /**
   * GET /product-categories : get all the productCategories.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of productCategories in body
   */
  @GetMapping("/product-categories")
  @Timed
  public List<ProductCategory> getAllProductCategories() {
    log.debug("REST request to get all ProductCategories");
    return productCategoryService.aggregateCategoryEvents();
  }

  /**
   * GET /product-categories/:id : get the "id" productCategory.
   *
   * @param id the id of the productCategory to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the productCategory, or with status 404 (Not Found)
   */
  @GetMapping("/product-categories/{id}")
  @Timed
  public ResponseEntity<ProductCategory> getProductCategory(@PathVariable Long id) {
    log.debug("REST request to get ProductCategory : {}", id);
    return ResponseUtil.wrapOrNotFound(getAllProductCategories().stream().filter(c -> c.getId().equals(id)).findFirst());

  }
}
