package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final Set<Role> roles;

    public RoleService(RoleRepository roleRepository, Set<Role> roles) {
        this.roleRepository = roleRepository;
        this.roles = roles;
    }

    public void saveAllRolesInDB() throws SQLException {
        List<Role> allRolesFromDb = roleRepository.findAll();
        for (Role role : roles)
        {
            if (!allRolesFromDb.contains(role))
                roleRepository.save(role);
        }
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/project_volunteer?useSSL=false", "root", "root");
        String sql = "SET GLOBAL max_connections=2048";
        Statement statement = con.createStatement();
        statement.execute(sql);
        con.close();
    }
}
