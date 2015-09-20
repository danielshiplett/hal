package org.brewman.hal.resource.api.v1;

import org.brewman.hal.domain.api.v1.ApiV1Info;
import org.springframework.hateoas.Resource;

public class ApiV1InfoResource extends Resource<ApiV1Info> {

    public ApiV1InfoResource(ApiV1Info apiV1Info) {
        super(apiV1Info);
    }
}
