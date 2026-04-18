package ap1.AllaVamos.service;

import ap1.AllaVamos.model.ActivityScheduleModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActivityScheduleService {

    Flux<ActivityScheduleModel> findAll();

    Mono<ActivityScheduleModel> findById(String id);

    Mono<ActivityScheduleModel> create(ActivityScheduleModel schedule);

    // 🔥 FALTABAN ESTOS
    Mono<ActivityScheduleModel> update(String id, ActivityScheduleModel schedule);

    Mono<Void> delete(String id);

    Mono<Void> restore(String id);
}