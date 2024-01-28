package com.example.psoft_22_23_project.plansmanagement.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.time.LocalDate;


@Data
@Embeddable
public class FeeRevision {

    private Double annualFee;
    private Double monthlyFee;
    private String user;
    private String time;


    public FeeRevision(Double annualFee,Double monthlyFee, String user) {
        this.annualFee = annualFee;
        this.monthlyFee = monthlyFee;
        this.user = user;
        this.time = LocalDate.now().toString();

    }

    public FeeRevision(Double annualFee,Double monthlyFee) {
        this.annualFee = annualFee;
        this.monthlyFee = monthlyFee;
        this.user = "Created With";
        this.time = LocalDate.now().toString();
    }

    public FeeRevision() {

    }
}
