package ap1.AllaVamos.repository;

import ap1.AllaVamos.model.ActivityScheduleModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityScheduleRepository extends ReactiveMongoRepository<ActivityScheduleModel, String> {
}