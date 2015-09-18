package org.brewman.hateos.web.rest;

import org.brewman.hateos.Application;
import org.brewman.hateos.domain.Todo;
import org.brewman.hateos.repository.TodoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TodoResource REST controller.
 *
 * @see TodoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TodoResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final DateTime DEFAULT_DUE_AT = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DUE_AT = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DUE_AT_STR = dateTimeFormatter.print(DEFAULT_DUE_AT);

    @Inject
    private TodoRepository todoRepository;

    private MockMvc restTodoMockMvc;

    private Todo todo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TodoResource todoResource = new TodoResource();
        ReflectionTestUtils.setField(todoResource, "todoRepository", todoRepository);
        this.restTodoMockMvc = MockMvcBuilders.standaloneSetup(todoResource).build();
    }

    @Before
    public void initTest() {
        todo = new Todo();
        todo.setTitle(DEFAULT_TITLE);
        todo.setDescription(DEFAULT_DESCRIPTION);
        todo.setDueAt(DEFAULT_DUE_AT);
    }

    @Test
    @Transactional
    public void createTodo() throws Exception {
        int databaseSizeBeforeCreate = todoRepository.findAll().size();

        // Create the Todo
        restTodoMockMvc.perform(post("/api/todos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(todo)))
                .andExpect(status().isCreated());

        // Validate the Todo in the database
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(databaseSizeBeforeCreate + 1);
        Todo testTodo = todos.get(todos.size() - 1);
        assertThat(testTodo.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTodo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTodo.getDueAt().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_DUE_AT);
    }

    @Test
    @Transactional
    public void getAllTodos() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        // Get all the todos
        restTodoMockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(todo.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].dueAt").value(hasItem(DEFAULT_DUE_AT_STR)));
    }

    @Test
    @Transactional
    public void getTodo() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        // Get the todo
        restTodoMockMvc.perform(get("/api/todos/{id}", todo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(todo.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dueAt").value(DEFAULT_DUE_AT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingTodo() throws Exception {
        // Get the todo
        restTodoMockMvc.perform(get("/api/todos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTodo() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

		int databaseSizeBeforeUpdate = todoRepository.findAll().size();

        // Update the todo
        todo.setTitle(UPDATED_TITLE);
        todo.setDescription(UPDATED_DESCRIPTION);
        todo.setDueAt(UPDATED_DUE_AT);
        restTodoMockMvc.perform(put("/api/todos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(todo)))
                .andExpect(status().isOk());

        // Validate the Todo in the database
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(databaseSizeBeforeUpdate);
        Todo testTodo = todos.get(todos.size() - 1);
        assertThat(testTodo.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTodo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTodo.getDueAt().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_DUE_AT);
    }

    @Test
    @Transactional
    public void deleteTodo() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

		int databaseSizeBeforeDelete = todoRepository.findAll().size();

        // Get the todo
        restTodoMockMvc.perform(delete("/api/todos/{id}", todo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
