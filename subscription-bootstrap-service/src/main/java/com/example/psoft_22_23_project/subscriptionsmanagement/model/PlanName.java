package com.example.psoft_22_23_project.subscriptionsmanagement.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Data
@Embeddable
public class PlanName {


    private String planName;

    public PlanName(String userName ) {
        this.planName = planName;
    }

    public PlanName() {

    }
}
