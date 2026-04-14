package ap1.AllaVamos.rest;

import ap1.AllaVamos.model.BookingModel;
import ap1.AllaVamos.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Operaciones transaccionales de reservas con cabecera y detalle")
public class BookingRest {

    private final BookingService bookingService;

    @GetMapping
    @Operation(summary = "Listar reservas (cabecera y detalle)")
    public Flux<BookingModel> listAll() {
        return bookingService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por ID (cabecera y detalle)")
    public Mono<BookingModel> getById(@PathVariable String id) {
        return bookingService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear reserva (cabecera y detalle)")
    public Mono<BookingModel> create(@RequestBody BookingModel booking) {
        return bookingService.save(booking);
    }
}
