package com.sytoss.checktask.stp.controller;

import com.sytoss.checktask.stp.service.ScoreService;
import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.personalexam.CheckRequestParameters;
import com.sytoss.domain.bom.personalexam.CheckTaskParameters;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.domain.bom.personalexam.Score;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/task/")
public class CheckTaskController {

    private final ScoreService scoreService;

    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "Success|OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "")),
                    @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(mediaType = ""))})
    @PostMapping("check")
    public Score check(
            @RequestBody CheckTaskParameters body) {
        return scoreService.checkAndScore(body);
    }

    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "Success|OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "")),
                    @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(mediaType = ""))})
    @PostMapping("check-etalon")
    public IsCheckEtalon checkEtalon(
            @RequestBody CheckRequestParameters body) {
        return scoreService.checkEtalon(body);
    }

    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "Success|OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "")),
                    @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(mediaType = ""))})
    @PostMapping("check-request")
    public QueryResult checkRequest(
            @RequestBody CheckRequestParameters body) {
        return scoreService.checkRequest(body);
    }

    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "Success|OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "")),
                    @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(mediaType = ""))})
    @PostMapping("check-validation")
    public boolean checkValidation(
            @RequestBody String script) {
        return scoreService.checkValidation(script);
    }
}
