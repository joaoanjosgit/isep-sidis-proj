package com.example.psoft_22_23_project.plansmanagement.model;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class PlanDTO {

    private String version;
    @Size(min = 1)
    @NotNull
    private String name;
    @Size(min = 1)
    @NotNull
    private String description;
    @Size(min = 1)
    @NotNull
    private String numberOfMinutes;
    @Pattern(regexp = "(automatic|personalized)")
    @NotNull
    private String musicSuggestion;
    @Min(0)
    @NotNull
    private String maximumNumberOfUsers;
    @Min(0)
    @NotNull
    private String musicCollection;
    @Min(0)
    @NotNull
    private String annualFee;
    @Min(0)
    @NotNull
    private String monthlyFee;
    @Nullable
    private String active;
    @Nullable
    private String promoted;

}
