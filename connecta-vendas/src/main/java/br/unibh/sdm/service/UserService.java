package br.unibh.sdm.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.unibh.sdm.entities.User;
import br.unibh.sdm.repository.UserRepository;

@Service
public class UserService {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserRepository userRepo;

    public UserService(UserRepository userRepository){
        this.userRepo=userRepository;
    }

    public User saveUser(User user){
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            String raw = user.getPassword();
            String hashed = passwordEncoder.encode(raw);

            user.setPassword(hashed);
        }

        return this.userRepo.save(user);
    }

    public Iterable<User> getAllUsers() {
        return this.userRepo.findAll();
    }

    public User getUserByCode(String code) {
        return this.userRepo.findByCode(code);
    }

    public User getUserByEmail(String email) {
        return this.userRepo.findByEmail(email);
    }

    public User getUserById(String id) {
        return this.userRepo.findByCode(id);
    }

    public User updateUser(String id, User user) {
        User existing = getUserById(id);
        if (existing == null) {
            return null;
        }

        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setPassword(user.getPassword());

        return this.userRepo.save(existing);
    }

    public boolean deleteUser(String id) {
        User existing = getUserById(id);
        if (existing == null) {
            return false;
        }

        this.userRepo.delete(existing);

        return true;
    }
}
