package com.example.psoft_22_23_project.subscriptionsmanagement.api;

import lombok.*;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionsDTO {


    private String userName;
    private String planName;
    @Pattern(regexp = "(annually|monthly)")
    private String paymentType;
    private String startDate;
    private String endDate;
    private String activeStatus;
}
