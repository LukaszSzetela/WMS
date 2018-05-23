package pl.szetela.lukasz.WMS.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.szetela.lukasz.WMS.models.Product;
import pl.szetela.lukasz.WMS.services.ProductService;

import javax.cache.Cache;
import javax.cache.CacheManager;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping(value = "/api")
public class ProductController {

    private ProductService productService;
    private CacheManager cacheManager;

    @Autowired
    public ProductController(ProductService productService, CacheManager cacheManager) {
        this.productService = productService;
        this.cacheManager = cacheManager;
    }

    @GetMapping(path = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Cache<Long, Product> cache = cacheManager.getCache("productCache");
        Product product = cache.get(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping(path = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Cache<Long, Product> cache = cacheManager.getCache("productCache");
        cache.iterator().forEachRemaining(y -> products.add(y.getValue()));
        return ResponseEntity.ok(products);
    }

    @PostMapping(path = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveProduct(@RequestBody Product product) {
        Long id = product.getProductId();
        if (product.getReservedNumber() == null) {
            product.setReservedNumber(0);
        }
        Product save = productService.save(product);
        Cache<Long, Product> cache = cacheManager.getCache("productCache");
        if (id == null) {
            cache.putIfAbsent(save.getProductId(), save);
        } else {
            cache.replace(save.getProductId(), product);
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(save.getProductId())
                .toUri();
        return ResponseEntity.created(location).body(save);
    }

}
