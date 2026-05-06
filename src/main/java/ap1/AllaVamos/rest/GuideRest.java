package ap1.AllaVamos.rest;

import ap1.AllaVamos.model.GuideModel;
import ap1.AllaVamos.service.GuideService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Guide", description = "Gestión de guías turísticos con auditoría y eliminado lógico")
@RestController
@RequestMapping("/api/guides")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GuideRest {

    private final GuideService service;

    // =========================
    // 🔍 LISTAR TODOS
    // =========================
    @Operation(summary = "Listar todos los guías")
    @GetMapping
    public Flux<GuideModel> getAll() {
        return service.findAll();
    }

    // =========================
    // 🔍 LISTAR ACTIVOS (CORRECTO)
    // =========================
    @Operation(summary = "Listar guías activos")
    @GetMapping("/active")
    public Flux<GuideModel> getActive() {
        return service.findAll()
                .filter(g -> Boolean.TRUE.equals(g.getState()));
    }

    // =========================
    // 🔍 BUSCAR POR ID
    // =========================
    @Operation(summary = "Buscar guía por ID")
    @GetMapping("/{id}")
    public Mono<GuideModel> getById(@PathVariable String id) {
        return service.findById(id);
    }

    // =========================
    // ➕ CREAR
    // =========================
    @Operation(summary = "Crear guía")
    @PostMapping
    public Mono<GuideModel> create(@RequestBody GuideModel guide) {

        if (guide.getName() == null || guide.getEmail() == null) {
            return Mono.error(new RuntimeException("Nombre y email son obligatorios"));
        }

        return service.create(guide);
    }

    // =========================
    // ✏️ EDITAR
    // =========================
    @Operation(summary = "Editar guía")
    @PutMapping("/{id}")
    public Mono<GuideModel> update(@PathVariable String id,
                                  @RequestBody GuideModel guide) {
        return service.update(id, guide);
    }

    // =========================
    // ❌ ELIMINADO LÓGICO
    // =========================
    @Operation(summary = "Eliminar guía (lógico)")
    @PatchMapping("/delete/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return service.delete(id);
    }

    // =========================
    // ♻️ RESTAURAR
    // =========================
    @Operation(summary = "Restaurar guía")
    @PatchMapping("/restore/{id}")
    public Mono<Void> restore(@PathVariable String id) {
        return service.restore(id);
    }
}