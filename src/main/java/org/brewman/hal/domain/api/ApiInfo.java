package org.brewman.hal.domain.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiInfo {

    private static final State DEFAULT_STATE = State.ALPHA;

    private final State state;

    @JsonCreator
    public ApiInfo() {
        this.state = DEFAULT_STATE;
    }

    public ApiInfo(@JsonProperty("state") State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}
