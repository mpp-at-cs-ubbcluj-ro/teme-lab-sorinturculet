package ro.mpp2024.server;

import ro.mpp2024.models.User;
import ro.mpp2024.repository.IUserRepository;

import java.util.Optional;

public class LoginService {
    private final IUserRepository userRepo;

    public LoginService(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> login(String username, String password) {
        return userRepo.authenticate(username, password);
    }
}
