package com.integrown.web.rest;

import com.integrown.StackoverflowApp;
import com.integrown.domain.ActivePairs;
import com.integrown.repository.ActivePairsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for the {@link ActivePairsResource} REST controller.
 */
@SpringBootTest(classes = StackoverflowApp.class)
@AutoConfigureWebTestClient
@WithMockUser
public class ActivePairsResourceIT {

    private static final String DEFAULT_EXCHANGE_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXCHANGE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private ActivePairsRepository activePairsRepository;

    @Autowired
    private WebTestClient webTestClient;

    private ActivePairs activePairs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivePairs createEntity() {
        ActivePairs activePairs = new ActivePairs()
            .exchangeId(DEFAULT_EXCHANGE_ID)
            .email(DEFAULT_EMAIL);
        return activePairs;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivePairs createUpdatedEntity() {
        ActivePairs activePairs = new ActivePairs()
            .exchangeId(UPDATED_EXCHANGE_ID)
            .email(UPDATED_EMAIL);
        return activePairs;
    }

    @BeforeEach
    public void initTest() {
        activePairsRepository.deleteAll().block();
        activePairs = createEntity();
    }

    @Test
    public void createActivePairs() throws Exception {
        int databaseSizeBeforeCreate = activePairsRepository.findAll().collectList().block().size();
        // Create the ActivePairs
        webTestClient.post().uri("/api/active-pairs")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activePairs))
            .exchange()
            .expectStatus().isCreated();

        // Validate the ActivePairs in the database
        List<ActivePairs> activePairsList = activePairsRepository.findAll().collectList().block();
        assertThat(activePairsList).hasSize(databaseSizeBeforeCreate + 1);
        ActivePairs testActivePairs = activePairsList.get(activePairsList.size() - 1);
        assertThat(testActivePairs.getExchangeId()).isEqualTo(DEFAULT_EXCHANGE_ID);
        assertThat(testActivePairs.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    public void createActivePairsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activePairsRepository.findAll().collectList().block().size();

        // Create the ActivePairs with an existing ID
        activePairs.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient.post().uri("/api/active-pairs")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activePairs))
            .exchange()
            .expectStatus().isBadRequest();

        // Validate the ActivePairs in the database
        List<ActivePairs> activePairsList = activePairsRepository.findAll().collectList().block();
        assertThat(activePairsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllActivePairsAsStream() {
        // Initialize the database
        activePairsRepository.save(activePairs).block();

        List<ActivePairs> activePairsList = webTestClient.get().uri("/api/active-pairs")
            .accept(MediaType.APPLICATION_STREAM_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_STREAM_JSON)
            .returnResult(ActivePairs.class)
            .getResponseBody()
            .filter(activePairs::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(activePairsList).isNotNull();
        assertThat(activePairsList).hasSize(1);
        ActivePairs testActivePairs = activePairsList.get(0);
        assertThat(testActivePairs.getExchangeId()).isEqualTo(DEFAULT_EXCHANGE_ID);
        assertThat(testActivePairs.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    public void getAllActivePairs() {
        // Initialize the database
        activePairsRepository.save(activePairs).block();

        // Get all the activePairsList
        webTestClient.get().uri("/api/active-pairs?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id").value(hasItem(activePairs.getId()))
            .jsonPath("$.[*].exchangeId").value(hasItem(DEFAULT_EXCHANGE_ID))
            .jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL));
    }
    
    @Test
    public void getActivePairs() {
        // Initialize the database
        activePairsRepository.save(activePairs).block();

        // Get the activePairs
        webTestClient.get().uri("/api/active-pairs/{id}", activePairs.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").value(is(activePairs.getId()))
            .jsonPath("$.exchangeId").value(is(DEFAULT_EXCHANGE_ID))
            .jsonPath("$.email").value(is(DEFAULT_EMAIL));
    }
    @Test
    public void getNonExistingActivePairs() {
        // Get the activePairs
        webTestClient.get().uri("/api/active-pairs/{id}", Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    public void updateActivePairs() throws Exception {
        // Initialize the database
        activePairsRepository.save(activePairs).block();

        int databaseSizeBeforeUpdate = activePairsRepository.findAll().collectList().block().size();

        // Update the activePairs
        ActivePairs updatedActivePairs = activePairsRepository.findById(activePairs.getId()).block();
        updatedActivePairs
            .exchangeId(UPDATED_EXCHANGE_ID)
            .email(UPDATED_EMAIL);

        webTestClient.put().uri("/api/active-pairs")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedActivePairs))
            .exchange()
            .expectStatus().isOk();

        // Validate the ActivePairs in the database
        List<ActivePairs> activePairsList = activePairsRepository.findAll().collectList().block();
        assertThat(activePairsList).hasSize(databaseSizeBeforeUpdate);
        ActivePairs testActivePairs = activePairsList.get(activePairsList.size() - 1);
        assertThat(testActivePairs.getExchangeId()).isEqualTo(UPDATED_EXCHANGE_ID);
        assertThat(testActivePairs.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    public void updateNonExistingActivePairs() throws Exception {
        int databaseSizeBeforeUpdate = activePairsRepository.findAll().collectList().block().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient.put().uri("/api/active-pairs")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activePairs))
            .exchange()
            .expectStatus().isBadRequest();

        // Validate the ActivePairs in the database
        List<ActivePairs> activePairsList = activePairsRepository.findAll().collectList().block();
        assertThat(activePairsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteActivePairs() {
        // Initialize the database
        activePairsRepository.save(activePairs).block();

        int databaseSizeBeforeDelete = activePairsRepository.findAll().collectList().block().size();

        // Delete the activePairs
        webTestClient.delete().uri("/api/active-pairs/{id}", activePairs.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent();

        // Validate the database contains one less item
        List<ActivePairs> activePairsList = activePairsRepository.findAll().collectList().block();
        assertThat(activePairsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
