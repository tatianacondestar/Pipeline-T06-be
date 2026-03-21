package ap1.AllaVamos.service;

import ap1.AllaVamos.model.CustomerModel;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerModel> findAll();
    Mono<CustomerModel> findById(String id);
    Mono<CustomerModel> save(CustomerModel customer);
    Mono<CustomerModel> update(String id, CustomerModel customer);
    Mono<Void> deleteById(String id);
    Flux<CustomerModel> findByState(Boolean state);
    Mono<CustomerModel> updateState(String id, Boolean newState);
}