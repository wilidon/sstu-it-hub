package ru.sstu.studentprofile.domain.service.project.comment.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.project.comment.ProjectComment;
import ru.sstu.studentprofile.domain.service.project.comment.dto.ProjectCommentIn;
import ru.sstu.studentprofile.domain.service.project.comment.dto.ProjectCommentOut;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentProjectMapper {
    @Mapping(target = "isEdited", source = "edited")
    ProjectCommentOut toCommentProjectOut(ProjectComment projectComment);

    List<ProjectCommentOut> toCommentProjectOut(List<ProjectComment> projectComment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "edited", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "project", ignore = true)
    ProjectComment toCommentProject(ProjectCommentIn projectCommentIn);
}
