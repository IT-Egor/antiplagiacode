package ru.itegor.antiplagiacode.user;

import org.mapstruct.*;
import ru.itegor.antiplagiacode.user.dto.MergeUserRequestDto;
import ru.itegor.antiplagiacode.user.dto.UserResponseDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserResponseDto toUserResponseDto(UserEntity userEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserEntity updateWithNull(MergeUserRequestDto userUpdateDto, @MappingTarget UserEntity userEntity);

    UserEntity toEntity(MergeUserRequestDto mergeUserRequestDto);
}