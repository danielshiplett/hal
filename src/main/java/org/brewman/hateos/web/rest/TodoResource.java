package org.brewman.hateos.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.brewman.hateos.domain.Todo;
import org.brewman.hateos.repository.TodoRepository;
import org.brewman.hateos.repository.UserRepository;
import org.brewman.hateos.security.SecurityUtils;
import org.brewman.hateos.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Todo.
 */
@RestController
@RequestMapping("/api")
public class TodoResource {

    private final Logger log = LoggerFactory.getLogger(TodoResource.class);

    @Inject
    private TodoRepository todoRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST /todos -> Create a new todo.
     */
    @RequestMapping(value = "/todos", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Todo todo)
            throws URISyntaxException {
        log.debug("REST request to save Todo : {}", todo);

        if (todo.getId() != null) {
            return ResponseEntity.badRequest()
                    .header("Failure", "A new todo cannot already have an ID")
                    .build();
        }

        todo.setOwner(userRepository.findOneByLogin(
                SecurityUtils.getCurrentLogin()).get());
        todo = todoRepository.save(todo);

        return ResponseEntity.created(new URI("/api/todos/" + todo.getId()))
                .build();
    }

    /**
     * PUT /todos -> Updates an existing todo.
     */
    @RequestMapping(value = "/todos", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Todo todo)
            throws URISyntaxException {
        log.debug("REST request to update Todo : {}", todo);
        if (todo.getId() == null) {
            return create(todo);
        }
        todoRepository.save(todo);
        return ResponseEntity.ok().build();
    }

    /**
     * GET /todos -> get all the todos.
     */
    @RequestMapping(value = "/todos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Todo>> getAll(
            @RequestParam(value = "page", required = false) Integer offset,
            @RequestParam(value = "per_page", required = false) Integer limit)
            throws URISyntaxException {
        Page<Todo> page = todoRepository.findAll(PaginationUtil
                .generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
                page, "/api/todos", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /todos/:id -> get the "id" todo.
     */
    @RequestMapping(value = "/todos/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Todo> get(@PathVariable Long id) {
        log.debug("REST request to get Todo : {}", id);
        return Optional.ofNullable(todoRepository.findOne(id))
                .map(todo -> new ResponseEntity<>(todo, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /todos/:id -> delete the "id" todo.
     */
    @RequestMapping(value = "/todos/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Todo : {}", id);
        todoRepository.delete(id);
    }
}
