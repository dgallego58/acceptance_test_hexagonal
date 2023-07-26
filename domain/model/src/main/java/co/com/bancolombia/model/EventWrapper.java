package co.com.bancolombia.model;

import java.util.ArrayList;
import java.util.List;

public record EventWrapper(List<String> events) {

    public EventWrapper(List<String> events) {
        this.events = events == null ? new ArrayList<>() : events;
    }
}
