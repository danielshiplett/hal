package org.brewman.hal.domain.api.v1;

import org.brewman.hal.domain.api.ApiInfo;
import org.brewman.hal.domain.api.State;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ApiV1Info extends ApiInfo {

    private static final State DEFAULT_STATE = State.ALPHA;

    @JsonCreator
    public ApiV1Info() {
        super(DEFAULT_STATE);
    }
}
