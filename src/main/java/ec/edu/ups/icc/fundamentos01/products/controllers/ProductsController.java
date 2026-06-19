package ec.edu.ups.icc.fundamentos01.products.controllers;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.products.models.ProductModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private List<ProductModel> products = new ArrayList<>();
    private long currentId = 1;

    // GET ALL
    @GetMapping
    public List<ProductResponseDto> findAll() {
        return products.stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Object findOne(@PathVariable long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(p -> (Object) ProductMapper.toResponse(p))
                .orElse(Map.of("error", "Product not found"));
    }

    // POST
    @PostMapping
    public ProductResponseDto create(@RequestBody CreateProductDto dto) {
        ProductModel product = ProductMapper.toModel(dto);
        product.setId(currentId++);
        products.add(product);
        return ProductMapper.toResponse(product);
    }

    // PUT
    @PutMapping("/{id}")
    public Object update(@PathVariable long id, @RequestBody UpdateProductDto dto) {
        ProductModel product = products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (product == null) return Map.of("error", "Product not found");

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        return ProductMapper.toResponse(product);
    }

    // PATCH
    @PatchMapping("/{id}")
    public Object partialUpdate(@PathVariable long id, @RequestBody PartialUpdateProductDto dto) {
        ProductModel product = products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (product == null) return Map.of("error", "Product not found");

        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getStock() != null) product.setStock(dto.getStock());
        return ProductMapper.toResponse(product);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable long id) {
        boolean exists = products.removeIf(p -> p.getId().equals(id));
        if (!exists) return Map.of("error", "Product not found");
        return Map.of("message", "Deleted successfully");
    }
}