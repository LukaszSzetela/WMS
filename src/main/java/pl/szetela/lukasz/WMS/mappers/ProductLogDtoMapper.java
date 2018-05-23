package pl.szetela.lukasz.WMS.mappers;

import org.springframework.jdbc.core.RowMapper;
import pl.szetela.lukasz.WMS.dto.ProductLogDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductLogDtoMapper implements RowMapper<ProductLogDto> {

    @Override
    public ProductLogDto mapRow(ResultSet resultSet, int i) throws SQLException {
        ProductLogDto productLog = new ProductLogDto();
        productLog.setId(resultSet.getLong("product_log_id"));
        productLog.setCategory(resultSet.getString("category"));
        productLog.setSubcategory(resultSet.getString("subcategory"));
        productLog.setName(resultSet.getString("name"));
        productLog.setNumber(resultSet.getInt("number"));
        productLog.setPrice(resultSet.getDouble("price"));
        productLog.setDate(resultSet.getDate("date"));
        productLog.setProductId(resultSet.getLong("product_id"));
        return productLog;
    }
}