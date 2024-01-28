package com.example.psoft_22_23_project.plansmanagement.service;

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

}
