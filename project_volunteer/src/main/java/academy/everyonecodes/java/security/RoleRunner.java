package academy.everyonecodes.java.security;

import academy.everyonecodes.java.service.RoleService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleRunner {
    @Bean
    ApplicationRunner run(RoleService roleService) {
        return args -> roleService.saveAllRolesInDB();
    }
}
