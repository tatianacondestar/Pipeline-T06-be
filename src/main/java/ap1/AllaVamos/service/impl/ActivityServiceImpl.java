package ap1.AllaVamos.service.impl;

import ap1.AllaVamos.model.ActivityModel;
import ap1.AllaVamos.repository.ActivityRepository;
import ap1.AllaVamos.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository repository;

    // ✅ LISTAR TODO
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

    // ✅ CREAR (🔥 CORREGIDO)
    @Override
    public Mono<ActivityModel> save(ActivityModel activity) {

        // 🔥 asegurar que sea nuevo
        activity.setId(null);

        // 🔥 estado por defecto
        if (activity.getState() == null) {
            activity.setState(true);
        }

        // 🔥 solo createdAt automático
        activity.setCreatedAt(new Date());

        // ❌ NO forzar activityDate (evita error 500)
        // se usará solo si viene en el JSON

        return repository.save(activity);
    }

    // ✅ ACTUALIZAR
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

                    return repository.save(existing);
                });
    }

    // ✅ ELIMINADO LÓGICO
    @Override
    public Mono<Void> deleteLogical(String id) {
        return repository.findById(id)
                .flatMap(activity -> {
                    activity.setState(false);
                    return repository.save(activity);
                })
                .then();
    }

    // ✅ RESTAURAR
    @Override
    public Mono<Void> restoreLogical(String id) {
        return repository.findById(id)
                .flatMap(activity -> {
                    activity.setState(true);
                    return repository.save(activity);
                })
                .then();
    }
}