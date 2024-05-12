package ru.sstu.studentprofile.web.controller.statistic;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sstu.studentprofile.domain.service.statistic.dto.StatisticOut;
import ru.sstu.studentprofile.domain.service.statistic.dto.StatisticPeopleOut;
import ru.sstu.studentprofile.domain.service.statistic.dto.StatisticProjectOut;
import ru.sstu.studentprofile.domain.service.statistic.dto.StatisticsHotOut;

@Tag(name = "7. Статистика")
@RequestMapping("/statistic")
@CrossOrigin(origins = "*")
public interface StatisticApi {
    @GetMapping("/projects")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получение статистики по проектам",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StatisticProjectOut.class))
            ),
    })
    ResponseEntity<StatisticProjectOut> getStatisticProjects();

    @GetMapping("/people")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получение статистики по людям",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StatisticPeopleOut.class))
            ),
    })
    ResponseEntity<StatisticPeopleOut> getStatisticPeople();

    @GetMapping("")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получение общей статистики",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StatisticOut.class))
            ),
    })
    ResponseEntity<StatisticOut> getStatistic();

    @GetMapping("/hot")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получение горячей статистики",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StatisticsHotOut.class))
            ),
    })
    ResponseEntity<StatisticsHotOut> getStatisticHot();
}
