package ap1.AllaVamos.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime; // 🔥 CAMBIO

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "activities")
public class ActivityModel {

    @Id
    private String id;

    private String name;

    private String description;

    @Field("duration_hours")
    private Integer durationHours;

    @Field("max_quota")
    private Integer maxQuota;

    private Double price;

    private String location;

    private Integer difficulty;

    // 🔥 Fecha de actividad
    @Field("activity_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
    private LocalDateTime activityDate; // 🔥 CAMBIO

    // 🔥 Estado lógico (activo / eliminado)
    @Builder.Default
    private Boolean state = true;

    // ===============================
    // 🔥 AUDITORÍA COMPLETA
    // ===============================

    @Field("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
    private LocalDateTime createdAt; // 🔥 CAMBIO

    @Field("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
    private LocalDateTime updatedAt; // 🔥 CAMBIO

    @Field("deleted_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
    private LocalDateTime deletedAt; // 🔥 CAMBIO

    // 🔥 RESTAURACIÓN (IMPORTANTE)
    @Field("restored_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
    private LocalDateTime restoredAt; // 🔥 CAMBIO

    // 🔥 MÉTODOS AUTOMÁTICOS
    public void markDeleted(LocalDateTime date) { // 🔥 CAMBIO
        this.state = false;
        if (this.deletedAt == null) {
            this.deletedAt = date;
        }
    }

    public void markRestored(LocalDateTime date) { // 🔥 CAMBIO
        this.state = true;
        this.restoredAt = date;
    }
}