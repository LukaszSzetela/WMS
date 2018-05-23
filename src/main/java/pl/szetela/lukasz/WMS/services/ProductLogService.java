package pl.szetela.lukasz.WMS.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.szetela.lukasz.WMS.dao.ProductLogDao;
import pl.szetela.lukasz.WMS.dto.ProductDto;
import pl.szetela.lukasz.WMS.dto.ProductLogDto;
import pl.szetela.lukasz.WMS.models.Product;
import pl.szetela.lukasz.WMS.models.ProductLog;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ProductLogService {

    private ProductLogDao productLogDao;
    private ProductService productService;

    @Autowired
    public ProductLogService(ProductLogDao productLogDao, ProductService productService) {
        this.productLogDao = productLogDao;
        this.productService = productService;
    }

    public ProductDto getById(Long id) {
        return productLogDao.findById(id);
    }

    public void save(ProductLog productLog) {
        productLogDao.save(productLog);
    }

    public List<ProductDto> getDistinctAll() {
        return productLogDao.findDistinctAll();
    }

    public ProductDto getOneBetweenDates(String dateFrom, String dateTo, Long id) {
        return productLogDao.findOneBetweenDates(dateFrom, dateTo, id);
    }

    public List<ProductDto> getAllBetweenDates(String dateFrom, String dateTo) {
        return productLogDao.findDistinctBetweenDates(dateFrom, dateTo);
    }

    public List<ProductLogDto> getAll(String dateFrom, String dateTo) {
        return productLogDao.findAll(dateFrom, dateTo);
    }

    public List<ProductLogDto> getAllByProductId(Long productId) {
        return productLogDao.findAllByProductId(productId);
    }

    public void createProductLogs(List<LinkedHashMap<String, Object>> items) {
        items.forEach(y -> {
            Long productId = Long.parseLong(y.get("id").toString());
            Integer quantity = Integer.parseInt(y.get("quantity").toString());

            ProductLog productLog = createProductLog(productId, quantity);

            save(productLog);
        });
    }

    public ProductLog createProductLog(Long productId, Integer quantity) {
        Product product = productService.getById(productId);
        ProductLog productLog = new ProductLog();
        productLog.setAnnualCostStock(product.getAnnualCostStock());
        productLog.setCategory(product.getCategory());
        LocalDate now = LocalDate.now();
        Date date = java.sql.Date.valueOf(now);
        productLog.setDate(date);
        productLog.setDeliveryTime(product.getDeliveryTime());
        productLog.setName(String.format("%s_%s_%s", product.getCategory(), product.getSubcategory(), product.getName()));
        productLog.setNumber(quantity);
        productLog.setPrice(product.getPrice());
        productLog.setSubcategory(product.getSubcategory());
        productLog.setShortageCost(product.getShortageCost());
        productLog.setProduct(product);
        return productLog;
    }
}
