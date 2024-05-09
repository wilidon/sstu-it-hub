package ru.sstu.studentprofile.domain.service.project;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.domain.service.event.dto.EventIn;
import ru.sstu.studentprofile.domain.service.event.dto.EventOut;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectIn;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectOut;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapper {
    ProjectOut toProjectOut(Project project);

    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "leader", source = "user")
    Project toProject(ProjectIn projectIn, User user);
}
