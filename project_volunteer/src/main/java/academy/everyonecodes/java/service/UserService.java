package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Role roleVolunteer;
    private final Role roleIndividual;
    private final Role roleCompany;


    public UserService(UserRepository userRepository,
                       Role roleVolunteer,
                       Role roleIndividual,
                       Role roleCompany) {
        this.roleVolunteer = roleVolunteer;
        this.roleIndividual = roleIndividual;
        this.roleCompany = roleCompany;
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
