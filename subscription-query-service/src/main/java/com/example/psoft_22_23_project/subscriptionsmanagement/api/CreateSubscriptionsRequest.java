package com.example.psoft_22_23_project.subscriptionsmanagement.api;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PaymentType;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubscriptionsRequest {


        private String name;
        @Pattern(regexp = "(annually|monthly)")
        private String paymentType;
        @Nullable
        private String userName;
        @Nullable
        private String planName;
        @Nullable
        private String startDate;
        @Nullable
        private String endDate;
        @Nullable
        private String activeStatus;
}
