package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.core.dtos.PaginationDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.security.services.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> findAll();

    ProductResponseDto findOne(Long id);

    // --- Práctica 13: ahora reciben currentUser ---
    ProductResponseDto create(CreateProductDto dto, UserDetailsImpl currentUser);

    ProductResponseDto update(Long id, UpdateProductDto dto, UserDetailsImpl currentUser);

    ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto, UserDetailsImpl currentUser);

    void delete(Long id, UserDetailsImpl currentUser);
    // --- fin cambios Práctica 13 ---

    List<ProductResponseDto> findByUserId(Long userId);

    List<ProductResponseDto> findByUserIdWithFilters(Long userId, ProductFilterByUserDto filters);

    List<ProductResponseDto> findByCategoryIdWithFilters(Long categoryId, ProductFilterByCategoryDto filters);

    Page<ProductResponseDto> findAllPage(PaginationDto pagination);

    // --- LÍNEA MODIFICADA ---
    Slice<ProductResponseDto> findAllSlice(PaginationDto pagination, UserDetailsImpl currentUser);

    Page<ProductResponseDto> findByCategoryIdWithFiltersPage(
            Long categoryId, ProductFilterByCategoryDto filters, PaginationDto pagination);

    Slice<ProductResponseDto> findByCategoryIdWithFiltersSlice(
            Long categoryId, ProductFilterByCategoryDto filters, PaginationDto pagination);
}