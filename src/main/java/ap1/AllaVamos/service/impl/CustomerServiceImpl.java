package ap1.AllaVamos.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import ap1.AllaVamos.model.CustomerModel;
import ap1.AllaVamos.repository.CustomerRepository;
import ap1.AllaVamos.service.CustomerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private static LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("America/Lima"));
    }

    @Override
    public Flux<CustomerModel> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Mono<CustomerModel> findById(String id) {
        return customerRepository.findById(id);
    }

    @Override
    public Mono<CustomerModel> save(CustomerModel customer) {
        // Activo por defecto y timestamps iniciales (soft-delete)
        customer.setState(true);
        customer.setCreatedAt(now());
        customer.setUpdatedAt(null);
        customer.setDeletedAt(null);
        customer.setRestoredAt(null);
        return customerRepository.save(customer);
    }

    @Override
    public Mono<CustomerModel> update(String id, CustomerModel customer) {
        return customerRepository.findById(id)
                .flatMap(c -> {
                    c.setName(customer.getName());
                    c.setLastName(customer.getLastName());
                    c.setBirthDate(customer.getBirthDate());
                    c.setDocType(customer.getDocType());
                    c.setDocNumber(customer.getDocNumber());
                    c.setEmail(customer.getEmail());
                    c.setPhoneNumber(customer.getPhoneNumber());

                    Boolean prevState = c.getState();
                    if (customer.getState() != null) {
                        c.setState(customer.getState());
                        if (Boolean.TRUE.equals(customer.getState()) && (prevState == null || !prevState)) {
                            c.setRestoredAt(now());
                            c.setDeletedAt(null);
                        }
                        if (Boolean.FALSE.equals(customer.getState()) && (prevState == null || prevState)) {
                            c.setDeletedAt(now());
                            c.setRestoredAt(null);
                        }
                    }

                    c.setUpdatedAt(now());
                    return customerRepository.save(c);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        // Mantiene el comportamiento de "eliminar lógico": si no existe, simplemente completamos.
        return customerRepository.findById(id)
                .flatMap(c -> {
                    c.setState(false);
                    c.setDeletedAt(now());
                    c.setRestoredAt(null);
                    return customerRepository.save(c);
                })
                .then();
    }

    @Override
    public Flux<CustomerModel> findByState(Boolean state) {
        return customerRepository.findByState(state);
    }

    @Override
    public Mono<CustomerModel> updateState(String id, Boolean newState) {
        return customerRepository.findById(id)
                .flatMap(c -> {
                    Boolean prev = c.getState();
                    c.setState(newState);
                    c.setUpdatedAt(now());
                    if (Boolean.TRUE.equals(newState) && (prev == null || !prev)) {
                        c.setRestoredAt(now());
                        c.setDeletedAt(null);
                    }
                    if (Boolean.FALSE.equals(newState) && (prev == null || prev)) {
                        c.setDeletedAt(now());
                        c.setRestoredAt(null);
                    }
                    return customerRepository.save(c);
                });
    }
}