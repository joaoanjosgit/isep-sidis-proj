package com.example.psoft_22_23_project.usermanagement.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class UserDTO {

    @NotNull
    private String username;

    @NotNull
    private String password;

    private String location;

    private UserImage userImage;

    private Set<RoleDTO> authorities;

    public void addAuthority(final RoleDTO r) {
        authorities.add(r);
    }

}
