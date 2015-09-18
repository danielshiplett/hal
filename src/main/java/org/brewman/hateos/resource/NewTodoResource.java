package org.brewman.hateos.resource;

import org.brewman.hateos.domain.Todo;
import org.springframework.hateoas.Resource;

public class NewTodoResource extends Resource<Todo> {

    public static final String LINK_NAME_OWNER = "owner";

    private String owner;

    public NewTodoResource(Todo todo) {
        super(todo);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
