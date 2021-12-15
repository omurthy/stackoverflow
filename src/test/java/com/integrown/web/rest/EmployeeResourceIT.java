package com.integrown.web.rest;

import com.integrown.StackoverflowApp;
import com.integrown.domain.Employee;
import com.integrown.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@SpringBootTest(classes = StackoverflowApp.class)
@AutoConfigureWebTestClient
@WithMockUser
public class EmployeeResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_HIRE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HIRE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_SALARY = 1L;
    private static final Long UPDATED_SALARY = 2L;

    private static final Long DEFAULT_COMMISSION_PCT = 1L;
    private static final Long UPDATED_COMMISSION_PCT = 2L;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private WebTestClient webTestClient;

    private Employee employee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity() {
        Employee employee = new Employee()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .hireDate(DEFAULT_HIRE_DATE)
            .salary(DEFAULT_SALARY)
            .commissionPct(DEFAULT_COMMISSION_PCT);
        return employee;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity() {
        Employee employee = new Employee()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .hireDate(UPDATED_HIRE_DATE)
            .salary(UPDATED_SALARY)
            .commissionPct(UPDATED_COMMISSION_PCT);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employeeRepository.deleteAll().block();
        employee = createEntity();
    }

    @Test
    public void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().collectList().block().size();
        // Create the Employee
        webTestClient.post().uri("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus().isCreated();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployee.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testEmployee.getHireDate()).isEqualTo(DEFAULT_HIRE_DATE);
        assertThat(testEmployee.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testEmployee.getCommissionPct()).isEqualTo(DEFAULT_COMMISSION_PCT);
    }

    @Test
    public void createEmployeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().collectList().block().size();

        // Create the Employee with an existing ID
        employee.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient.post().uri("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus().isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllEmployeesAsStream() {
        // Initialize the database
        employeeRepository.save(employee).block();

        List<Employee> employeeList = webTestClient.get().uri("/api/employees")
            .accept(MediaType.APPLICATION_STREAM_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_STREAM_JSON)
            .returnResult(Employee.class)
            .getResponseBody()
            .filter(employee::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(employeeList).isNotNull();
        assertThat(employeeList).hasSize(1);
        Employee testEmployee = employeeList.get(0);
        assertThat(testEmployee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployee.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testEmployee.getHireDate()).isEqualTo(DEFAULT_HIRE_DATE);
        assertThat(testEmployee.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testEmployee.getCommissionPct()).isEqualTo(DEFAULT_COMMISSION_PCT);
    }

    @Test
    public void getAllEmployees() {
        // Initialize the database
        employeeRepository.save(employee).block();

        // Get all the employeeList
        webTestClient.get().uri("/api/employees?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id").value(hasItem(employee.getId()))
            .jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME))
            .jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME))
            .jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString()))
            .jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.intValue()))
            .jsonPath("$.[*].commissionPct").value(hasItem(DEFAULT_COMMISSION_PCT.intValue()));
    }
    
    @Test
    public void getEmployee() {
        // Initialize the database
        employeeRepository.save(employee).block();

        // Get the employee
        webTestClient.get().uri("/api/employees/{id}", employee.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").value(is(employee.getId()))
            .jsonPath("$.firstName").value(is(DEFAULT_FIRST_NAME))
            .jsonPath("$.lastName").value(is(DEFAULT_LAST_NAME))
            .jsonPath("$.email").value(is(DEFAULT_EMAIL))
            .jsonPath("$.phoneNumber").value(is(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.hireDate").value(is(DEFAULT_HIRE_DATE.toString()))
            .jsonPath("$.salary").value(is(DEFAULT_SALARY.intValue()))
            .jsonPath("$.commissionPct").value(is(DEFAULT_COMMISSION_PCT.intValue()));
    }
    @Test
    public void getNonExistingEmployee() {
        // Get the employee
        webTestClient.get().uri("/api/employees/{id}", Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    public void updateEmployee() throws Exception {
        // Initialize the database
        employeeRepository.save(employee).block();

        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).block();
        updatedEmployee
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .hireDate(UPDATED_HIRE_DATE)
            .salary(UPDATED_SALARY)
            .commissionPct(UPDATED_COMMISSION_PCT);

        webTestClient.put().uri("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedEmployee))
            .exchange()
            .expectStatus().isOk();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmployee.getHireDate()).isEqualTo(UPDATED_HIRE_DATE);
        assertThat(testEmployee.getSalary()).isEqualTo(UPDATED_SALARY);
        assertThat(testEmployee.getCommissionPct()).isEqualTo(UPDATED_COMMISSION_PCT);
    }

    @Test
    public void updateNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient.put().uri("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employee))
            .exchange()
            .expectStatus().isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteEmployee() {
        // Initialize the database
        employeeRepository.save(employee).block();

        int databaseSizeBeforeDelete = employeeRepository.findAll().collectList().block().size();

        // Delete the employee
        webTestClient.delete().uri("/api/employees/{id}", employee.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent();

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
