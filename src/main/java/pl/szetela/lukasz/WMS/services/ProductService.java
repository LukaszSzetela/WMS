package pl.szetela.lukasz.WMS.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.szetela.lukasz.WMS.dao.ProductDao;
import pl.szetela.lukasz.WMS.models.Product;

import java.util.List;

@Service
public class ProductService {

    private ProductDao productDao;

    @Autowired
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product getById(Long id) {
        return productDao.findById(id);
    }

    public List<Product> getAll() {
        return productDao.findAll();
    }

    public Product save(Product product) {
        return productDao.save(product);
    }

    public Product getByName(String name) {
        return productDao.findByName(name);
    }
}
