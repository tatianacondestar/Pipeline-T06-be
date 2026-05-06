package ap1.AllaVamos.service;

import ap1.AllaVamos.model.ActivityScheduleModel;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Servicio de programación de actividades (transaccional).
 *
 * 🔹 Maneja cabecera + detalle (details)
 * 🔹 Soporta creación con JSON mínimo
 * 🔹 Implementa eliminado lógico (soft delete)
 */
public interface ActivityScheduleService {

    // =========================
    // 🔍 LISTAR
    // =========================
    /**
     * Lista todos los horarios activos (no eliminados)
     */
    Flux<ActivityScheduleModel> findAll();

    // =========================
    // 🔍 BUSCAR POR ID
    // =========================
    /**
     * Obtiene un horario por ID
     */
    Mono<ActivityScheduleModel> findById(String id);

    // =========================
    // ➕ CREAR
    // =========================
    /**
     * Crea un horario.
     *
     * 🔥 Solo requiere:
     * - activityId
     * - guideId
     * - date
     *
     * El sistema completa automáticamente:
     * - startTime / endTime
     * - details (activity + guide)
     * - auditoría
     */
    Mono<ActivityScheduleModel> create(ActivityScheduleModel schedule);

    // =========================
    // ✏️ ACTUALIZAR
    // =========================
    /**
     * Actualiza un horario existente
     */
    Mono<ActivityScheduleModel> update(String id, ActivityScheduleModel schedule);

    // =========================
    // ❌ ELIMINADO LÓGICO
    // =========================
    /**
     * Elimina lógicamente un horario (deletedAt)
     */
    Mono<Void> delete(String id);

    // =========================
    // ♻️ RESTAURAR
    // =========================
    /**
     * Restaura un horario eliminado
     */
    Mono<Void> restore(String id);
}