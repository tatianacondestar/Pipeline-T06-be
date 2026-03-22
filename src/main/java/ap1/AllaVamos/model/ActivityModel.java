package ap1.AllaVamos.model;

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

    // 🔥 CORREGIDO
    private String description;

    @Field("duration_hours")
    private Integer durationHours;

    @Field("max_quota")
    private Integer maxQuota;

    private Double price;
    private String location;
    private Integer difficulty;

    @Field("activity_date")
    private Date activityDate;

    @Builder.Default
    private Boolean state = true;

    @Field("created_at")
    private Date createdAt;
}