package ru.sstu.studentprofile.domain.service.request.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.domain.service.request.dto.SenderOut;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SenderMapper {
    SenderOut toSenderOut(User user);
}
