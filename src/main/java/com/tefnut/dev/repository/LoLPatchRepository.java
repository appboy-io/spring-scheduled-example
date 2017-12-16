package com.tefnut.dev.repository;

import com.tefnut.dev.models.Patch;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface LoLPatchRepository extends ReactiveMongoRepository<Patch, Long> {
    Flux<Patch> findByGameName(String name);
    Flux<Patch> findByGameNameOrderByIdDesc(String name);
}
