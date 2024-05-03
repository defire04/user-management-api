package com.example.mapper.user.imp;

import com.example.domain.User;
import com.example.dto.user.UserDTO;
import com.example.mapper.user.IUserMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements IUserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public User toModel(UserDTO dto) {
        return modelMapper.map(dto, User.class);
    }

    @Override
    public UserDTO toDTO(User model) {
        return modelMapper.map(model, UserDTO.class);
    }
}
