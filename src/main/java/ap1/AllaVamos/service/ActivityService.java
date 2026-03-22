package ap1.AllaVamos.service;

import ap1.AllaVamos.model.ActivityModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActivityService {

    // ✅ Listar todas las actividades
    Flux<ActivityModel> findAll();

    // ✅ Buscar por ID
    Mono<ActivityModel> findById(String id);

    // ✅ Filtrar por estado (activo/inactivo)
    Flux<ActivityModel> findByState(Boolean state);

    // ✅ Crear actividad
    Mono<ActivityModel> save(ActivityModel activity);

    // ✅ Actualizar actividad
    Mono<ActivityModel> update(String id, ActivityModel activity);

    // ✅ Eliminado lógico (state = false)
    Mono<Void> deleteLogical(String id);

    // ✅ Restaurar (state = true)
    Mono<Void> restoreLogical(String id);
}