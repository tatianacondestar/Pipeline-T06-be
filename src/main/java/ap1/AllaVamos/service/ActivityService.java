package ap1.AllaVamos.service;

import ap1.AllaVamos.model.ActivityModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActivityService {

    // 🔥 LISTAR TODOS (ACTIVOS + INACTIVOS)
    Flux<ActivityModel> findAll();

    // 🔍 Buscar por ID
    Mono<ActivityModel> findById(String id);

    // 🔍 Filtrar por estado (true / false)
    Flux<ActivityModel> findByState(Boolean state);

    // ✅ Crear actividad (auditoría: createdAt)
    Mono<ActivityModel> save(ActivityModel activity);

    // 🔄 Actualizar actividad (auditoría: updatedAt)
    Mono<ActivityModel> update(String id, ActivityModel activity);

    // ❌ Eliminado lógico (auditoría: deletedAt)
    Mono<ActivityModel> deleteLogical(String id);

    // ♻️ Restaurar (auditoría: limpiar deletedAt)
    Mono<ActivityModel> restoreLogical(String id);
}