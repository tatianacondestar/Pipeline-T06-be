package ap1.AllaVamos.repository;

import ap1.AllaVamos.model.GuideModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface GuideRepository extends ReactiveMongoRepository<GuideModel, String> {

    // 🔥 EXTRA PRO (opcional pero recomendado)

    // Listar solo guías activos
    Flux<GuideModel> findByStateTrue();

    // Buscar por nombre (para demo)
    Flux<GuideModel> findByNameContainingIgnoreCase(String name);
}