package ec.edu.ups.icc.fundamentos01.security.enums;

/*
 * Enum que representa los roles disponibles en el sistema.
 *
 * Usar enum en vez de String evita errores de tipeo (ADMIN vs ADMNI)
 * y centraliza los valores válidos en un solo lugar.
 */
public enum RoleName {
    ROLE_USER("Usuario estándar con permisos básicos"),
    ROLE_ADMIN("Administrador con permisos completos");

    private final String description;

    RoleName(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}