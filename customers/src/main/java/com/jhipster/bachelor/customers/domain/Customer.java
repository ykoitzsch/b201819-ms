package com.jhipster.bachelor.customers.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Customer implements Serializable {

  private static final long serialVersionUID = 1L;

  public Customer() {
  }

  public Customer(String name, String email, String address) {
    this.name = name;
    this.email = email;
    this.address = address;
  }

  @Id
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "email")
  private String email;

  @Column(name = "address")
  private String address;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Customer name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public Customer email(String email) {
    this.email = email;
    return this;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public Customer address(String address) {
    this.address = address;
    return this;
  }

  public void setAddress(String address) {
    this.address = address;
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
    Customer customer = (Customer) o;
    if (customer.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), customer.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "Customer{" +
      "id=" +
      getId() +
      ", name='" +
      getName() +
      "'" +
      ", email='" +
      getEmail() +
      "'" +
      ", address='" +
      getAddress() +
      "'" +
      "}";
  }
}
