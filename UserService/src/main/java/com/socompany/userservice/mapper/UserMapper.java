package com.socompany.userservice.mapper;

import com.socompany.userservice.dto.UserDto;
import com.socompany.userservice.persistent.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {java.util.UUID.class, java.time.Instant.class})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "createdDate", expression = "java(Instant.now())")
    @Mapping(target = "lastModifiedDate", expression = "java(Instant.now())")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "roles", target = "roles")
    User toEntity(UserDto userDto);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "roles", target = "roles")
    UserDto toDto(User user);
}