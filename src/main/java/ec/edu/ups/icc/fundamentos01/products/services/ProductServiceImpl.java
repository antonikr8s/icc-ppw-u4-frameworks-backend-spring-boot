package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.BadRequestException; // Asegúrate de tener esta excepción creada
import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.models.Product;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return productRepository.findByDeletedFalse()
                .stream()
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .toList();
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

        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        if (category.isDeleted()) {
            throw new NotFoundException("Category not found");
        }

        productRepository.findByNameIgnoreCaseAndDeletedFalse(dto.getName()).ifPresent(e -> {
            throw new ConflictException("Product name already registered");
        });

        Product product = Product.fromDto(dto);
        ProductEntity entityToSave = product.toEntity();
        entityToSave.setOwner(owner);
        entityToSave.setCategory(category);

        ProductEntity saved = productRepository.save(entityToSave);
        return Product.fromEntity(saved).toResponseDto();
    }

    @Override
    public ProductResponseDto update(Long id, UpdateProductDto dto) {
        ProductEntity entity = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        if (category.isDeleted()) {
            throw new NotFoundException("Category not found");
        }

        Product product = Product.fromEntity(entity);
        product.update(dto);

        ProductEntity entityToSave = product.toEntity();
        entityToSave.setCategory(category);

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

        if (dto.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            if (category.isDeleted()) {
                throw new NotFoundException("Category not found");
            }
            entityToSave.setCategory(category);
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
        if (!userRepository.existsByIdAndDeletedFalse(userId)) {
            throw new NotFoundException("User not found");
        }
        return productRepository.findByOwner_IdAndDeletedFalse(userId)
                .stream()
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findByCategoryId(Long categoryId) {
        if (!categoryRepository.existsByIdAndDeletedFalse(categoryId)) {
            throw new NotFoundException("Category not found");
        }
        return productRepository.findByCategory_IdAndDeletedFalse(categoryId)
                .stream()
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .toList();
    }

    // --- IMPLEMENTACIÓN DE LOS MÉTODOS FILTRADOS DE LA PRÁCTICA 09 ---

    @Override
    public List<ProductResponseDto> findByUserIdWithFilters(Long userId, ProductFilterByUserDto filters) {
        // 1. Validar que el usuario del contexto exista y no esté eliminado
        if (!userRepository.existsByIdAndDeletedFalse(userId)) {
            throw new NotFoundException("User not found");
        }

        // 2. Validar reglas de negocio de los filtros (rango de precios)
        validateFilters(filters);

        // 3. Normalizar el nombre (si viene vacío, se pasa como null para que SQL lo ignore)
        String name = normalizeName(filters.getName());

        // 4. Consultar y mapear resultados usando tu modelo Product
        return productRepository.findByOwnerIdWithFilters(
                        userId,
                        name,
                        filters.getMinPrice(),
                        filters.getMaxPrice(),
                        filters.getCategoryId()
                ).stream()
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findByCategoryIdWithFilters(Long categoryId, ProductFilterByUserDto filters) {
        // 1. Validar que la categoría del contexto exista y no esté eliminada
        if (!categoryRepository.existsByIdAndDeletedFalse(categoryId)) {
            throw new NotFoundException("Category not found");
        }

        // 2. Validar rango de precios
        validateFilters(filters);

        // 3. Normalizar nombre
        String name = normalizeName(filters.getName());

        // 4. Consultar y mapear usando tu modelo Product
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

    private void validateFilters(ProductFilterByUserDto filters) {
        if (filters == null) return;

        // Validar coherencia de rango de precios
        if (!filters.hasValidPriceRange()) {
            throw new BadRequestException("El precio máximo debe ser mayor o igual al precio mínimo");
        }

        // Si se incluye un filtro de categoría cruzado, validar que exista
        if (filters.getCategoryId() != null && !categoryRepository.existsByIdAndDeletedFalse(filters.getCategoryId())) {
            throw new NotFoundException("Category not found");
        }
    }

    private String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        return name.trim();
    }
}