package pl.szetela.lukasz.WMS.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szetela.lukasz.WMS.dto.ProductDto;
import pl.szetela.lukasz.WMS.services.ProductLogService;
import pl.szetela.lukasz.WMS.utils.StringUtils;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ProductLogController {

    private ProductLogService productLogService;

    @Autowired
    public ProductLogController(ProductLogService productLogService) {
        this.productLogService = productLogService;
    }

    @GetMapping(path = "/productsLog/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto productDto = productLogService.getById(id);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping(path = "/productsLog", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductDto>> getAllDistinctProduct() {
        List<ProductDto> allProduct = productLogService.getDistinctAll();
        return ResponseEntity.ok(allProduct);
    }

    @GetMapping(path = "/productsDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductDto>> getAllBetweenDates() {
        String[] dates = StringUtils.getDate(LocalDate.now().minusYears(1));
        productLogService.getAllBetweenDates(dates[0], dates[1]);
        return ResponseEntity.ok(null);
    }

    @GetMapping(path = "/productsDate/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDto> getOneBetweenDates(@PathVariable Long id) {
        String[] dates = StringUtils.getDate(LocalDate.now().minusYears(1));
        ProductDto productDto = productLogService.getOneBetweenDates(dates[0], dates[1], id);
        return ResponseEntity.ok(productDto);
    }


}
