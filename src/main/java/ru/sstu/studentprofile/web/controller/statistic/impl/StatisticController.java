package ru.sstu.studentprofile.web.controller.statistic.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.studentprofile.domain.service.statistic.StatisticService;
import ru.sstu.studentprofile.domain.service.statistic.dto.StatisticOut;
import ru.sstu.studentprofile.domain.service.statistic.dto.StatisticPeopleOut;
import ru.sstu.studentprofile.domain.service.statistic.dto.StatisticProjectOut;
import ru.sstu.studentprofile.domain.service.statistic.dto.StatisticsHotOut;
import ru.sstu.studentprofile.web.controller.statistic.StatisticApi;

@RestController
@AllArgsConstructor
@RequestMapping("/statistic")
@CrossOrigin("*")
public class StatisticController implements StatisticApi {
    private final StatisticService statisticService;

    @Override
    public ResponseEntity<StatisticProjectOut> getStatisticProjects() {
        return ResponseEntity.ok(statisticService.getStatisticProjects());
    }

    @Override
    public ResponseEntity<StatisticPeopleOut> getStatisticPeople() {
        return ResponseEntity.ok(statisticService.getStatisticPeople());
    }

    @Override
    public ResponseEntity<StatisticOut> getStatistic() {
        return ResponseEntity.ok(statisticService.getStatistic());
    }

    @Override
    public ResponseEntity<StatisticsHotOut> getStatisticHot() {
        return ResponseEntity.ok(statisticService.getStatisticHot());
    }
}
