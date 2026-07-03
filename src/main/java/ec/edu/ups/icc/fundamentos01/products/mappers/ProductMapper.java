package ec.edu.ups.icc.fundamentos01.products.mappers;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.models.ProductModel;

public class ProductMapper {

    // DTO → Model
    public static ProductModel toModelFromDTO(CreateProductDto dto) {
        ProductModel model = new ProductModel();
        model.setName(dto.getName());
        model.setPrice(dto.getPrice());
        model.setStock(dto.getStock());
        return model;
    }

    // Entity → Model
    public static ProductModel toModelFromEntity(ProductEntity entity) {
        ProductModel model = new ProductModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setPrice(entity.getPrice());
        model.setStock(entity.getStock());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setDeleted(entity.isDeleted());

        // Conversión a Lista para el modelo
        if (entity.getCategories() != null) {
            // Nota: Si tu ProductModel usa List<CategoryEntity>, esto funcionará directo.
            // Si usa otra cosa, debes mapearlo acorde.
            // model.setCategories(entity.getCategories().stream().toList());
        }

        return model;
    }

    // Model → Entity
    public static ProductEntity toEntityFromModel(ProductModel model) {
        ProductEntity entity = new ProductEntity();
        entity.setName(model.getName());
        entity.setPrice(model.getPrice());
        entity.setStock(model.getStock());
        return entity;
    }

    // Model → ResponseDto
    public static ProductResponseDto toResponse(ProductModel model) {
        ProductResponseDto response = new ProductResponseDto();
        response.setId(model.getId());
        response.setName(model.getName());
        response.setPrice(model.getPrice());
        response.setStock(model.getStock());

        // Aquí iría el mapeo de model.getCategories() a response.setCategories()
        // tal como indica la guía en el paso 17.

        return response;
    }
}