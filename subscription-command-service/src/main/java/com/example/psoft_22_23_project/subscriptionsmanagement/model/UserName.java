package com.example.psoft_22_23_project.subscriptionsmanagement.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Data
@Embeddable
public class UserName {


    private String userName;

    public UserName(String userName ) {
        this.userName = userName;
    }

    public UserName() {

    }
}
