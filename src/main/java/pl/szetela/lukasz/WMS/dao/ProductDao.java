package pl.szetela.lukasz.WMS.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szetela.lukasz.WMS.models.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Long>, IProductDao {
    Product findByName(String name);
}
