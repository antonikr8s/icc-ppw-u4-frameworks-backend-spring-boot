package ec.edu.ups.icc.fundamentos01.products.repositories;

import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
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

    // --- MÉTODOS DE LA PRÁCTICA 09: EVOLUCIÓN A MANY-TO-MANY ---

    /*
     * Busca productos de un usuario.
     * Se elimina el filtro categoryId por el cambio a colección Set<CategoryEntity>.
     */
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

    /*
     * Busca productos filtrando desde el contexto de una categoría.
     * Usa DISTINCT para evitar duplicados en tablas ManyToMany.
     */
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
}