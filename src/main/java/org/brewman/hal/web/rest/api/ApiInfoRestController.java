package org.brewman.hal.web.rest.api;

import javax.inject.Inject;

import org.brewman.hal.domain.api.ApiInfo;
import org.brewman.hal.resource.api.ApiInfoResource;
import org.brewman.hal.resource.api.ApiInfoResourceAssembler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/api/public", produces = "application/hal+json")
public class ApiInfoRestController {

    @Inject
    private ApiInfoResourceAssembler apiInfoResourceAssembler;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public HttpEntity<ApiInfoResource> getApiInfo() {
        return new ResponseEntity<>(
                apiInfoResourceAssembler.toResource(new ApiInfo()),
                HttpStatus.OK);
    }
}
