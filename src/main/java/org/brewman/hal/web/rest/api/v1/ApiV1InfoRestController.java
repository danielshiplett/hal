package org.brewman.hal.web.rest.api.v1;

import javax.inject.Inject;

import org.brewman.hal.domain.api.v1.ApiV1Info;
import org.brewman.hal.resource.api.v1.ApiV1InfoResource;
import org.brewman.hal.resource.api.v1.ApiV1InfoResourceAssembler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/api/public/v1", produces = "application/hal+json")
public class ApiV1InfoRestController {

    @Inject
    private ApiV1InfoResourceAssembler apiV1InfoResourceAssembler;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public HttpEntity<ApiV1InfoResource> getApiV1Info() {
        return new ResponseEntity<>(
                apiV1InfoResourceAssembler.toResource(new ApiV1Info()),
                HttpStatus.OK);
    }
}
