package ap1.AllaVamos.service.impl;

import ap1.AllaVamos.model.ActivityScheduleModel;
import ap1.AllaVamos.repository.ActivityScheduleRepository;
import ap1.AllaVamos.repository.ActivityRepository;
import ap1.AllaVamos.service.ActivityScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ActivityScheduleServiceImpl implements ActivityScheduleService {

    private final ActivityScheduleRepository repository;
    private final ActivityRepository activityRepository;

    // =========================
    // 🔍 LISTAR
    // =========================
    @Override
    public Flux<ActivityScheduleModel> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<ActivityScheduleModel> findById(String id) {
        return repository.findById(id);
    }

    // =========================
    // ➕ CREAR (CON DETALLE)
    // =========================
    @Override
    public Mono<ActivityScheduleModel> create(ActivityScheduleModel schedule) {

        schedule.setCreatedAt(LocalDateTime.now());
        schedule.setState(true);

        return activityRepository.findById(schedule.getActivityId())
                .switchIfEmpty(Mono.error(new RuntimeException("Actividad no encontrada")))
                .flatMap(activity -> {

                    Map<String, Object> details = new HashMap<>();
                    details.put("name", activity.getName());
                    details.put("description", activity.getDescription());
                    details.put("durationHours", activity.getDurationHours());
                    details.put("price", activity.getPrice());
                    details.put("location", activity.getLocation());
                    details.put("difficulty", activity.getDifficulty());

                    schedule.setDetails(details);

                    return repository.save(schedule);
                });
    }

    // =========================
    // ✏️ EDITAR (PUT)
    // =========================
    @Override
    public Mono<ActivityScheduleModel> update(String id, ActivityScheduleModel schedule) {

        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Horario no encontrado")))
                .flatMap(existing ->
                        activityRepository.findById(schedule.getActivityId())
                                .switchIfEmpty(Mono.error(new RuntimeException("Actividad no encontrada")))
                                .flatMap(activity -> {

                                    Map<String, Object> details = new HashMap<>();
                                    details.put("name", activity.getName());
                                    details.put("description", activity.getDescription());
                                    details.put("durationHours", activity.getDurationHours());
                                    details.put("price", activity.getPrice());
                                    details.put("location", activity.getLocation());
                                    details.put("difficulty", activity.getDifficulty());

                                    existing.setActivityId(schedule.getActivityId());
                                    existing.setGuideId(schedule.getGuideId());
                                    existing.setStartDateTime(schedule.getStartDateTime());
                                    existing.setEndDateTime(schedule.getEndDateTime());
                                    existing.setDetails(details);

                                    existing.setUpdatedAt(LocalDateTime.now());

                                    return repository.save(existing);
                                })
                );
    }

    // =========================
    // ❌ ELIMINADO LÓGICO
    // =========================
    @Override
    public Mono<Void> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Horario no encontrado")))
                .flatMap(existing -> {
                    existing.setState(false);
                    existing.setDeletedAt(LocalDateTime.now());
                    return repository.save(existing);
                }).then();
    }

    // =========================
    // ♻️ RESTAURAR
    // =========================
    @Override
    public Mono<Void> restore(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Horario no encontrado")))
                .flatMap(existing -> {
                    existing.setState(true);
                    existing.setDeletedAt(null);
                    return repository.save(existing);
                }).then();
    }
}