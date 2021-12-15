package com.integrown.web.rest;

import com.integrown.domain.ActivePairs;
import com.integrown.repository.ActivePairsRepository;
import com.integrown.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.reactive.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.integrown.domain.ActivePairs}.
 */
@RestController
@RequestMapping("/api")
public class ActivePairsResource {

    private final Logger log = LoggerFactory.getLogger(ActivePairsResource.class);

    private static final String ENTITY_NAME = "activePairs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivePairsRepository activePairsRepository;

    public ActivePairsResource(ActivePairsRepository activePairsRepository) {
        this.activePairsRepository = activePairsRepository;
    }

    /**
     * {@code POST  /active-pairs} : Create a new activePairs.
     *
     * @param activePairs the activePairs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activePairs, or with status {@code 400 (Bad Request)} if the activePairs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/active-pairs")
    public Mono<ResponseEntity<ActivePairs>> createActivePairs(@RequestBody ActivePairs activePairs) throws URISyntaxException {
        log.debug("REST request to save ActivePairs : {}", activePairs);
        if (activePairs.getId() != null) {
            throw new BadRequestAlertException("A new activePairs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return activePairsRepository.save(activePairs)            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/active-pairs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /active-pairs} : Updates an existing activePairs.
     *
     * @param activePairs the activePairs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activePairs,
     * or with status {@code 400 (Bad Request)} if the activePairs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activePairs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/active-pairs")
    public Mono<ResponseEntity<ActivePairs>> updateActivePairs(@RequestBody ActivePairs activePairs) throws URISyntaxException {
        log.debug("REST request to update ActivePairs : {}", activePairs);
        if (activePairs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        return activePairsRepository.save(activePairs)            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map(result -> ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                .body(result)
            );
    }

    /**
     * {@code GET  /active-pairs} : get all the activePairs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activePairs in body.
     */
    @GetMapping("/active-pairs")
    public Mono<List<ActivePairs>> getAllActivePairs() {
        log.debug("REST request to get all ActivePairs");
        return activePairsRepository.findAll().collectList();
    }

    /**
     * {@code GET  /active-pairs} : get all the activePairs as a stream.
     * @return the {@link Flux} of activePairs.
     */
    @GetMapping(value = "/active-pairs", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<ActivePairs> getAllActivePairsAsStream() {
        log.debug("REST request to get all ActivePairs as a stream");
        return activePairsRepository.findAll();
    }

    /**
     * {@code GET  /active-pairs/:id} : get the "id" activePairs.
     *
     * @param id the id of the activePairs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activePairs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/active-pairs/{id}")
    public Mono<ResponseEntity<ActivePairs>> getActivePairs(@PathVariable String id) {
        log.debug("REST request to get ActivePairs : {}", id);
        Mono<ActivePairs> activePairs = activePairsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(activePairs);
    }

    /**
     * {@code DELETE  /active-pairs/:id} : delete the "id" activePairs.
     *
     * @param id the id of the activePairs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/active-pairs/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteActivePairs(@PathVariable String id) {
        log.debug("REST request to delete ActivePairs : {}", id);
        return activePairsRepository.deleteById(id)            .map(result -> ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
        );
    }
}
