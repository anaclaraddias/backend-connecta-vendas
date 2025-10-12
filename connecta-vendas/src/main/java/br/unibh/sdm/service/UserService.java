package br.unibh.sdm.service;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.unibh.sdm.entities.User;
import br.unibh.sdm.repository.UserRepository;

@Service
public class UserService {
    private static final Logger logger= LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepo;

    public UserService(UserRepository userRepository){
        this.userRepo=userRepository;
    }

    public User saveUser(User user){
        if(logger.isInfoEnabled()){
            logger.info("Salvando User com os detalhes {}", user.toString());
        }

        return this.userRepo.save(user);
    }
}
