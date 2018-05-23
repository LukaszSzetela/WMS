package pl.szetela.lukasz.WMS.mappers;

import org.springframework.jdbc.core.RowMapper;
import pl.szetela.lukasz.WMS.models.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet resultSet, int i) throws SQLException {
        Product product = new Product();
        product.setProductId(resultSet.getLong("product_id"));
        product.setCategory(resultSet.getString("category"));
        product.setSubcategory(resultSet.getString("subcategory"));
        product.setName(resultSet.getString("name"));
        product.setNumber(resultSet.getInt("number"));
        product.setPrice(resultSet.getDouble("price"));
        product.setAnnualCostStock( resultSet.getDouble("annual_cost_stock"));
        product.setDeliveryTime(resultSet.getInt("delivery_time"));
        product.setShortageCost(resultSet.getDouble("shortage_cost"));
        product.setReservedNumber(resultSet.getInt("reserved_number"));
        return product;
    }
}