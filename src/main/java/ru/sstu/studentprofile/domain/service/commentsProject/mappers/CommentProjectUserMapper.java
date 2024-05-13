package ru.sstu.studentprofile.domain.service.commentsProject.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.domain.service.commentsProject.dto.CommentProjectUserOut;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentProjectUserMapper {
    CommentProjectUserOut toCommentProjectUserOut(User user);
}
