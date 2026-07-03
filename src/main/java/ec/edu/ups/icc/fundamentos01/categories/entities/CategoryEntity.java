package ec.edu.ups.icc.fundamentos01.categories.entities;

import ec.edu.ups.icc.fundamentos01.core.entities.BaseEntity;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/*
 * Entidad JPA del recurso categories.
 *
 * Representa la tabla categories en PostgreSQL.
 * Una categoría puede estar asociada a muchos productos.
 */
@Entity
@Table(name = "categories")
public class CategoryEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(length = 500)
    private String description;

    /*
     * Relación inversa con productos.
     * mappedBy indica que la relación principal se define
     * en el atributo categories de ProductEntity
     */
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<ProductEntity> products = new HashSet<>();

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

    public Set<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductEntity> products) {
        this.products = products;
    }
}