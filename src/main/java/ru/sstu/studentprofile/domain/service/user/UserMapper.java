package ru.sstu.studentprofile.domain.service.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.user.User;
import ru.sstu.studentprofile.data.models.user.UserMedia;
import ru.sstu.studentprofile.data.models.user.UserRating;
import ru.sstu.studentprofile.data.models.user.UserRatingType;
import ru.sstu.studentprofile.data.models.user.UserReview;
import ru.sstu.studentprofile.data.repository.user.projection.UserRatingProjection;
import ru.sstu.studentprofile.domain.service.user.dto.UserEvent;
import ru.sstu.studentprofile.domain.service.user.dto.UserMediaOut;
import ru.sstu.studentprofile.domain.service.user.dto.UserOut;
import ru.sstu.studentprofile.domain.service.user.dto.UserProject;
import ru.sstu.studentprofile.domain.service.user.dto.UserReviewOut;
import ru.sstu.studentprofile.domain.service.user.dto.rating.UserRatingOut;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface UserMapper {
    @Mapping(target = "roles", expression = "java(user.getUserRoles().stream().map(userRole -> userRole.getRole().getName()).toList())")
    @Mapping(target = "rolesForProject", expression = "java(user.getUserRolesForProject().stream().map(userRoleForProject -> new RoleForProjectOut(userRoleForProject.getRole().getId(), userRoleForProject.getRole().getName())).toList())")
    @Mapping(target = "media", source = "user")
    @Mapping(target = "ratings", source = "userRating")
    UserOut toUserOut(User user, List<UserReview> userReviews, UserRatingOut userRating);

//    @Mapping(target = "count", source = "value.count")
//    @Mapping(target = "ratingType", source = "value.ratingType")
//    UserRatingOut map(UserRatingProjection value);

    @Mapping(target = "membersCount", source = "membersCount")
    UserEvent toUserEvent(Event event, Long membersCount);

    List<UserEvent> toUserEvent(List<Event> events);

    UserProject toUserProject(Project project);

    UserReviewOut toUserReviewOut(UserReview userReview);
    List<UserReviewOut> toUserReviewOut(List<UserReview> userReviews);

    @Mapping(target = "email", source = "user.email")
    @Mapping(target = ".", source = "user.userMedia")
    UserMediaOut toUserMediaOut(User user);

    @Mapping(target = "id", ignore = true)
    UserRating toUserRating(User recipient, User sender, UserRatingType ratingType);

}
