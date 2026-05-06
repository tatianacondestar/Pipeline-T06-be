package ap1.AllaVamos.rest;

import ap1.AllaVamos.model.ActivityScheduleModel;
import ap1.AllaVamos.service.ActivityScheduleService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@Tag(name = "Activity Schedule",
     description = "Transaccional de programación de actividades (JSON mínimo permitido)")
@RestController
@RequestMapping("/api/activity-schedules")
@RequiredArgsConstructor
public class ActivityScheduleRest {

    private final ActivityScheduleService service;

    // =========================
    // 🔍 LISTAR
    // =========================
    @Operation(summary = "Listar horarios")
    @GetMapping
    public Flux<ActivityScheduleModel> getAll() {
        return service.findAll();
    }

    // =========================
    // 🔍 BUSCAR POR ID
    // =========================
    @Operation(summary = "Buscar por ID")
    @GetMapping("/{id}")
    public Mono<ActivityScheduleModel> getById(@PathVariable String id) {
        return service.findById(id);
    }

    // =========================
    // ➕ CREAR
    // =========================
    @Operation(
        summary = "Crear horario",
        description = "⚠️ SOLO ENVÍA activityId, guideId y date. El backend completa lo demás automáticamente",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                examples = @ExampleObject(
                    name = "Ejemplo mínimo",
                    value = """
                    {
                      "activityId": "ID_DE_ACTIVIDAD",
                      "guideId": "ID_DEL_GUIA",
                      "date": "2026-05-06"
                    }
                    """
                )
            )
        )
    )
    @PostMapping
    public Mono<ActivityScheduleModel> create(@org.springframework.web.bind.annotation.RequestBody ActivityScheduleModel schedule) {
        return service.create(schedule);
    }

    // =========================
    // ✏️ EDITAR
    // =========================
    @Operation(summary = "Editar horario")
    @PutMapping("/{id}")
    public Mono<ActivityScheduleModel> update(@PathVariable String id,
                                              @org.springframework.web.bind.annotation.RequestBody ActivityScheduleModel schedule) {
        return service.update(id, schedule);
    }

    // =========================
    // ❌ ELIMINADO LÓGICO
    // =========================
    @Operation(summary = "Eliminar lógico")
    @PatchMapping("/delete/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return service.delete(id);
    }

    // =========================
    // ♻️ RESTAURAR
    // =========================
    @Operation(summary = "Restaurar horario")
    @PatchMapping("/restore/{id}")
    public Mono<Void> restore(@PathVariable String id) {
        return service.restore(id);
    }
}