package ap1.AllaVamos.service.impl;

import ap1.AllaVamos.model.GuideModel;
import ap1.AllaVamos.repository.GuideRepository;
import ap1.AllaVamos.repository.ActivityScheduleRepository;
import ap1.AllaVamos.service.GuideService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GuideServiceImpl implements GuideService {

    private final GuideRepository repository;
    private final ActivityScheduleRepository scheduleRepository;

    @Override
    public Flux<GuideModel> findAll() {
        return repository.findAll()
                .filter(g -> Boolean.TRUE.equals(g.getState()));
    }

    @Override
    public Mono<GuideModel> findById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Guía no encontrado")));
    }

    @Override
    public Mono<GuideModel> create(GuideModel guide) {

        guide.setId(null);
        guide.setState(true);
        guide.setCreatedAt(LocalDateTime.now());
        guide.setUpdatedAt(null);
        guide.setDeletedAt(null);
        guide.setRestoredAt(null);

        return repository.save(guide);
    }

    @Override
    public Mono<GuideModel> update(String id, GuideModel guide) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Guía no encontrado")))
                .flatMap(existing -> {

                    existing.setName(guide.getName());
                    existing.setEmail(guide.getEmail());
                    existing.setPhone(guide.getPhone());
                    existing.setUpdatedAt(LocalDateTime.now());

                    return repository.save(existing);
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return scheduleRepository.existsByGuideIdAndDeletedAtIsNull(id)
                .flatMap(inUse -> {

                    if (inUse) {
                        return Mono.error(new RuntimeException("No se puede eliminar, guía en uso"));
                    }

                    return repository.findById(id)
                            .switchIfEmpty(Mono.error(new RuntimeException("Guía no encontrado")))
                            .flatMap(existing -> {

                                existing.setState(false);
                                existing.setDeletedAt(LocalDateTime.now());

                                return repository.save(existing);
                            }).then();
                });
    }

    @Override
    public Mono<Void> restore(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Guía no encontrado")))
                .flatMap(existing -> {

                    existing.setState(true);
                    existing.setRestoredAt(LocalDateTime.now());

                    return repository.save(existing);
                }).then();
    }
}