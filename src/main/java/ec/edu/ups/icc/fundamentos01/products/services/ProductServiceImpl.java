package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.core.dtos.PaginationDto;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.BadRequestException;
import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.models.Product;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(
            ProductRepository productRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ProductResponseDto> findAll() {
        return productRepository.findByDeletedFalse().stream()
                .map(Product::fromEntity).map(Product::toResponseDto).toList();
    }

    @Override
    public ProductResponseDto findOne(Long id) {
        ProductEntity entity = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return Product.fromEntity(entity).toResponseDto();
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {
        UserEntity owner = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (owner.isDeleted()) {
            throw new NotFoundException("User not found");
        }

        Set<CategoryEntity> categories = validateAndGetCategories(dto.getCategoryIds());

        productRepository.findByNameIgnoreCaseAndDeletedFalse(dto.getName()).ifPresent(e -> {
            throw new ConflictException("Product name already registered");
        });

        Product product = Product.fromDto(dto);
        ProductEntity entityToSave = product.toEntity();
        entityToSave.setOwner(owner);
        entityToSave.setCategories(categories);

        ProductEntity saved = productRepository.save(entityToSave);
        return Product.fromEntity(saved).toResponseDto();
    }

    @Override
    public ProductResponseDto update(Long id, UpdateProductDto dto) {
        ProductEntity entity = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        Set<CategoryEntity> categories = validateAndGetCategories(dto.getCategoryIds());

        Product product = Product.fromEntity(entity);
        product.update(dto);

        ProductEntity entityToSave = product.toEntity();
        entityToSave.setCategories(categories);

        ProductEntity saved = productRepository.save(entityToSave);
        return Product.fromEntity(saved).toResponseDto();
    }

    @Override
    public ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto) {
        ProductEntity entity = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        Product product = Product.fromEntity(entity);
        product.partialUpdate(dto);
        ProductEntity entityToSave = product.toEntity();

        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            Set<CategoryEntity> categories = validateAndGetCategories(dto.getCategoryIds());
            entityToSave.setCategories(categories);
        }

        ProductEntity saved = productRepository.save(entityToSave);
        return Product.fromEntity(saved).toResponseDto();
    }

    @Override
    public void delete(Long id) {
        ProductEntity entity = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        entity.setDeleted(true);
        productRepository.save(entity);
    }

    @Override
    public List<ProductResponseDto> findByUserId(Long userId) {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) throw new NotFoundException("User not found");
        return productRepository.findByOwner_IdAndDeletedFalse(userId).stream()
                .map(Product::fromEntity).map(Product::toResponseDto).toList();
    }

    @Override
    public List<ProductResponseDto> findByUserIdWithFilters(Long userId, ProductFilterByUserDto filters) {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) {
            throw new NotFoundException("User not found");
        }

        validateUserFilters(filters);
        String name = normalizeName(filters.getName());

        return productRepository.findByOwnerIdWithFilters(
                        userId,
                        name,
                        filters.getMinPrice(),
                        filters.getMaxPrice()
                ).stream()
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findByCategoryIdWithFilters(Long categoryId, ProductFilterByCategoryDto filters) {
        if (!categoryRepository.existsByIdAndDeletedFalse(categoryId)) {
            throw new NotFoundException("Category not found");
        }

        validateCategoryFilters(filters);
        String name = normalizeName(filters.getName());

        return productRepository.findByCategoryIdWithFilters(
                        categoryId,
                        name,
                        filters.getMinPrice(),
                        filters.getMaxPrice(),
                        filters.getUserId()
                ).stream()
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .toList();
    }

    // --- MÉTODOS DE LA PRÁCTICA 10: PAGINACIÓN GENERAL ---

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findAllPage(PaginationDto pagination) {
        Pageable pageable = createPageable(pagination);

        return productRepository.findActivePage(pageable)
                .map(Product::fromEntity)
                .map(Product::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<ProductResponseDto> findAllSlice(PaginationDto pagination) {
        Pageable pageable = createPageable(pagination);

        return productRepository.findActiveSlice(pageable)
                .map(Product::fromEntity)
                .map(Product::toResponseDto);
    }

    // --- MÉTODOS DE LA PRÁCTICA 10: PAGINACIÓN POR CATEGORÍA (ACTIVIDAD FINAL) ---

    /*
     * Retorna productos activos de una categoría usando Page.
     * Mantiene los filtros de la práctica anterior y agrega paginación.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findByCategoryIdWithFiltersPage(
            Long categoryId,
            ProductFilterByCategoryDto filters,
            PaginationDto pagination
    ) {
        if (!categoryRepository.existsByIdAndDeletedFalse(categoryId)) {
            throw new NotFoundException("Category not found");
        }

        validateCategoryFilters(filters);
        String name = normalizeName(filters.getName());
        Pageable pageable = createPageable(pagination);

        return productRepository.findByCategoryIdWithFiltersPage(
                        categoryId,
                        name,
                        filters.getMinPrice(),
                        filters.getMaxPrice(),
                        filters.getUserId(),
                        pageable
                )
                .map(Product::fromEntity)
                .map(Product::toResponseDto);
    }

    /*
     * Retorna productos activos de una categoría usando Slice.
     * No calcula totalElements ni totalPages.
     */
    @Override
    @Transactional(readOnly = true)
    public Slice<ProductResponseDto> findByCategoryIdWithFiltersSlice(
            Long categoryId,
            ProductFilterByCategoryDto filters,
            PaginationDto pagination
    ) {
        if (!categoryRepository.existsByIdAndDeletedFalse(categoryId)) {
            throw new NotFoundException("Category not found");
        }

        validateCategoryFilters(filters);
        String name = normalizeName(filters.getName());
        Pageable pageable = createPageable(pagination);

        return productRepository.findByCategoryIdWithFiltersSlice(
                        categoryId,
                        name,
                        filters.getMinPrice(),
                        filters.getMaxPrice(),
                        filters.getUserId(),
                        pageable
                )
                .map(Product::fromEntity)
                .map(Product::toResponseDto);
    }

    // --- MÉTODOS AUXILIARES (HELPERS) ---

    private Set<CategoryEntity> validateAndGetCategories(Set<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new BadRequestException("Debe seleccionar al menos una categoría");
        }
        Set<CategoryEntity> categories = new HashSet<>();
        for (Long catId : categoryIds) {
            CategoryEntity category = categoryRepository.findById(catId)
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            if (category.isDeleted()) {
                throw new NotFoundException("Category not found");
            }
            categories.add(category);
        }
        return categories;
    }

    private void validateUserFilters(ProductFilterByUserDto filters) {
        if (filters == null) return;
        if (!filters.hasValidPriceRange()) {
            throw new BadRequestException("El precio máximo debe ser mayor o igual al precio mínimo");
        }
    }

    private void validateCategoryFilters(ProductFilterByCategoryDto filters) {
        if (filters == null) return;
        if (!filters.hasValidPriceRange()) {
            throw new BadRequestException("El precio máximo debe ser mayor o igual al precio mínimo");
        }
        if (filters.getUserId() != null && !userRepository.existsByIdAndDeletedFalse(filters.getUserId())) {
            throw new NotFoundException("User not found");
        }
    }

    private String normalizeName(String name) {
        if (name == null || name.isBlank()) return null;
        return name.trim();
    }

    private Pageable createPageable(PaginationDto pagination) {
        String sortBy = normalizeSortBy(pagination.getSortBy());
        Sort.Direction direction = normalizeDirection(pagination.getDirection());
        Sort sort = Sort.by(direction, sortBy);

        return PageRequest.of(
                pagination.getPage(),
                pagination.getSize(),
                sort
        );
    }

    private String normalizeSortBy(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return "id";
        }

        Set<String> allowedFields = Set.of(
                "id",
                "name",
                "price",
                "stock",
                "createdAt",
                "updatedAt"
        );

        if (!allowedFields.contains(sortBy)) {
            throw new BadRequestException("Campo de ordenamiento no permitido: " + sortBy);
        }

        return sortBy;
    }

    private Sort.Direction normalizeDirection(String direction) {
        if (direction == null || direction.isBlank()) {
            return Sort.Direction.ASC;
        }

        if (direction.equalsIgnoreCase("asc")) {
            return Sort.Direction.ASC;
        }

        if (direction.equalsIgnoreCase("desc")) {
            return Sort.Direction.DESC;
        }

        throw new BadRequestException("Dirección de ordenamiento no válida: " + direction);
    }
}