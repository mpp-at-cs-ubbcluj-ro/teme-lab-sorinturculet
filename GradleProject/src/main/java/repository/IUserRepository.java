package repository;

import models.User;
import java.util.List;
import java.util.Optional;

public interface IUserRepository extends ICrudRepository<User, Integer> {
    Optional<User> authenticate(String username, String password);
    Optional<User> read(Integer id);
    List<User> findAll();
}
