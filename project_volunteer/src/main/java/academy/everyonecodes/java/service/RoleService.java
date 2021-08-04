package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final Set<Role> roles;

    public RoleService(RoleRepository roleRepository, Set<Role> roles) {
        this.roleRepository = roleRepository;
        this.roles = roles;
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public Role findByRole(String role) {
        return roleRepository.findByRole(role);
    }

    public void saveAllRolesInDB() {
        for (Role role : roles)
        {
            save(role);
        }
    }
}
