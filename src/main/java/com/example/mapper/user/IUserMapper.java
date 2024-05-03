package com.example.mapper.user;

import com.example.domain.User;
import com.example.dto.user.UserDTO;

public interface IUserMapper {


    User toModel(UserDTO dto);
    UserDTO toDTO(User model);
}
