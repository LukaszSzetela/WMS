package pl.szetela.lukasz.WMS.dao;

import pl.szetela.lukasz.WMS.models.Product;

import java.util.List;

public interface IProductDao {

    Product findById(Long id);
    List<Product> findAll();
}
