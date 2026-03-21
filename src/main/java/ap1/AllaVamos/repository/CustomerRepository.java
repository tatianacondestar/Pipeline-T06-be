package ap1.AllaVamos.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import ap1.AllaVamos.model.CustomerModel;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<CustomerModel, String> {

    // Buscar por email (ignorando mayúsculas)
    Flux<CustomerModel> findByEmailContainingIgnoreCase(String email);

    // Buscar por estado (activo/inactivo)
    Flux<CustomerModel> findByState(Boolean state);

    // Validaciones útiles
    Mono<Boolean> existsByDocNumber(String docNumber);
    Mono<Boolean> existsByEmail(String email);
}