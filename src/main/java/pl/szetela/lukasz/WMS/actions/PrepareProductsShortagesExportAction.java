package pl.szetela.lukasz.WMS.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.szetela.lukasz.WMS.dto.ProductDto;
import pl.szetela.lukasz.WMS.models.Product;
import pl.szetela.lukasz.WMS.services.ProductLogService;
import pl.szetela.lukasz.WMS.services.ProductService;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class PrepareProductsShortagesExportAction {

    private ProductLogService productLogService;
    private ProductService productService;

    @Autowired
    public PrepareProductsShortagesExportAction(ProductLogService productLogService, ProductService productService) {
        this.productLogService = productLogService;
        this.productService = productService;
    }

    public List<ProductDto> execute() {
        List<ProductDto> productsDetails = productLogService.getDistinctAll();
        List<Product> products = productService.getAll();
        Map<Long, Product> productsMaps = products.stream().collect(Collectors.toMap(Product::getProductId, y -> y));
        productsDetails = productsDetails.stream().peek(y -> {
            Product product = productsMaps.get(y.getProductId());
            y.setName(product.getName());
            y.setCategory(product.getCategory());
            y.setSubcategory(product.getSubcategory());
            y.setPrice(product.getPrice());
            y.setNumber(product.getNumber());
            y.setReservedNumber(product.getReservedNumber());
        }).filter(isShortages()).collect(Collectors.toList());
        return productsDetails;
    }

    private Predicate<ProductDto> isShortages() {
        return y -> y.getNumber() < y.getZI();
    }
}
