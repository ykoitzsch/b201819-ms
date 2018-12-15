package com.jhipster.bachelor.invoices.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.jhipster.bachelor.invoices.domain.enumeration.InvoiceStatus;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvoiceStatus status;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "payment_date")
    private String paymentDate;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "order_id")
    private Long orderId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Invoice code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public Invoice status(InvoiceStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public String getDueDate() {
        return dueDate;
    }

    public Invoice dueDate(String dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public Invoice paymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getAmount() {
        return amount;
    }

    public Invoice amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Invoice customerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Invoice orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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
        Invoice invoice = (Invoice) o;
        if (invoice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", status='" + getStatus() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", amount=" + getAmount() +
            ", customerId=" + getCustomerId() +
            ", orderId=" + getOrderId() +
            "}";
    }
}
