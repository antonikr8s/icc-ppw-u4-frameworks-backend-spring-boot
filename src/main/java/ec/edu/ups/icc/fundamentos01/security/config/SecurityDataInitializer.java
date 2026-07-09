package ec.edu.ups.icc.fundamentos01.security.config;

import ec.edu.ups.icc.fundamentos01.security.entities.RoleEntity;
import ec.edu.ups.icc.fundamentos01.security.enums.RoleName;
import ec.edu.ups.icc.fundamentos01.security.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/*
 * Crea los roles base (ROLE_USER, ROLE_ADMIN) al iniciar la aplicación,
 * si todavía no existen. Sin esto, el registro fallaría porque AuthService
 * no encontraría ROLE_USER para asignar por defecto.
 */
@Component
public class SecurityDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public SecurityDataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        createRoleIfNotExists(RoleName.ROLE_USER, "Usuario estándar del sistema");
        createRoleIfNotExists(RoleName.ROLE_ADMIN, "Administrador del sistema");
    }

    private void createRoleIfNotExists(RoleName roleName, String description) {
        if (!roleRepository.existsByName(roleName)) {
            RoleEntity role = new RoleEntity(roleName, description);
            roleRepository.save(role);
        }
    }
}