package org.brewman.hateos.repository;

import java.util.List;

import org.brewman.hateos.domain.Todo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the Todo entity.
 */
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("select todo from Todo todo where todo.owner.login = ?#{principal.username}")
    List<Todo> findAllForCurrentUser(Pageable pageable);

}
