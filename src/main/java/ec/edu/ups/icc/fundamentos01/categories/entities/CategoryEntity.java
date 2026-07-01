package ec.edu.ups.icc.fundamentos01.categories.entities;

import ec.edu.ups.icc.fundamentos01.core.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/*
 * Entidad JPA del recurso categories.
 *
 * Representa la tabla categories en PostgreSQL.
 * Una categoría puede estar asociada a muchos productos,
 * pero en esta práctica la relación se define desde ProductEntity. [cite: 390, 391]
 */
@Entity
@Table(name = "categories")
public class CategoryEntity extends BaseEntity { // Hereda id, createdAt, updatedAt y deleted de BaseEntity

    @Column(nullable = false, unique = true, length = 120)
    private String name; // Nombre único de la categoría [cite: 397]

    @Column(length = 500)
    private String description; // Descripción opcional [cite: 397]

    // Constructor vacío obligatorio para JPA
    public CategoryEntity() {
    }

    // Constructor lleno para facilitar la creación de instancias
    public CategoryEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}