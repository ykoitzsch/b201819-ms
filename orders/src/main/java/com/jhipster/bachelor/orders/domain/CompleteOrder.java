package com.jhipster.bachelor.orders.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.jhipster.bachelor.orders.domain.enumeration.OrderStatus;

/**
 * A CompleteOrder.
 */
@Entity
@Table(name = "complete_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CompleteOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_id")
    private Long invoiceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "order_date")
    private String orderDate;

    @OneToMany(mappedBy = "completeOrder")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProductOrder> productOrders = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public CompleteOrder invoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
        return this;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public CompleteOrder status(OrderStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public CompleteOrder customerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public CompleteOrder totalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public CompleteOrder orderDate(String orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Set<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public CompleteOrder productOrders(Set<ProductOrder> productOrders) {
        this.productOrders = productOrders;
        return this;
    }

    public CompleteOrder addProductOrder(ProductOrder productOrder) {
        this.productOrders.add(productOrder);
        productOrder.setCompleteOrder(this);
        return this;
    }

    public CompleteOrder removeProductOrder(ProductOrder productOrder) {
        this.productOrders.remove(productOrder);
        productOrder.setCompleteOrder(null);
        return this;
    }

    public void setProductOrders(Set<ProductOrder> productOrders) {
        this.productOrders = productOrders;
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
        CompleteOrder completeOrder = (CompleteOrder) o;
        if (completeOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), completeOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompleteOrder{" +
            "id=" + getId() +
            ", invoiceId=" + getInvoiceId() +
            ", status='" + getStatus() + "'" +
            ", customerId=" + getCustomerId() +
            ", totalPrice=" + getTotalPrice() +
            ", orderDate='" + getOrderDate() + "'" +
            "}";
    }
}
