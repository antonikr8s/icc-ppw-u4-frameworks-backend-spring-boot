package ec.edu.ups.icc.fundamentos01.users.entities;

import ec.edu.ups.icc.fundamentos01.core.entities.BaseEntity;
import ec.edu.ups.icc.fundamentos01.security.entities.RoleEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    /*
     * Relación ManyToMany con Roles.
     *
     * fetch = EAGER: necesario porque Spring Security necesita los roles
     * disponibles inmediatamente al autenticar (si fuera LAZY, tendríamos
     * LazyInitializationException al construir el token o el UserDetails
     * fuera de una transacción abierta).
     *
     * Tabla intermedia: user_roles (creada automáticamente por Hibernate).
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    public UserEntity() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Set<RoleEntity> getRoles() { return roles; }
    public void setRoles(Set<RoleEntity> roles) { this.roles = roles; }
}