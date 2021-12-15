package com.integrown.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Employee.
 */
@Document(collection = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("email")
    private String email;

    @Field("phone_number")
    private String phoneNumber;

    @Field("hire_date")
    private Instant hireDate;

    @Field("salary")
    private Long salary;

    @Field("commission_pct")
    private Long commissionPct;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Employee firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Employee lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public Employee email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Employee phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getHireDate() {
        return hireDate;
    }

    public Employee hireDate(Instant hireDate) {
        this.hireDate = hireDate;
        return this;
    }

    public void setHireDate(Instant hireDate) {
        this.hireDate = hireDate;
    }

    public Long getSalary() {
        return salary;
    }

    public Employee salary(Long salary) {
        this.salary = salary;
        return this;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Long getCommissionPct() {
        return commissionPct;
    }

    public Employee commissionPct(Long commissionPct) {
        this.commissionPct = commissionPct;
        return this;
    }

    public void setCommissionPct(Long commissionPct) {
        this.commissionPct = commissionPct;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", hireDate='" + getHireDate() + "'" +
            ", salary=" + getSalary() +
            ", commissionPct=" + getCommissionPct() +
            "}";
    }
}
