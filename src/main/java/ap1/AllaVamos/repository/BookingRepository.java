package ap1.AllaVamos.repository;

import ap1.AllaVamos.model.BookingModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends ReactiveMongoRepository<BookingModel, String> {
}
