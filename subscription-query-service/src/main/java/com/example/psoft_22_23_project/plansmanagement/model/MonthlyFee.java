package com.example.psoft_22_23_project.plansmanagement.model;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
@Data
public class MonthlyFee {
    @Column(name = "Monthly_Fee")
    @NotNull
    @Min(0)
    private double monthlyFee;

    public void setMonthlyFee(double monthlyFee) {
        if (monthlyFee < 0) {
            throw new IllegalArgumentException("Monthly Fee must be positive");
        }
        this.monthlyFee = monthlyFee;
    }

}
