package ap1.AllaVamos.service.impl;

import ap1.AllaVamos.model.BookingModel;
import ap1.AllaVamos.repository.BookingRepository;
import ap1.AllaVamos.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private static LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("America/Lima"));
    }

    @Override
    public Flux<BookingModel> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Mono<BookingModel> findById(String id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Mono<BookingModel> save(BookingModel booking) {
        booking.setId(null);

        if (booking.getState() == null) {
            booking.setState(true);
        }

        if (booking.getBookingDate() == null) {
            booking.setBookingDate(now());
        }

        booking.setCreatedAt(now());

        if (booking.getTotalPay() == null) {
            double computedTotal = 0.0;
            if (booking.getDetails() != null) {
                computedTotal = booking.getDetails().stream()
                        .filter(Objects::nonNull)
                        .mapToDouble(detail -> {
                            double subTotal = detail.getSubTotal() == null ? 0.0 : detail.getSubTotal();
                            double discount = detail.getDiscount() == null ? 0.0 : detail.getDiscount();
                            return subTotal - discount;
                        })
                        .sum();
            }
            booking.setTotalPay(computedTotal);
        }

        return bookingRepository.save(booking);
    }
}
