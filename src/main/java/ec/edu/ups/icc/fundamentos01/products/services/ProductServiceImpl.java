package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.models.Product; // Asegúrate de que coincida con tu paquete real
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
        // 1. Validar existencia y estado del usuario
        UserEntity owner = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (owner.isDeleted()) {
            throw new NotFoundException("User not found");
        }

        // 2. Validar existencia y estado de la categoría
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        if (category.isDeleted()) {
            throw new NotFoundException("Category not found");
        }

        // 3. Validar duplicados por nombre
        productRepository.findByNameIgnoreCaseAndDeletedFalse(dto.getName()).ifPresent(e -> {
            throw new ConflictException("Product name already registered");
        });

        // 4. Mapear y asignar relaciones explícitamente a la entidad
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

        // Validar la nueva categoría
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        if (category.isDeleted()) {
            throw new NotFoundException("Category not found");
        }

        Product product = Product.fromEntity(entity);
        product.update(dto);

        ProductEntity entityToSave = product.toEntity();
        entityToSave.setCategory(category); // Actualiza la relación de categoría

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

        // Validar y cambiar categoría solo si viene en el DTO
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

    // --- NUEVOS MÉTODOS REQUERIDOS POR LA PRÁCTICA 08 ---

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
}