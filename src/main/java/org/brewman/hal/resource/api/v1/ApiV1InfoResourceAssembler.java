package org.brewman.hal.resource.api.v1;

import org.brewman.hal.domain.api.v1.ApiV1Info;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class ApiV1InfoResourceAssembler implements
        ResourceAssembler<ApiV1Info, ApiV1InfoResource> {

    @Override
    public ApiV1InfoResource toResource(ApiV1Info apiV1Info) {
        ApiV1InfoResource resource = new ApiV1InfoResource(apiV1Info);

        /*
         * TODO: Add functions here.
         */
        // resource.add(linkTo(
        // methodOn(TodoRestController.class).getPublicBlogPostById(
        // entity.getId())).withSelfRel());

        return resource;
    }

}
