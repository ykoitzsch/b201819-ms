package com.jhipster.bachelor.orders.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ProductOrder.
 */
@Entity
@Table(name = "product_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JsonIgnoreProperties("productOrders")
    private CompleteOrder completeOrder;

    @ManyToOne
    @JsonIgnoreProperties("productOrders")
    private Basket basket;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public ProductOrder amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public ProductOrder customerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public ProductOrder productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public CompleteOrder getCompleteOrder() {
        return completeOrder;
    }

    public ProductOrder completeOrder(CompleteOrder completeOrder) {
        this.completeOrder = completeOrder;
        return this;
    }

    public void setCompleteOrder(CompleteOrder completeOrder) {
        this.completeOrder = completeOrder;
    }

    public Basket getBasket() {
        return basket;
    }

    public ProductOrder basket(Basket basket) {
        this.basket = basket;
        return this;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductOrder productOrder = (ProductOrder) o;
        if (productOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductOrder{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", customerId=" + getCustomerId() +
            ", productId=" + getProductId() +
            "}";
    }
}
