package ap1.AllaVamos.service;

import ap1.AllaVamos.model.BookingModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingService {
    Flux<BookingModel> findAll();
    Mono<BookingModel> findById(String id);
    Mono<BookingModel> save(BookingModel booking);
}
