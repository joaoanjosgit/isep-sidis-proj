package com.example.psoft_22_23_project.usermanagement.services;

import com.example.psoft_22_23_project.usermanagement.model.User;
import com.example.psoft_22_23_project.usermanagement.model.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserDTOMapper {

    public abstract UserDTO createDTOFromUser(User request);
    public abstract User createUserFromDTO(UserDTO request);

}
