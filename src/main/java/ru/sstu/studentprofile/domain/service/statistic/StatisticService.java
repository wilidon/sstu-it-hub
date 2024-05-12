package ru.sstu.studentprofile.domain.service.statistic;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sstu.studentprofile.data.repository.project.ProjectRepository;
import ru.sstu.studentprofile.data.repository.user.UserRepository;
import ru.sstu.studentprofile.domain.service.statistic.dto.StatisticOut;
import ru.sstu.studentprofile.domain.service.statistic.dto.StatisticPeopleOut;
import ru.sstu.studentprofile.domain.service.statistic.dto.StatisticProjectOut;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class StatisticService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

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
}
