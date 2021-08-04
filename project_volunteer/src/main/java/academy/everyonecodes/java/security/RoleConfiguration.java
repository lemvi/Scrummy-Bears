package academy.everyonecodes.java.security;

import academy.everyonecodes.java.data.Role;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@ConfigurationProperties("security")
public class RoleConfiguration {
    private Set<Role> roles;

    @Bean
    Set<Role> roles() {
        return roles;
    }

    void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
