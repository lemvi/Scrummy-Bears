package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.RoleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private Set<Role> roles;

    private Role mockRole = new Role();

    @Test
    void saveAllRolesInDB() {
        //TODO
    }


}