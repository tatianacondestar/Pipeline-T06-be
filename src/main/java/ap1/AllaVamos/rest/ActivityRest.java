package ap1.AllaVamos.rest;

import ap1.AllaVamos.model.ActivityModel;
import ap1.AllaVamos.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// 🔥 SWAGGER
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "Actividades", description = "CRUD de actividades turísticas en Lunahuaná")
@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ActivityRest {

    private final ActivityService service;

    // 🔥 LISTAR TODOS (ACTIVOS + INACTIVOS)
    @Operation(summary = "Listar todas las actividades (activas e inactivas)")
    @GetMapping
    public Flux<ActivityModel> getAll() {
        return service.findAll();
    }

    // 🔍 BUSCAR POR ID
    @Operation(summary = "Buscar actividad por ID")
    @GetMapping("/{id}")
    public Mono<ActivityModel> getById(
            @Parameter(description = "ID de la actividad")
            @PathVariable String id) {
        return service.findById(id);
    }

    // 🔍 FILTRAR POR ESTADO
    @Operation(summary = "Filtrar actividades por estado (true=activo, false=inactivo)")
    @GetMapping("/state/{state}")
    public Flux<ActivityModel> getByState(
            @Parameter(description = "Estado true/false")
            @PathVariable Boolean state) {
        return service.findByState(state);
    }

    // ✅ CREAR
    @Operation(summary = "Crear actividad (registra createdAt)")
    @PostMapping
    public Mono<ActivityModel> create(@RequestBody ActivityModel activity) {
        return service.save(activity);
    }

    // 🔄 ACTUALIZAR
    @Operation(summary = "Actualizar actividad (registra updatedAt)")
    @PutMapping("/{id}")
    public Mono<ActivityModel> update(
            @Parameter(description = "ID de la actividad")
            @PathVariable String id,
            @RequestBody ActivityModel activity) {

        activity.setId(id);
        return service.update(id, activity);
    }

    // ❌ ELIMINADO LÓGICO
    @Operation(summary = "Eliminar lógico (state=false y guarda deletedAt)")
    @PatchMapping("/delete/{id}")
    public Mono<ActivityModel> delete(
            @Parameter(description = "ID de la actividad")
            @PathVariable String id) {
        return service.deleteLogical(id);
    }

    // ♻️ RESTAURAR
    @Operation(summary = "Restaurar actividad (state=true y registra updatedAt)")
    @PatchMapping("/restore/{id}")
    public Mono<ActivityModel> restore(
            @Parameter(description = "ID de la actividad")
            @PathVariable String id) {
        return service.restoreLogical(id);
    }
}