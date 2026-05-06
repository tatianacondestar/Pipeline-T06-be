package ap1.AllaVamos.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "guides")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuideModel {

    @Id
    private String id;

    private String name;
    private String email;
    private String phone;

    // 🔥 ESTADO (soft delete)
    @Builder.Default
    private Boolean state = true;

    // 🔥 AUDITORÍA
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime restoredAt;
}