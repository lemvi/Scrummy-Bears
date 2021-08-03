package academy.everyonecodes.java.security;

import academy.everyonecodes.java.data.Role;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("roles")
public class RoleConfiguration {
    private Role roleVolunteer;
    private Role roleIndividual;
    private Role roleCompany;

    @Bean
    Role roleVolunteer() {
        return roleVolunteer;
    }

    @Bean
    Role roleIndividual() {
        return roleIndividual;
    }

    @Bean
    Role roleCompany() {
        return roleCompany;
    }

    void setRoleVolunteer(Role roleVolunteer) {
        this.roleVolunteer = roleVolunteer;
    }

    void setRoleIndividual(Role roleIndividual) {
        this.roleIndividual = roleIndividual;
    }

    void setRoleCompany(Role roleCompany) {
        this.roleCompany = roleCompany;
    }
}
