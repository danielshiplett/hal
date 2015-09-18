package org.brewman.hateos.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.brewman.hateos.domain.Todo;
import org.brewman.hateos.web.rest.TodoRestController;
import org.brewman.hateos.web.rest.UserResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class TodoResourceAssembler implements
        ResourceAssembler<Todo, NewTodoResource> {

    @Override
    public NewTodoResource toResource(Todo entity) {
        NewTodoResource resource = new NewTodoResource(entity);

        /*
         * Set the owner field in the resource.
         */
        if (entity.getOwner() != null) {
            resource.setOwner(entity.getOwner().getLogin());
        }

        /*
         * Add a link to the specific Todo.
         */
        resource.add(linkTo(
                methodOn(TodoRestController.class).getPublicBlogPostById(
                        entity.getId())).withSelfRel());

        /*
         * Add a link to the owner of the Todo.
         */
        if (entity.getOwner() != null) {
            Link userLink = linkTo(
                    methodOn(UserResource.class).getUser(
                            entity.getOwner().getLogin())).withRel(
                    NewTodoResource.LINK_NAME_OWNER);
            resource.add(userLink);
        }

        return resource;
    }
}
