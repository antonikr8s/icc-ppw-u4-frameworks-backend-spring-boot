package ec.edu.ups.icc.fundamentos01.categories.controllers;

import ec.edu.ups.icc.fundamentos01.products.dtos.ProductFilterByCategoryDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Controlador REST encargado de exponer consultas relacionadas
 * entre categorías y productos[cite: 279, 280].
 *
 * La ruta pertenece al contexto semántico de categorías:
 * /categories/{id}/products [cite: 280]
 */
@RestController
@RequestMapping("/categories")
public class CategoryProductsController {

    private final ProductService productService;

    public CategoryProductsController(ProductService productService) {
        this.productService = productService;
    }

    /*
     * Endpoint para consultar productos de una categoría[cite: 283].
     *
     * GET /api/categories/{id}/products
     * GET /api/categories/{id}/products?name=laptop
     * GET /api/categories/{id}/products?minPrice=500&maxPrice=1500
     * GET /api/categories/{id}/products?userId=1 [cite: 284]
     */
    @GetMapping("/{id}/products")
    public List<ProductResponseDto> findProductsByCategory(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductFilterByCategoryDto filters
    ) {
        return productService.findByCategoryIdWithFilters(id, filters);
    }
}