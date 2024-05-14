package ru.sstu.studentprofile.domain.service.request.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.request.Request;
import ru.sstu.studentprofile.data.models.request.RequestResult;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.domain.service.request.dto.RequestIn;
import ru.sstu.studentprofile.domain.service.request.dto.RequestOut;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sender", expression = "java(sender)")
    @Mapping(target = "recipient", expression = "java(recipient)")
    @Mapping(target = "project", expression = "java(project)")
    @Mapping(target = "result", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Request toRequest(RequestIn requestIn, User sender, User recipient, Project project);

    @Mapping(target = "sender", expression = "java(mapperSender.toSenderOut(request.getSender()))")
//    @Mapping(target = "recipient_id", expression = "java(request.getRecipient().getId())")
//    @Mapping(target = "project_id", expression = "java(request.getProject().getId())")
    @Mapping(target = "createdAt", source = "request.createdAt")
    RequestOut toRequestOut(Request request, SenderMapper mapperSender);
}
