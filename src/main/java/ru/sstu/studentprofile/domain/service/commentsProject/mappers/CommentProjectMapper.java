package ru.sstu.studentprofile.domain.service.commentsProject.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.commentsProject.CommentsProject;
import ru.sstu.studentprofile.domain.service.commentsProject.dto.CommentProjectIn;
import ru.sstu.studentprofile.domain.service.commentsProject.dto.CommentProjectOut;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentProjectMapper {
    @Mapping(target = "author", expression = "java(commentProjectUserMapper.toCommentProjectUserOut(commentsProject.getAuthor()))")
    CommentProjectOut toCommentProjectOut(CommentsProject commentsProject, CommentProjectUserMapper commentProjectUserMapper);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "createDate", expression = "java(commentProjectIn.createDate())")
    CommentsProject toCommentProject(CommentProjectIn commentProjectIn);
}
