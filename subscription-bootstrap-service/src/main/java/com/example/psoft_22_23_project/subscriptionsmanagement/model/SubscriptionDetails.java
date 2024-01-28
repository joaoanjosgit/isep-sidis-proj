package com.example.psoft_22_23_project.subscriptionsmanagement.model;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.model.Promoted;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;

@Data
@Getter
@RequiredArgsConstructor
public class SubscriptionDetails {



    private String name;
    private String description;
    private String numberOfMinutes;
    private String maximumNumberOfUsers;
    private String musicCollection;
    private String musicSuggestion;
    private String monthlyFee;
    private String annualFee;
    private String active;
    private String promoted;


    public SubscriptionDetails(String name, String description, String numberOfMinutes, String maximumNumberOfUsers, String musicCollection, String musicSuggestion, String monthlyFee, String annualFee, String active, String promoted) {
        this.name = name;
        this.description = description;
        this.numberOfMinutes = numberOfMinutes;
        this.maximumNumberOfUsers = maximumNumberOfUsers;
        this.musicCollection = musicCollection;
        this.musicSuggestion = musicSuggestion;
        this.monthlyFee = monthlyFee;
        this.annualFee = annualFee;
        this.active = active;
        this.promoted = promoted;
    }
}
