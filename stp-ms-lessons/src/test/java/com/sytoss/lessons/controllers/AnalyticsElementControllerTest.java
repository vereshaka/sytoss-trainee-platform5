package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.AnalyticsElement;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AnalyticsElementControllerTest extends LessonsControllerTest {

    @Test
    public void shouldUpdateAnalyticsElement() {
        AnalyticsElement analyticsElement = new AnalyticsElement();
        analyticsElement.setDisciplineId(1L);
        analyticsElement.setExamId(1L);
        analyticsElement.setStudentId(1L);
        analyticsElement.setPersonalExamId("1");
        analyticsElement.setGrade(10.0);
        analyticsElement.setTimeSpent(1L);
        when(analyticsService.updateAnalyticsElement(analyticsElement)).thenReturn(analyticsElement);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<AnalyticsElement> requestEntity = new HttpEntity<>(analyticsElement, httpHeaders);

        ResponseEntity<AnalyticsElement> result = doPost("/api/analytics/update", requestEntity, AnalyticsElement.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldMigrateAnalyticsElements() {
        AnalyticsElement analyticsElement = new AnalyticsElement();
        analyticsElement.setDisciplineId(1L);
        analyticsElement.setExamId(1L);
        analyticsElement.setStudentId(1L);
        analyticsElement.setPersonalExamId("1");
        analyticsElement.setGrade(10.0);
        analyticsElement.setTimeSpent(1L);

        when(analyticsService.migrate(1L)).thenReturn(List.of(analyticsElement));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<List<AnalyticsElement>> result = doPost("/api/analytics/migrate/" + 1, requestEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }
}
