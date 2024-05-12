package ru.sstu.studentprofile.domain.service.statistic;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sstu.studentprofile.data.models.event.Event;
import ru.sstu.studentprofile.data.models.project.Project;
import ru.sstu.studentprofile.data.repository.event.EventRepository;
import ru.sstu.studentprofile.data.repository.project.ActualRoleForProjectRepository;
import ru.sstu.studentprofile.data.repository.project.MemberRoleForProjectRepository;
import ru.sstu.studentprofile.data.repository.project.ProjectMemberRepository;
import ru.sstu.studentprofile.data.repository.project.ProjectRepository;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
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
    private final MemberRoleForProjectRepository memberRoleForProjectRepository;
    private final ActualRoleForProjectRepository actualRoleForProjectRepository;
    private final EventRepository eventRepository;
    private final StatisitcProjectMapper mapperProject;
    private final StatisticEventMapper mapperEvent;

    public StatisticProjectOut getStatisticProjects(){
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

    public StatisticPeopleOut getStatisticPeople(){
        long countPeople = userRepository.getCountAllUsers();
        return new StatisticPeopleOut(
                countPeople
        );
    }

    public StatisticOut getStatistic(){
        StatisticPeopleOut statisticPeople = this.getStatisticPeople();
        StatisticProjectOut statisticProject = this.getStatisticProjects();

        return new StatisticOut(
                statisticPeople,
                statisticProject
        );
    }

    public StatisticsHotOut getStatisticHot(){
        Pageable pageable = PageRequest.of(0, 6, Sort.by("createDate").descending());
        List<Project> projectSource = projectRepository.findAllByOrderByCreateDateDesc(pageable);
        List<StatisticHotProjectOut> projects = mapperProject.toProjectOut(projectSource);

        Event eventSource = eventRepository.findTopEventByMembers();
        StatisticsEventOut event = mapperEvent.toEventOut(eventSource);

        String topRole = memberRoleForProjectRepository.getTopRole();
        String rareRole = memberRoleForProjectRepository.getRareRole();
        String findRole = actualRoleForProjectRepository.getFindestRole();

        return new StatisticsHotOut(
                projects,
                event,
                topRole,
                rareRole,
                findRole
        );
    }
}
