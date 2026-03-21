package ap1.AllaVamos.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ap1.AllaVamos.model.CustomerModel;
import ap1.AllaVamos.service.CustomerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "CRUD de clientes con ID secuencial y campos en inglés")
public class CustomerRest {

    private final CustomerService customerService;

    public CustomerRest(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los clientes")
    public Flux<CustomerModel> listar() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID")
    public Mono<ResponseEntity<CustomerModel>> obtenerPorId(@PathVariable String id) {
        return customerService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/state/{state}")
    @Operation(summary = "Listar clientes por estado (true/false)")
    public Flux<CustomerModel> listarPorEstado(@PathVariable Boolean state) {
        return customerService.findByState(state);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo cliente")
    public Mono<CustomerModel> crear(@RequestBody CustomerModel customer) {
        return customerService.save(customer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente completo")
    public Mono<ResponseEntity<CustomerModel>> actualizar(@PathVariable String id, @RequestBody CustomerModel customer) {
        return customerService.update(id, customer)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente por ID")
    public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id) {
        return customerService.deleteById(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activar cliente")
    public Mono<ResponseEntity<CustomerModel>> activar(@PathVariable String id) {
        return customerService.updateState(id, true)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Desactivar cliente")
    public Mono<ResponseEntity<CustomerModel>> desactivar(@PathVariable String id) {
        return customerService.updateState(id, false)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}