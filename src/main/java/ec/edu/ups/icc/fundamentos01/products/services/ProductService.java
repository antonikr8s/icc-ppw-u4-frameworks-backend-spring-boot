package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.core.dtos.PaginationDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

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

    // --- Métodos de la Práctica 09 (Filtros dinámicos) ---
    List<ProductResponseDto> findByUserIdWithFilters(Long userId, ProductFilterByUserDto filters);
    List<ProductResponseDto> findByCategoryIdWithFilters(Long categoryId, ProductFilterByCategoryDto filters);

    // --- Métodos de la Práctica 10 (Paginación general) ---
    Page<ProductResponseDto> findAllPage(PaginationDto pagination);
    Slice<ProductResponseDto> findAllSlice(PaginationDto pagination);

    // --- Métodos de la Práctica 10 (Actividad final: paginación por categoría) ---

    /*
     * Retorna productos de una categoría con filtros y Page.
     */
    Page<ProductResponseDto> findByCategoryIdWithFiltersPage(
            Long categoryId,
            ProductFilterByCategoryDto filters,
            PaginationDto pagination
    );

    /*
     * Retorna productos de una categoría con filtros y Slice.
     */
    Slice<ProductResponseDto> findByCategoryIdWithFiltersSlice(
            Long categoryId,
            ProductFilterByCategoryDto filters,
            PaginationDto pagination
    );
}