package ap1.AllaVamos.service.impl;

import ap1.AllaVamos.model.ActivityModel;
import ap1.AllaVamos.repository.ActivityRepository;
import ap1.AllaVamos.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository repository;

    // 🔥 FECHA AUTOMÁTICA PERÚ
    private Date now() {
        return Date.from(
                LocalDateTime.now()
                        .atZone(ZoneId.of("America/Lima"))
                        .toInstant()
        );
    }

    // ✅ LISTAR TODOS (ACTIVOS + INACTIVOS)
    @Override
    public Flux<ActivityModel> findAll() {
        return repository.findAll();
    }

    // ✅ BUSCAR POR ID
    @Override
    public Mono<ActivityModel> findById(String id) {
        return repository.findById(id);
    }

    // ✅ FILTRAR POR ESTADO
    @Override
    public Flux<ActivityModel> findByState(Boolean state) {
        return repository.findByState(state);
    }

    // ✅ CREAR
    @Override
    public Mono<ActivityModel> save(ActivityModel activity) {

        activity.setId(null);

        if (activity.getState() == null) {
            activity.setState(true);
        }

        Date now = now();

        activity.setCreatedAt(now);
        activity.setUpdatedAt(null);
        activity.setDeletedAt(null);
        activity.setRestoredAt(null);

        return repository.save(activity);
    }

    // ✅ ACTUALIZAR (SOLO AQUÍ se usa updatedAt)
    @Override
    public Mono<ActivityModel> update(String id, ActivityModel activity) {
        return repository.findById(id)
                .flatMap(existing -> {

                    existing.setName(activity.getName());
                    existing.setDescription(activity.getDescription());
                    existing.setDurationHours(activity.getDurationHours());
                    existing.setMaxQuota(activity.getMaxQuota());
                    existing.setPrice(activity.getPrice());
                    existing.setLocation(activity.getLocation());
                    existing.setDifficulty(activity.getDifficulty());

                    if (activity.getActivityDate() != null) {
                        existing.setActivityDate(activity.getActivityDate());
                    }

                    // 🔥 SOLO AQUÍ se actualiza
                    existing.setUpdatedAt(now());

                    return repository.save(existing);
                });
    }

    // ✅ ELIMINADO LÓGICO (NO TOCA updatedAt)
    @Override
    public Mono<ActivityModel> deleteLogical(String id) {
        return repository.findById(id)
                .flatMap(activity -> {

                    activity.setState(false);

                    if (activity.getDeletedAt() == null) {
                        activity.setDeletedAt(now());
                    }

                    return repository.save(activity);
                });
    }

    // ✅ RESTAURAR (NO TOCA updatedAt)
    @Override
    public Mono<ActivityModel> restoreLogical(String id) {
        return repository.findById(id)
                .flatMap(activity -> {

                    activity.setState(true);

                    // 🔥 NO borrar deletedAt
                    // 🔥 SOLO registrar restauración
                    activity.setRestoredAt(now());

                    // ❌ NO actualizar updatedAt aquí

                    return repository.save(activity);
                });
    }
}