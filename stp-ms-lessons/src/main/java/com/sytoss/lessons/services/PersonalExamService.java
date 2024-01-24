package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.PersonalExamIntegrationException;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.lessons.connectors.PersonalExamConnector;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonalExamService {

    private final PersonalExamConnector personalExamConnector;

    private final ExecutorService executor;

    public void updatePersonalExams(List<ExamConfiguration> examConfigurations) {
        List<Future<String>> updateResults = new ArrayList<>();
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        examConfigurations.forEach(examConfiguration ->
                updateResults.add(executor.submit(() -> {
                            RequestContextHolder.setRequestAttributes(attributes);
                            return callUpdatePersonalExam(examConfiguration);
                        }
                )));

        handleApiCallResults(updateResults);
    }

    public void createPersonalExams(List<ExamConfiguration> examConfigurations) {
        List<Future<String>> createResults = new ArrayList<>();
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        examConfigurations.forEach(examConfiguration ->
                createResults.add(executor.submit(() -> {
                            RequestContextHolder.setRequestAttributes(attributes);
                            return callCreatePersonalExam(examConfiguration);
                        }
                )));

        handleApiCallResults(createResults);
    }

    // todo: AnastasiiaKravchuk: to think of better error handling approach (Feign.ErrorDecoder)
    private void handleApiCallResults(List<Future<String>> futures) {
        Set<String> errors = futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException ex) {
                        log.error("", ex);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (!errors.isEmpty()) {
            throw new PersonalExamIntegrationException(String.join("; ", errors));
        }
    }

    private String callCreatePersonalExam(ExamConfiguration examConfiguration) {
        try {
            personalExamConnector.create(examConfiguration);
        } catch (Exception e) {
            log.error("Unable to create personal exam for student", e);
            String errorMessage = e.getMessage();
            if (e instanceof FeignException feignException) {
                errorMessage = feignException.contentUTF8();
            }
            return String.format("Unable to create personal exam for student with uid %s and assignee id %d due to %s",
                    examConfiguration.getStudent().getUid(), examConfiguration.getExamAssignee().getId(), errorMessage);
        }
        return null;
    }

    private String callUpdatePersonalExam(ExamConfiguration examConfiguration) {
        try {
            personalExamConnector.update(examConfiguration);
        } catch (Exception e) {
            log.error("Unable to update personal exam for student", e);
            String errorMessage = e.getMessage();
            if (e instanceof FeignException feignException) {
                errorMessage = feignException.contentUTF8();
            }
            return String.format("Unable to update personal exam for student with uid %s and assignee id %d due to %s",
                    examConfiguration.getStudent().getUid(), examConfiguration.getExamAssignee().getId(), errorMessage);
        }
        return null;
    }
}
