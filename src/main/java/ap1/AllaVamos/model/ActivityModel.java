package ap1.AllaVamos.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

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
    private Date activityDate;

    // 🔥 Estado lógico (activo / eliminado)
    @Builder.Default
    private Boolean state = true;

    // 🔥 AUDITORÍA

    @Field("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
    private Date createdAt;

    @Field("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
    private Date updatedAt;

    @Field("deleted_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
    private Date deletedAt;
}