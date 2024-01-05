package com.neobis.mobiMarket.mapper;

import com.neobis.mobiMarket.dto.RegisterDto;
import com.neobis.mobiMarket.dto.UserDto;
import com.neobis.mobiMarket.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto entityToDto(User user);

    User dtoToEntity(UserDto userDto);

    List<UserDto> entitiesToDtos(List<User> users);

    User registerDtoToEntity(RegisterDto registerDto);

}
