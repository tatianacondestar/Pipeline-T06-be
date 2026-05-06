package ap1.AllaVamos.service.impl;

import ap1.AllaVamos.model.ActivityScheduleModel;
import ap1.AllaVamos.repository.ActivityScheduleRepository;
import ap1.AllaVamos.repository.ActivityRepository;
import ap1.AllaVamos.repository.GuideRepository;
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
    private final GuideRepository guideRepository;

    // =========================
    // 🔍 LISTAR SOLO ACTIVOS
    // =========================
    @Override
    public Flux<ActivityScheduleModel> findAll() {
        return repository.findAll()
                .filter(s -> s.getDeletedAt() == null);
    }

    @Override
    public Mono<ActivityScheduleModel> findById(String id) {
        return repository.findById(id)
                .filter(s -> s.getDeletedAt() == null)
                .switchIfEmpty(Mono.error(new RuntimeException("Horario no encontrado")));
    }

    // =========================
    // ➕ CREAR
    // =========================
    @Override
    public Mono<ActivityScheduleModel> create(ActivityScheduleModel schedule) {

        if (schedule.getActivityId() == null ||
            schedule.getGuideId() == null ||
            schedule.getDate() == null ||
            schedule.getStartTime() == null ||
            schedule.getEndTime() == null) {

            return Mono.error(new RuntimeException("Campos obligatorios incompletos"));
        }

        if (!schedule.getStartTime().isBefore(schedule.getEndTime())) {
            return Mono.error(new RuntimeException(
                    "La hora de inicio debe ser menor que la hora de fin"));
        }

        return repository.existsByActivityIdAndDateAndStartTimeAndEndTimeAndDeletedAtIsNull(
                schedule.getActivityId(),
                schedule.getDate(),
                schedule.getStartTime(),
                schedule.getEndTime()
        )
        .flatMap(exists -> {

            if (exists) {
                return Mono.error(new RuntimeException("Horario duplicado"));
            }

            return repository.findByGuideIdAndDateAndDeletedAtIsNull(
                    schedule.getGuideId(),
                    schedule.getDate()
            )
            .filter(existing ->
                    schedule.getStartTime().isBefore(existing.getEndTime()) &&
                    schedule.getEndTime().isAfter(existing.getStartTime())
            )
            .hasElements()
            .flatMap(overlap -> {

                if (overlap) {
                    return Mono.error(new RuntimeException(
                            "El guía ya tiene un horario en ese rango"));
                }

                return buildDetails(schedule)
                        .flatMap(details -> {

                            schedule.setDetails(details);
                            schedule.setCreatedAt(LocalDateTime.now());
                            schedule.setDeletedAt(null);
                            schedule.setRestoredAt(null);

                            return repository.save(schedule);
                        });
            });
        });
    }

    // =========================
    // ✏️ UPDATE
    // =========================
    @Override
    public Mono<ActivityScheduleModel> update(String id, ActivityScheduleModel schedule) {

        if (!schedule.getStartTime().isBefore(schedule.getEndTime())) {
            return Mono.error(new RuntimeException(
                    "La hora de inicio debe ser menor que la hora de fin"));
        }

        return repository.findById(id)
                .filter(s -> s.getDeletedAt() == null)
                .switchIfEmpty(Mono.error(new RuntimeException("Horario no encontrado")))
                .flatMap(existing ->

                        repository.findByGuideIdAndDateAndDeletedAtIsNull(
                                schedule.getGuideId(),
                                schedule.getDate()
                        )
                        .filter(other ->
                                !other.getId().equals(id) &&
                                schedule.getStartTime().isBefore(other.getEndTime()) &&
                                schedule.getEndTime().isAfter(other.getStartTime())
                        )
                        .hasElements()
                        .flatMap(overlap -> {

                            if (overlap) {
                                return Mono.error(new RuntimeException(
                                        "El guía ya tiene un horario en ese rango"));
                            }

                            return buildDetails(schedule)
                                    .flatMap(details -> {

                                        existing.setActivityId(schedule.getActivityId());
                                        existing.setGuideId(schedule.getGuideId());
                                        existing.setDate(schedule.getDate());
                                        existing.setStartTime(schedule.getStartTime());
                                        existing.setEndTime(schedule.getEndTime());
                                        existing.setDetails(details);
                                        existing.setUpdatedAt(LocalDateTime.now());

                                        return repository.save(existing);
                                    });
                        })
                );
    }

    // =========================
    // 🔧 MÉTODO REUTILIZABLE
    // =========================
    private Mono<Map<String, Object>> buildDetails(ActivityScheduleModel schedule) {

        return activityRepository.findById(schedule.getActivityId())
                .switchIfEmpty(Mono.error(new RuntimeException("Actividad no existe")))
                .zipWith(
                        guideRepository.findById(schedule.getGuideId())
                                .switchIfEmpty(Mono.error(new RuntimeException("Guía no existe")))
                )
                .map(tuple -> {

                    var activity = tuple.getT1();
                    var guide = tuple.getT2();

                    Map<String, Object> details = new HashMap<>();

                    // Activity
                    details.put("name", activity.getName());
                    details.put("description", activity.getDescription());
                    details.put("durationHours", activity.getDurationHours());
                    details.put("price", activity.getPrice());
                    details.put("location", activity.getLocation());
                    details.put("difficulty", activity.getDifficulty());
                    details.put("maxQuota", activity.getMaxQuota());

                    // Guide
                    details.put("guideName", guide.getName());
                    details.put("guideEmail", guide.getEmail());
                    details.put("guidePhone", guide.getPhone());

                    return details;
                });
    }

    // =========================
    // ❌ DELETE LÓGICO
    // =========================
    @Override
    public Mono<Void> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Horario no encontrado")))
                .flatMap(existing -> {
                    existing.setDeletedAt(LocalDateTime.now());
                    return repository.save(existing);
                }).then();
    }

    // =========================
    // ♻️ RESTORE
    // =========================
    @Override
    public Mono<Void> restore(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Horario no encontrado")))
                .flatMap(existing -> {
                    existing.setDeletedAt(null);
                    existing.setRestoredAt(LocalDateTime.now());
                    return repository.save(existing);
                }).then();
    }
}