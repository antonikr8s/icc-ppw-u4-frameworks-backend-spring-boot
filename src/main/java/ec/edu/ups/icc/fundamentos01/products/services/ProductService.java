package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import java.util.List;

public interface ProductService {
    List<ProductResponseDto> findAll();
    ProductResponseDto findOne(Long id);
    ProductResponseDto create(CreateProductDto dto);
    ProductResponseDto update(Long id, UpdateProductDto dto);
    ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto);
    void delete(Long id);

    // --- Métodos de la Práctica 08 ---
    List<ProductResponseDto> findByUserId(Long userId);

    // --- MÉTODOS DE LA PRÁCTICA 09 (Filtros dinámicos) ---
    List<ProductResponseDto> findByUserIdWithFilters(Long userId, ProductFilterByUserDto filters);
    List<ProductResponseDto> findByCategoryIdWithFilters(Long categoryId, ProductFilterByCategoryDto filters);
}