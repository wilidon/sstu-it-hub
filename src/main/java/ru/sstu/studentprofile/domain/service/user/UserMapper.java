package ru.sstu.studentprofile.domain.service.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.models.user.UserReview;
import ru.sstu.studentprofile.domain.service.user.dto.UserEvent;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;
import ru.sstu.studentprofile.domain.service.user.dto.UserProject;
import ru.sstu.studentprofile.domain.service.user.dto.UserReviewOut;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface UserMapper {
    @Mapping(target = "roles", expression = "java(user.getUserRoles().stream().map(userRole -> userRole.getRole().getName()).toList())")
    @Mapping(target = "rolesForProject", expression = "java(user.getUserRolesForProject().stream().map(userRoleForProject -> userRoleForProject.getRole().getName()).toList())")
    UserOut toUserOut(User user, List<UserReview> userReviews);

    @Mapping(target = "membersCount", source = "membersCount")
    UserEvent toUserEvent(Event event, Long membersCount);

    List<UserEvent> toUserEvent(List<Event> events);

    UserProject toUserProject(Project project);

    UserReviewOut toUserReviewOut(UserReview userReview);
    List<UserReviewOut> toUserReviewOut(List<UserReview> userReviews);
}
