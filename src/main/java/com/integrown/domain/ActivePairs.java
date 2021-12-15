package com.integrown.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * A ActivePairs.
 */
@Document(collection = "active_pairs")
public class ActivePairs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("exchange_id")
    private String exchangeId;

    @Field("email")
    private String email;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public ActivePairs exchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
        return this;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getEmail() {
        return email;
    }

    public ActivePairs email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivePairs)) {
            return false;
        }
        return id != null && id.equals(((ActivePairs) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActivePairs{" +
            "id=" + getId() +
            ", exchangeId='" + getExchangeId() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
