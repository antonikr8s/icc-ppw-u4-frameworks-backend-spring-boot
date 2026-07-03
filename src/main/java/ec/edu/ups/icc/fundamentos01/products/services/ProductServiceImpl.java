package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.BadRequestException;
import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.models.Product;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

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

    /*
     * Crea un producto asociado a un usuario y a varias categorías[cite: 273].
     */
    @Override
    public ProductResponseDto create(CreateProductDto dto) {
        UserEntity owner = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (owner.isDeleted()) {
            throw new NotFoundException("User not found");
        }

        // Valida que las categorías existan y no estén eliminadas [cite: 274, 275]
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

        // Reemplaza todas las categorías asociadas al producto [cite: 276]
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
            // Reemplaza todas las categorías si categoryIds viene con valor [cite: 277]
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

    // --- IMPLEMENTACIÓN DE LOS MÉTODOS FILTRADOS (FASE B y C) ---

    @Override
    public List<ProductResponseDto> findByUserIdWithFilters(Long userId, ProductFilterByUserDto filters) {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) {
            throw new NotFoundException("User not found");
        }

        validateUserFilters(filters);
        String name = normalizeName(filters.getName());

        // Se elimina el filtro categoryId para mantener la semántica de la ruta [cite: 288]
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

    /*
     * Retorna productos activos de una categoría aplicando filtros opcionales[cite: 260].
     * Consulta los productos desde ProductRepository[cite: 261, 262].
     */
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

    // --- MÉTODOS AUXILIARES (HELPERS) ---

    /*
     * Valida que todas las categorías existan y estén activas[cite: 267, 268].
     * Retorna el conjunto de entidades CategoryEntity[cite: 269].
     */
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

    /*
     * Valida reglas de negocio relacionadas con filtros[cite: 184].
     */
    private void validateUserFilters(ProductFilterByUserDto filters) {
        if (filters == null) return;
        if (!filters.hasValidPriceRange()) {
            throw new BadRequestException("El precio máximo debe ser mayor o igual al precio mínimo");
        }
    }

    /*
     * Valida reglas de negocio relacionadas con filtros usados desde el contexto de categoría[cite: 266].
     */
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
}