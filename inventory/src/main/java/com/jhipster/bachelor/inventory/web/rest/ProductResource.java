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
import com.jhipster.bachelor.inventory.domain.Product;
import com.jhipster.bachelor.inventory.service.ProductService;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Product.
 */
@RestController
@RequestMapping("/api")
public class ProductResource {

  private final Logger log = LoggerFactory.getLogger(ProductResource.class);

  private ProductService productService;

  public ProductResource(ProductService productService) {
    this.productService = productService;
  }

  /**
   * GET /products : get all the products.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of products in body
   */
  @GetMapping("/products")
  @Timed
  public List<Product> getAllProducts() {
    log.debug("REST request to get all Products");
    return productService.aggregateProductEvents();
  }

  /**
   * GET /products/:id : get the "id" product.
   *
   * @param id the id of the product to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the product, or with status 404 (Not Found)
   */
  @GetMapping("/products/{id}")
  @Timed
  public ResponseEntity<Product> getProduct(@PathVariable Long id) {
    log.debug("REST request to get Product : {}", id);
    return ResponseUtil.wrapOrNotFound(getAllProducts().stream().filter(c -> c.getId().equals(id)).findFirst());

  }

}
