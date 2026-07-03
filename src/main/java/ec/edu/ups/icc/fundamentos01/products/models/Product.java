package ec.edu.ups.icc.fundamentos01.products.models;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;

// Importaciones necesarias para las relaciones
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoryResponseDto;

import java.util.List;
import java.util.Set;

public class Product {

    private Long id;
    private String name;
    private Double price;
    private Integer stock;
    private boolean deleted;

    // Relaciones agregadas
    private UserEntity owner;

    // Cambiado de un objeto individual a una colección (Set)
    private Set<CategoryEntity> categories;

    public Product() {}

    // Factory: DTO → Product
    public static Product fromDto(CreateProductDto dto) {
        Product p = new Product();
        p.name = dto.getName();
        p.price = dto.getPrice();
        p.stock = dto.getStock();
        return p;
    }

    // Factory: Entity → Product
    public static Product fromEntity(ProductEntity entity) {
        Product p = new Product();
        p.id = entity.getId();
        p.name = entity.getName();
        p.price = entity.getPrice();
        p.stock = entity.getStock();
        p.deleted = entity.isDeleted();

        // Extraer relaciones de la entidad
        p.owner = entity.getOwner();
        p.categories = entity.getCategories(); // Extrae la colección
        return p;
    }

    // Product → Entity
    public ProductEntity toEntity() {
        ProductEntity entity = new ProductEntity();
        if (this.id != null) entity.setId(this.id);
        entity.setName(this.name);
        entity.setPrice(this.price);
        entity.setStock(this.stock);

        // Pasar las relaciones a la entidad
        entity.setOwner(this.owner);
        entity.setCategories(this.categories); // Pasa la colección

        return entity;
    }

    // Product → ResponseDto
    public ProductResponseDto toResponseDto() {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setPrice(this.price);
        dto.setStock(this.stock);

        // Mapeo seguro para evitar los NULLs en la respuesta anidada
        if (this.owner != null) {
            UserResponseDto ownerDto = new UserResponseDto();
            ownerDto.setId(this.owner.getId());
            ownerDto.setName(this.owner.getName());
            ownerDto.setEmail(this.owner.getEmail());
            dto.setOwner(ownerDto);
        }

        // Nuevo mapeo: Convierte el Set de CategoryEntity en una List de CategoryResponseDto
        if (this.categories != null) {
            List<CategoryResponseDto> categoryDtos = this.categories.stream().map(cat -> {
                CategoryResponseDto catDto = new CategoryResponseDto();
                catDto.setId(cat.getId());
                catDto.setName(cat.getName());
                catDto.setDescription(cat.getDescription());
                return catDto;
            }).toList();
            dto.setCategories(categoryDtos);
        }

        return dto;
    }

    // Actualización completa
    public void update(UpdateProductDto dto) {
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.stock = dto.getStock();
    }

    // Actualización parcial
    public void partialUpdate(PartialUpdateProductDto dto) {
        if (dto.getName() != null) this.name = dto.getName();
        if (dto.getPrice() != null) this.price = dto.getPrice();
        if (dto.getStock() != null) this.stock = dto.getStock();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    // Getters y Setters de las relaciones actualizados
    public UserEntity getOwner() { return owner; }
    public void setOwner(UserEntity owner) { this.owner = owner; }
    public Set<CategoryEntity> getCategories() { return categories; }
    public void setCategories(Set<CategoryEntity> categories) { this.categories = categories; }
}