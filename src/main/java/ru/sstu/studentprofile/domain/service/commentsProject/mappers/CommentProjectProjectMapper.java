package ru.sstu.studentprofile.domain.service.commentsProject.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.domain.service.commentsProject.dto.CommentProjectProjectOut;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentProjectProjectMapper {
    CommentProjectProjectOut toCommentProjectProjectOut(Project project);
}
