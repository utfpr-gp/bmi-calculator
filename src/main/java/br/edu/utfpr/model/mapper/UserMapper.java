package br.edu.utfpr.model.mapper;

import br.edu.utfpr.dto.UserDTO;
import br.edu.utfpr.model.domain.User;

/**
 * Created by ronifabio on 01/05/2019.
 */
public class UserMapper {

    public static User toEntity(UserDTO dto){
        User entity = new User(dto.getName(), dto.getEmail(), dto.getPassword());
        return entity;
    }

    public static UserDTO toDTO(User entity){
        UserDTO dto = new UserDTO(entity.getName(), entity.getEmail(), entity.getPassword(), entity.getPassword());
        return dto;
    }
}
