package com.sytoss.traineeplatform.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonUtils {

    public static String sequenceToStringInJSON(SequenceOfStops sequenceOfStops) {

        if (sequenceOfStops == null || sequenceOfStops.getStopFloors().isEmpty()) {
            return null;
        }

        String sequenceOfStopsToJSON;

        try {
            sequenceOfStopsToJSON = new ObjectMapper().writeValueAsString(sequenceOfStops);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return sequenceOfStopsToJSON;
    }

    public static String orderSequenceToStringInJSON(List<SequenceOfStops> orderSequenceOfStops) {
        if (orderSequenceOfStops == null) {
            return null;
        }

        String orderSequenceOfStopsToJSON;

        try {
            orderSequenceOfStopsToJSON = new ObjectMapper().writeValueAsString(orderSequenceOfStops);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return orderSequenceOfStopsToJSON;
    }

    public static List<SequenceOfStops> convertOrderSequenceOfStopsFromJson(String json){

        if (json == null) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, SequenceOfStops.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
