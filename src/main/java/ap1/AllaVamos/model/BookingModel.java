package ap1.AllaVamos.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "bookings")
public class BookingModel {

    @Id
    private String id;

    private CustomerSummary customer;

    @Field("booking_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
    private LocalDateTime bookingDate;

    @Field("total_pay")
    private Double totalPay;

    @Builder.Default
    private Boolean state = true;

    private List<BookingDetail> details;

    private List<Payment> payments;

    @Field("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
    private LocalDateTime createdAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CustomerSummary {
        private String name;
        private String email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingDetail {
        @Field("activity_name")
        private String activityName;

        @Field("personnel_quantity")
        private Integer personnelQuantity;

        @Field("sub_total")
        private Double subTotal;

        private Double discount;

        @Field("state_detail")
        private String stateDetail;

        private List<EquipmentDetail> equipment;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EquipmentDetail {
        private String name;
        private Integer quantity;
        private Boolean returned;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Payment {
        private Double amount;

        @Field("payment_method")
        private String paymentMethod;

        private String currency;

        @Field("pay_date")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
        private LocalDateTime payDate;
    }
}
