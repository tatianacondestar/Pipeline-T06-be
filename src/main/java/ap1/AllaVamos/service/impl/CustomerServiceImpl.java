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

    // =========================
    // LISTAR
    // =========================
    @Override
    public Flux<CustomerModel> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Mono<CustomerModel> findById(String id) {
        return customerRepository.findById(id);
    }

    @Override
    public Flux<CustomerModel> findByState(Boolean state) {
        return customerRepository.findByState(state);
    }

    // =========================
    // CREAR
    // =========================
    @Override
    public Mono<CustomerModel> save(CustomerModel customer) {
        customer.setState(true);
        customer.setCreatedAt(now());
        customer.setUpdatedAt(null);
        customer.setDeletedAt(null);
        customer.setRestoredAt(null);
        return customerRepository.save(customer);
    }

    // =========================
    // ACTUALIZAR
    // =========================
    @Override
    public Mono<CustomerModel> update(String id, CustomerModel customer) {
        return customerRepository.findById(id)
                .flatMap(c -> {
                    LocalDateTime originalDeletedAt = c.getDeletedAt();
                    LocalDateTime originalRestoredAt = c.getRestoredAt();

                    boolean onlyStateChange = customer.getState() != null
                            && customer.getName() == null
                            && customer.getLastName() == null
                            && customer.getBirthDate() == null
                            && customer.getDocType() == null
                            && customer.getDocNumber() == null
                            && customer.getEmail() == null
                            && customer.getPhoneNumber() == null;

                    c.setName(customer.getName());
                    c.setLastName(customer.getLastName());
                    c.setBirthDate(customer.getBirthDate());
                    c.setDocType(customer.getDocType());
                    c.setDocNumber(customer.getDocNumber());
                    c.setEmail(customer.getEmail());
                    c.setPhoneNumber(customer.getPhoneNumber());

                    Boolean prevState = c.getState();
                    if (customer.getState() != null) {
                        Boolean newState = customer.getState();
                        c.setState(newState);
                        applyAuditDates(c, prevState, newState);
                    }

                    if (c.getDeletedAt() == null && originalDeletedAt != null) {
                        c.setDeletedAt(originalDeletedAt);
                    }
                    if (c.getRestoredAt() == null && originalRestoredAt != null) {
                        c.setRestoredAt(originalRestoredAt);
                    }

                    // updatedAt solo cambia cuando se actualizan datos (no por activar/desactivar)
                    if (!onlyStateChange) {
                        c.setUpdatedAt(now());
                    }

                    return customerRepository.save(c);
                });
    }

    // =========================
    // ELIMINAR (SOFT DELETE)
    // =========================
    @Override
    public Mono<Void> deleteById(String id) {
        return customerRepository.findById(id)
                .flatMap(c -> {
                    LocalDateTime originalRestoredAt = c.getRestoredAt();

                    Boolean prevState = c.getState();
                    c.setState(false);
                    applyAuditDates(c, prevState, false);
                    if (c.getRestoredAt() == null && originalRestoredAt != null) {
                        c.setRestoredAt(originalRestoredAt);
                    }
                    return customerRepository.save(c);
                })
                .then();
    }

    // =========================
    // CAMBIAR ESTADO (ACTIVAR / DESACTIVAR)
    // =========================
    @Override
    public Mono<CustomerModel> updateState(String id, Boolean newState) {
        return customerRepository.findById(id)
                .flatMap(c -> {
                    LocalDateTime originalDeletedAt = c.getDeletedAt();
                    LocalDateTime originalRestoredAt = c.getRestoredAt();

                    Boolean prevState = c.getState();
                    c.setState(newState);
                    applyAuditDates(c, prevState, newState);

                    if (c.getDeletedAt() == null && originalDeletedAt != null) {
                        c.setDeletedAt(originalDeletedAt);
                    }
                    if (c.getRestoredAt() == null && originalRestoredAt != null) {
                        c.setRestoredAt(originalRestoredAt);
                    }

                    return customerRepository.save(c);
                });
    }

    private void applyAuditDates(CustomerModel customer, Boolean prevState, Boolean newState) {
        if (Boolean.FALSE.equals(newState)) {
            customer.setDeletedAt(now());
        }
        if (Boolean.TRUE.equals(newState)) {
            customer.setRestoredAt(now());
        }
    }
}