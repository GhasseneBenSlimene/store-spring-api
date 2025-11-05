package com.ghassene.store.controllers;

import com.ghassene.store.dtos.ProductDto;
import com.ghassene.store.entities.Category;
import com.ghassene.store.entities.Product;
import com.ghassene.store.mappers.ProductMapper;
import com.ghassene.store.repositories.CategoryRepository;
import com.ghassene.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private ProductMapper productMapper;

    @GetMapping
    public Iterable<ProductDto> getAllProducts(
            @RequestParam(required = false, name="categoryId") Long categoryId
    ){
        List<Product> products;
        if (categoryId != null){
            products = productRepository.findByCategoryId(categoryId);
        } else{
            products = productRepository.findAllWithCategory();
        }
        return products
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id){
        Product product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto request,
                                                    UriComponentsBuilder uriBuilder){
        Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if(category==null){
            return ResponseEntity.badRequest().build();
        }

        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        productRepository.save(product);

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        request.setId(product.getId());
        return ResponseEntity.created(uri).body(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable(name="id") Long id, @RequestBody ProductDto request){
        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if(category==null){
            return ResponseEntity.badRequest().build();
        }

        var product = productRepository.findById(id).orElse(null);
        if(product==null){
            return ResponseEntity.notFound().build();
        }

        productMapper.update(request,product);
        product.setCategory(category);
        productRepository.save(product);

        request.setId(product.getId());
        return ResponseEntity.ok(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name="id") Long id){
        var product = productRepository.findById(id).orElse(null);
        if(product==null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
