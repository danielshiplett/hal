package org.brewman.hal.resource.api;

import org.brewman.hal.domain.api.ApiInfo;
import org.springframework.hateoas.Resource;

public class ApiInfoResource extends Resource<ApiInfo> {

    public ApiInfoResource(ApiInfo apiInfo) {
        super(apiInfo);
    }

}
