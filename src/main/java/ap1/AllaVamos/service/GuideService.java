package ap1.AllaVamos.service;

import ap1.AllaVamos.model.GuideModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GuideService {

    Flux<GuideModel> findAll();

    Mono<GuideModel> findById(String id);

    Mono<GuideModel> create(GuideModel guide);

    Mono<GuideModel> update(String id, GuideModel guide);

    Mono<Void> delete(String id);

    Mono<Void> restore(String id);
}