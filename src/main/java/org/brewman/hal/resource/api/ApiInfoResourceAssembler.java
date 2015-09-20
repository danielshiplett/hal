package org.brewman.hal.resource.api;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.brewman.hal.domain.api.ApiInfo;
import org.brewman.hal.web.rest.api.v1.ApiV1InfoRestController;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class ApiInfoResourceAssembler implements
        ResourceAssembler<ApiInfo, ApiInfoResource> {

    @Override
    public ApiInfoResource toResource(ApiInfo apiInfo) {
        ApiInfoResource resource = new ApiInfoResource(apiInfo);

        /*
         * TODO: Add functions here.
         */
        resource.add(linkTo(
                methodOn(ApiV1InfoRestController.class).getApiV1Info())
                .withSelfRel());

        return resource;
    }
}
