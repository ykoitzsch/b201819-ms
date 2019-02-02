package com.jhipster.bachelor.inventory.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.inventory.domain.Product;
import com.jhipster.bachelor.inventory.domain.ProductCategory;
import com.jhipster.bachelor.inventory.service.ProductService;

import event.product.ProductEvent;
import event.productCategory.CategoryEvent;

@RestController
@RequestMapping("/api")
public class EventResource {

  private ProductService productService;

  public EventResource(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping("/product-events/{event}")
  @Timed
  public ResponseEntity<Object> addCustomerEvent(@RequestBody Product product, @PathVariable("event") String event) throws Exception {
    if ("PRODUCT_DELETED".equals(event) || "PRODUCT_UPDATED".equals(event) || "PRODUCT_CREATED".equals(event)) {
      productService.addProductEvent(new ProductEvent(product, event));
    } else
      throw new Exception("Unknown Event");
    return new ResponseEntity<>(HttpStatus.OK);

  }

  @PostMapping("/category-events/{event}")
  @Timed
  public ResponseEntity<Object> addCustomerEvent(@RequestBody ProductCategory category, @PathVariable("event") String event)
      throws Exception {
    if ("CATEGORY_DELETED".equals(event) || "CATEGORY_UPDATED".equals(event) || "CATEGORY_CREATED".equals(event)) {
      productService.addCategoryEvent(new CategoryEvent(category, event));
    } else
      throw new Exception("Unknown Event");
    return new ResponseEntity<>(HttpStatus.OK);

  }

}
