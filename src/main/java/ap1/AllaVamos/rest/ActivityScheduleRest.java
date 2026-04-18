package ap1.AllaVamos.rest;

import ap1.AllaVamos.model.ActivityScheduleModel;
import ap1.AllaVamos.service.ActivityScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Activity Schedule",
     description = "Transaccional de programación de actividades con cabecera + detalle (MongoDB)")
@RestController
@RequestMapping("/api/activity-schedules")
@RequiredArgsConstructor
public class ActivityScheduleRest {

    private final ActivityScheduleService service;

    // =========================
    // 🔍 LISTAR
    // =========================
    @Operation(summary = "Listar horarios",
               description = "Obtiene todos los horarios con cabecera y detalle")
    @GetMapping
    public Flux<ActivityScheduleModel> getAll() {
        return service.findAll();
    }

    @Operation(summary = "Buscar por ID",
               description = "Obtiene un horario específico con su detalle")
    @GetMapping("/{id}")
    public Mono<ActivityScheduleModel> getById(@PathVariable String id) {
        return service.findById(id);
    }

    // =========================
    // ➕ CREAR
    // =========================
    @Operation(summary = "Crear horario",
               description = "Crea un horario. El detalle se genera automáticamente desde activities")
    @PostMapping
    public Mono<ActivityScheduleModel> create(@RequestBody ActivityScheduleModel schedule) {
        return service.create(schedule);
    }

    // =========================
    // ✏️ EDITAR
    // =========================
    @Operation(summary = "Editar horario",
               description = "Actualiza cabecera y regenera el detalle desde activities")
    @PutMapping("/{id}")
    public Mono<ActivityScheduleModel> update(@PathVariable String id,
                                              @RequestBody ActivityScheduleModel schedule) {
        return service.update(id, schedule);
    }

    // =========================
    // ❌ ELIMINADO LÓGICO
    // =========================
    @Operation(summary = "Eliminar lógico",
               description = "Cambia state=false y registra deletedAt")
    @PatchMapping("/delete/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return service.delete(id);
    }

    // =========================
    // ♻️ RESTAURAR
    // =========================
    @Operation(summary = "Restaurar horario",
               description = "Cambia state=true y elimina deletedAt")
    @PatchMapping("/restore/{id}")
    public Mono<Void> restore(@PathVariable String id) {
        return service.restore(id);
    }
}