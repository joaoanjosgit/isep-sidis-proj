package com.example.psoft_22_23_project.subscriptionsmanagement.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class IsBonus {

    @Column(name = "Is_Bonus")
    private boolean bonus = Boolean.FALSE;

}
