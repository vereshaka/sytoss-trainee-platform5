package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Rating;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RatingControllerTest extends LessonsControllerTest {

    @Test
    public void shouldUpdateRating() {
        Rating rating = new Rating();
        rating.setDisciplineId(1L);
        rating.setExamId(1L);
        rating.setStudentId(1L);
        rating.setPersonalExamId("1");
        rating.setGrade(10.0);
        rating.setTimeSpent(1L);
        when(ratingService.updateRating(rating)).thenReturn(rating);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Rating> requestEntity = new HttpEntity<>(rating, httpHeaders);

        ResponseEntity<Rating> result = doPost("/api/rating/update", requestEntity, Rating.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldMigrateRating() {
        Rating rating = new Rating();
        rating.setDisciplineId(1L);
        rating.setExamId(1L);
        rating.setStudentId(1L);
        rating.setPersonalExamId("1");
        rating.setGrade(10.0);
        rating.setTimeSpent(1L);

        when(ratingService.migrate(1L)).thenReturn(List.of(rating));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<List<Rating>> result = doPost("/api/rating/migrate/" + 1, requestEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }
}
