package ap1.AllaVamos.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "activity_schedules")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityScheduleModel {

    @Id
    private String id;

    // 🔷 CABECERA
    private String activityId;
    private String guideId;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private Boolean state;

    // 🔶 DETALLE
    private Map<String, Object> details;

    // 🔥 AUDITORÍA COMPLETA
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime restoredAt; // ✅ NUEVO
}