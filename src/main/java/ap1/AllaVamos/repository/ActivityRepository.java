package ap1.AllaVamos.repository;

import ap1.AllaVamos.model.ActivityModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ActivityRepository extends ReactiveMongoRepository<ActivityModel, String> {

    // ✅ Filtrar por estado (ACTIVO / INACTIVO)
    Flux<ActivityModel> findByState(Boolean state);

    // 🔍 Buscar por nombre (like)
    Flux<ActivityModel> findByNameContainingIgnoreCase(String name);

    // 📍 Buscar por ubicación
    Flux<ActivityModel> findByLocation(String location);

    // 🔥 OPCIONAL PRO (por si lo usas luego)
    Mono<Boolean> existsByName(String name);
}