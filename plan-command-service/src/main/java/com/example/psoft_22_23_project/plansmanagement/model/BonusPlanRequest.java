package com.example.psoft_22_23_project.plansmanagement.model;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonusPlanRequest implements Serializable {

    private long userId;

    private Plans plan;

}
