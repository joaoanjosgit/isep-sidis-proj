package com.example.psoft_22_23_project.plansmanagement.services;

import com.example.psoft_22_23_project.plansmanagement.model.PlanDTO;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class PlanDTOMapper {

    @Mapping(source = "request.name", target = "name.name")
    @Mapping(source = "request.description", target = "description.description")
    @Mapping(source = "request.numberOfMinutes", target = "numberOfMinutes.numberOfMinutes")
    @Mapping(source = "request.maximumNumberOfUsers", target = "maximumNumberOfUsers.maximumNumberOfUsers")
    @Mapping(source = "request.musicCollection", target = "musicCollection.musicCollection")
    @Mapping(source = "request.musicSuggestion", target = "musicSuggestion.musicSuggestion")
    @Mapping(source = "request.annualFee", target = "annualFee.annualFee")
    @Mapping(source = "request.monthlyFee", target = "monthlyFee.monthlyFee")
    @Mapping(source = "request.active", target = "active.active")
    @Mapping(source = "request.promoted", target = "promoted.promoted")
    public abstract Plans createPlanFromDTO(PlanDTO request);

    @Mapping(source = "request.name.name", target = "name")
    @Mapping(source = "request.version", target = "version")
    @Mapping(source = "request.description.description", target = "description")
    @Mapping(source = "request.numberOfMinutes.numberOfMinutes", target = "numberOfMinutes")
    @Mapping(source = "request.maximumNumberOfUsers.maximumNumberOfUsers", target = "maximumNumberOfUsers")
    @Mapping(source = "request.musicCollection.musicCollection", target = "musicCollection")
    @Mapping(source = "request.musicSuggestion.musicSuggestion", target = "musicSuggestion")
    @Mapping(source = "request.annualFee.annualFee", target = "annualFee")
    @Mapping(source = "request.monthlyFee.monthlyFee", target = "monthlyFee")
    @Mapping(source = "request.active.active", target = "active")
    @Mapping(source = "request.promoted.promoted", target = "promoted")
    public abstract PlanDTO createDTOFromPlan(Plans request);

}
