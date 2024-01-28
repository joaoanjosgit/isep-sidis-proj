package com.example.psoft_22_23_project.subscriptionsmanagement.model;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.usermanagement.model.User;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SubsDTO {

    private String version;

    @Size(min = 1)
    @NotNull
    private String startDate;

    @Size(min = 1)
    @NotNull
    private String endDate;

    @Size(min = 1)
    @NotNull
    private String activeStatus;

    @Size(min = 1)
    @NotNull
    private String paymentType;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Plans plan;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private User user;

}
