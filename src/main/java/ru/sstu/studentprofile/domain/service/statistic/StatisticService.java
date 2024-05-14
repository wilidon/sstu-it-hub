package ru.sstu.studentprofile.domain.service.statistic;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.models.project.RoleForProject;
import ru.sstu.studentprofile.data.repository.event.EventRepository;
import ru.sstu.studentprofile.data.repository.project.*;
import ru.sstu.studentprofile.data.repository.project.proection.Role;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
import ru.sstu.studentprofile.domain.service.project.dto.ProjectStatusSearchIn;
import ru.sstu.studentprofile.domain.service.statistic.dto.*;
import ru.sstu.studentprofile.domain.service.statistic.mappers.StatisitcProjectMapper;
import ru.sstu.studentprofile.domain.service.statistic.mappers.StatisticEventMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StatisticService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final RoleForProjectRepository roleForProjectRepository;
    private final MemberRoleForProjectRepository memberRoleForProjectRepository;
    private final ActualRoleForProjectRepository actualRoleForProjectRepository;
    private final EventRepository eventRepository;
    private final StatisitcProjectMapper mapperProject;
    private final StatisticEventMapper mapperEvent;

    public StatisticProjectOut getStatisticProjects() {
        long allProject = projectRepository.getCountAllProject();

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = localDateTime.toLocalDate();
        long allProjectToday = projectRepository.getCountAllProjectToday(localDate);

        long allProjectPlanned = projectRepository.getCountAllProjectPlanned();
        long allProjectOpen = projectRepository.getCountAllProjectOpen();
        long allProjectCompleted = projectRepository.getCountAllProjectCompleted();

        return new StatisticProjectOut(
                allProject,
                allProjectToday,
                allProjectPlanned,
                allProjectOpen,
                allProjectCompleted
        );
    }

    public StatisticPeopleOut getStatisticPeople() {
        long countPeople = userRepository.getCountAllUsers();
        return new StatisticPeopleOut(
                countPeople
        );
    }

    public StatisticOut getStatistic() {
        StatisticPeopleOut statisticPeople = this.getStatisticPeople();
        StatisticProjectOut statisticProject = this.getStatisticProjects();
        StatisticsHotOut statisticsHot = this.getStatisticHot();

        return new StatisticOut(
                statisticPeople,
                statisticProject,
                statisticsHot
        );
    }

    public StatisticsHotOut getStatisticHot() {
        Pageable pageable = PageRequest.of(0, 6, Sort.by("createDate").descending());
        Page<Project> projectSource = projectRepository.findAllByOrderByCreateDateDesc(pageable, null);
        List<StatisticHotProjectOut> projects = mapperProject.toProjectOut(projectSource.getContent());

        Event eventSource = eventRepository.findTopEventByMembers();
        StatisticsEventOut event = mapperEvent.toEventOut(eventSource);

        Role topRole = memberRoleForProjectRepository.getTopRole();
        Role rareRole = memberRoleForProjectRepository.getRareRole();
        Role findRole = actualRoleForProjectRepository.getFindestRole();
        StatisticRoleOut topRoleOut = null;
        if (topRole != null) {
            topRoleOut = new StatisticRoleOut(topRole.getName(), topRole.getCount());
        }
        StatisticRoleOut rareRoleOut = null;
        if (rareRole != null) {
            rareRoleOut = new StatisticRoleOut(rareRole.getName(), rareRole.getCount());
        }
        StatisticRoleOut findRoleOut = null;
        if (findRole != null) {
            findRoleOut = new StatisticRoleOut(findRole.getName(), findRole.getCount());
        }

        return new StatisticsHotOut(
                projects,
                event,
                topRoleOut,
                rareRoleOut,
                findRoleOut
        );
    }
}
