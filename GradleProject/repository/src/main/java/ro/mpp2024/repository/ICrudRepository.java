package ro.mpp2024.repository;

import java.util.List;
import java.util.Optional;

/**
 * A generic interface for CRUD operations.
 *
 * @param <T>  the type of the entity
 * @param <ID> the type of the entity's identifier
 */
public interface ICrudRepository<T, ID> {

    T create(T entity);

    Optional<T> read(ID id);

    T update(T entity);

    List<T> findAll();
}
