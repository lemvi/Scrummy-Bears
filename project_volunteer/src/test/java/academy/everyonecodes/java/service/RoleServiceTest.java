package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Set;

import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RoleServiceTest {

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private Set<Role> roles;

    @Test
    void saveAllRolesInDB() {
        roles.stream()
                .forEach(role -> verify(roleRepository).save(role));
    }
}