package org.brewman.hateos.web.rest;

import javax.inject.Inject;

import org.brewman.hateos.domain.Todo;
import org.brewman.hateos.repository.TodoRepository;
import org.brewman.hateos.resource.NewTodoResource;
import org.brewman.hateos.resource.TodoResourceAssembler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/api", produces = "application/hal+json")
public class TodoRestController {

    private static final int DEFAULT_RETURN_RECORD_COUNT = 20;

    @Inject
    private TodoRepository todoRepository;

    @Inject
    private TodoResourceAssembler todoResourceAssembler;

    @RequestMapping(method = RequestMethod.GET, value = "/public/todos")
    public HttpEntity<PagedResources<NewTodoResource>> getPublicTodos(
            @PageableDefault(size = DEFAULT_RETURN_RECORD_COUNT, page = 0) Pageable pageable,
            PagedResourcesAssembler<Todo> assembler) {

        Page<Todo> todos = todoRepository.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(todos,
                todoResourceAssembler), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/public/todos/{todoId}")
    public HttpEntity<NewTodoResource> getPublicBlogPostById(
            @PathVariable("todoId") Long id) throws ResourceNotFoundException {

        Todo todo = todoRepository.findOne(id);
        NewTodoResource resource = todoResourceAssembler.toResource(todo);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
