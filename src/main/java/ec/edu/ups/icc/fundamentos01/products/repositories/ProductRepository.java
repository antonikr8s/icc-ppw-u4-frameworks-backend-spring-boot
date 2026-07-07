package ec.edu.ups.icc.fundamentos01.products.repositories;

import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByNameIgnoreCaseAndDeletedFalse(String name);
    List<ProductEntity> findByDeletedFalse();
    Optional<ProductEntity> findByIdAndDeletedFalse(Long id);
    List<ProductEntity> findByOwner_IdAndDeletedFalse(Long ownerId);

    // --- MÉTODOS DE LA PRÁCTICA 09: MANY-TO-MANY ---

    @Query("""
            SELECT DISTINCT p
            FROM ProductEntity p
            LEFT JOIN FETCH p.categories c
            JOIN FETCH p.owner u
            WHERE p.deleted = false
              AND u.id = :userId
              AND u.deleted = false
              AND (:name IS NULL OR LOWER(CAST(p.name AS string)) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))
              AND (:minPrice IS NULL OR p.price >= :minPrice)
              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
           """)
    List<ProductEntity> findByOwnerIdWithFilters(
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );

    @Query("""
            SELECT DISTINCT p
            FROM ProductEntity p
            JOIN p.categories catFilter
            LEFT JOIN FETCH p.categories c
            JOIN FETCH p.owner u
            WHERE p.deleted = false
              AND catFilter.id = :categoryId
              AND catFilter.deleted = false
              AND u.deleted = false
              AND (:name IS NULL OR LOWER(CAST(p.name AS string)) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))
              AND (:minPrice IS NULL OR p.price >= :minPrice)
              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
              AND (:userId IS NULL OR u.id = :userId)
           """)
    List<ProductEntity> findByCategoryIdWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("userId") Long userId
    );

    // --- MÉTODOS DE LA PRÁCTICA 10: PAGINACIÓN GENERAL ---

    @Query(
            value = """
                    SELECT p
                    FROM ProductEntity p
                    WHERE p.deleted = false
                    """,
            countQuery = """
                    SELECT COUNT(p)
                    FROM ProductEntity p
                    WHERE p.deleted = false
                    """
    )
    Page<ProductEntity> findActivePage(Pageable pageable);

    @Query("""
            SELECT p
            FROM ProductEntity p
            WHERE p.deleted = false
            """)
    Slice<ProductEntity> findActiveSlice(Pageable pageable);

    // --- MÉTODOS DE LA PRÁCTICA 10: PAGINACIÓN POR CATEGORÍA ---

    /*
     * Nota: no se usa DISTINCT con Page/Slice porque Spring Data JPA no puede
     * calcular COUNT DISTINCT correctamente cuando hay fetch join de colecciones.
     * Por eso aquí NO se hace LEFT JOIN FETCH p.categories (evita duplicados
     * y problemas con el conteo). El listado de categorías de cada producto
     * se sigue mostrando bien porque ProductMapper/Product usa lazy loading
     * dentro de la misma transacción (@Transactional en el service).
     */
    @Query(
            value = """
                    SELECT DISTINCT p
                    FROM ProductEntity p
                    JOIN p.categories catFilter
                    JOIN p.owner u
                    WHERE p.deleted = false
                      AND catFilter.id = :categoryId
                      AND catFilter.deleted = false
                      AND u.deleted = false
                      AND (:name IS NULL OR LOWER(CAST(p.name AS string)) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))
                      AND (:minPrice IS NULL OR p.price >= :minPrice)
                      AND (:maxPrice IS NULL OR p.price <= :maxPrice)
                      AND (:userId IS NULL OR u.id = :userId)
                    """,
            countQuery = """
                    SELECT COUNT(DISTINCT p)
                    FROM ProductEntity p
                    JOIN p.categories catFilter
                    JOIN p.owner u
                    WHERE p.deleted = false
                      AND catFilter.id = :categoryId
                      AND catFilter.deleted = false
                      AND u.deleted = false
                      AND (:name IS NULL OR LOWER(CAST(p.name AS string)) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))
                      AND (:minPrice IS NULL OR p.price >= :minPrice)
                      AND (:maxPrice IS NULL OR p.price <= :maxPrice)
                      AND (:userId IS NULL OR u.id = :userId)
                    """
    )
    Page<ProductEntity> findByCategoryIdWithFiltersPage(
            @Param("categoryId") Long categoryId,
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("""
            SELECT DISTINCT p
            FROM ProductEntity p
            JOIN p.categories catFilter
            JOIN p.owner u
            WHERE p.deleted = false
              AND catFilter.id = :categoryId
              AND catFilter.deleted = false
              AND u.deleted = false
              AND (:name IS NULL OR LOWER(CAST(p.name AS string)) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))
              AND (:minPrice IS NULL OR p.price >= :minPrice)
              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
              AND (:userId IS NULL OR u.id = :userId)
            """)
    Slice<ProductEntity> findByCategoryIdWithFiltersSlice(
            @Param("categoryId") Long categoryId,
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("userId") Long userId,
            Pageable pageable
    );
}