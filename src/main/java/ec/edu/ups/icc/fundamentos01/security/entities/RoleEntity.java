package ec.edu.ups.icc.fundamentos01.security.entities;

import ec.edu.ups.icc.fundamentos01.core.entities.BaseEntity;
import ec.edu.ups.icc.fundamentos01.security.enums.RoleName;
import jakarta.persistence.*;

/*
 * Entidad que representa un rol del sistema (ROLE_USER, ROLE_ADMIN).
 *
 * Tabla en BD: roles
 * Se relaciona ManyToMany con UserEntity (tabla intermedia user_roles).
 */
@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @Column(length = 200)
    private String description;

    public RoleEntity() {}

    public RoleEntity(RoleName name, String description) {
        this.name = name;
        this.description = description;
    }

    public RoleName getName() { return name; }
    public void setName(RoleName name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
