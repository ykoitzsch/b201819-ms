package com.jhipster.bachelor.inventory.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.inventory.domain.Product;
import com.jhipster.bachelor.inventory.service.ProductService;
import com.jhipster.bachelor.inventory.web.rest.errors.BadRequestAlertException;
import com.jhipster.bachelor.inventory.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Product.
 */
@RestController
@RequestMapping("/api")
public class ProductResource {

  private final Logger log = LoggerFactory.getLogger(ProductResource.class);

  private static final String ENTITY_NAME = "inventoryProduct";

  private ProductService productService;

  public ProductResource(ProductService productService) {
    this.productService = productService;
  }

  /**
   * POST /products : Create a new product.
   *
   * @param product the product to create
   * @return the ResponseEntity with status 201 (Created) and with body the new product, or with status 400 (Bad
   *         Request) if the product has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/products")
  @Timed
  public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) throws URISyntaxException {
    log.debug("REST request to save Product : {}", product);
    if (product.getId() != null) {
      throw new BadRequestAlertException("A new product cannot already have an ID", ENTITY_NAME, "idexists");
    }
    Product result = productService.save(product);
    return ResponseEntity
      .created(new URI("/api/products/" + result.getId()))
      .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
      .body(result);
  }

  /**
   * PUT /products : Updates an existing product.
   *
   * @param product the product to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated product, or with status 400 (Bad Request)
   *         if the product is not valid, or with status 500 (Internal Server Error) if the product couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/products")
  @Timed
  public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product) throws URISyntaxException {
    log.debug("REST request to update Product : {}", product);
    if (product.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    Product result = productService.save(product);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, product.getId().toString())).body(result);
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

  /**
   * DELETE /products/:id : delete the "id" product.
   *
   * @param id the id of the product to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/products/{id}")
  @Timed
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    log.debug("REST request to delete Product : {}", id);
    productService.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }
}
