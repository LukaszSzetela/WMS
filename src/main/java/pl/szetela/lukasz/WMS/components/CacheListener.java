package pl.szetela.lukasz.WMS.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import pl.szetela.lukasz.WMS.models.Product;
import pl.szetela.lukasz.WMS.services.ProductService;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CacheListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(CacheListener.class);
    private ProductService productService;
    private CacheManager cacheManager;

    @Autowired
    public CacheListener(ProductService productService, CacheManager cacheManager) {
        this.productService = productService;
        this.cacheManager = cacheManager;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        createProductCache();
    }

    private void createProductCache() {
        MutableConfiguration<Long, Product> productsCache = new MutableConfiguration<>();
        List<Product> products = productService.getAll();
        Map<Long, Product> productsMap = products.stream().collect(Collectors.toMap(Product::getProductId, y -> y));
        try(Cache<Long, Product> cache = cacheManager.createCache("productCache", productsCache)) {
            cache.putAll(productsMap);
            logger.info("Created cache for product");
        }
    }
}
