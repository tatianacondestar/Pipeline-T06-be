package ap1.AllaVamos.repository;

import ap1.AllaVamos.model.ActivityScheduleModel;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Repositorio reactivo para ActivitySchedule
 *
 * 🔹 Maneja soft delete (deletedAt)
 * 🔹 Todas las consultas consideran solo registros activos
 */
@Repository
public interface ActivityScheduleRepository extends ReactiveMongoRepository<ActivityScheduleModel, String> {

    // =========================
    // 🔍 VALIDAR SI GUÍA ESTÁ EN USO
    // =========================
    /**
     * Verifica si un guía tiene horarios activos
     */
    Mono<Boolean> existsByGuideIdAndDeletedAtIsNull(String guideId);

    // =========================
    // 🔍 VALIDAR DUPLICADO EXACTO
    // =========================
    /**
     * Evita crear horarios exactamente iguales (misma actividad, fecha y hora)
     */
    Mono<Boolean> existsByActivityIdAndDateAndStartTimeAndEndTimeAndDeletedAtIsNull(
            String activityId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    );

    // =========================
    // 🔍 HORARIOS POR GUÍA Y FECHA
    // =========================
    /**
     * Obtiene horarios activos de un guía en un día específico
     */
    Flux<ActivityScheduleModel> findByGuideIdAndDateAndDeletedAtIsNull(
            String guideId,
            LocalDate date
    );

    // =========================
    // 🔍 HORARIOS POR ACTIVIDAD Y FECHA
    // =========================
    /**
     * Obtiene horarios activos de una actividad en un día específico
     */
    Flux<ActivityScheduleModel> findByActivityIdAndDateAndDeletedAtIsNull(
            String activityId,
            LocalDate date
    );
}