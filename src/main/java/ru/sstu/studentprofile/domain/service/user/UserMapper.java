package ru.sstu.studentprofile.domain.service.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface UserMapper {
    @Mapping(target = "roles", expression = "java(user.getUserRoles().stream().map(userRole -> userRole.getRole().getName()).toList())")
    @Mapping(target = "rolesForProject", expression = "java(user.getUserRolesForProject().stream().map(userRoleForProject -> userRoleForProject.getRole().getName()).toList())")
    UserOut toUserOut(User user);
}
