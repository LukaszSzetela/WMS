package pl.szetela.lukasz.WMS.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.szetela.lukasz.WMS.mappers.ProductRowMapper;
import pl.szetela.lukasz.WMS.models.Product;

import java.util.List;

import static pl.szetela.lukasz.WMS.queries.ProductNativeQueries.FIND_ALL;
import static pl.szetela.lukasz.WMS.queries.ProductNativeQueries.FIND_ONE;

public class ProductDaoImpl implements IProductDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Product findById(Long id) {
        return jdbcTemplate.queryForObject(FIND_ONE, new Object[]{id},
                new BeanPropertyRowMapper<>(Product.class));
    }

    public List<Product> findAll() {
        return jdbcTemplate.query(FIND_ALL, new ProductRowMapper());
    }

}
