package pl.szetela.lukasz.WMS.mappers;

import org.springframework.jdbc.core.RowMapper;
import pl.szetela.lukasz.WMS.dto.ProductDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AbstractProductDtoRowMapper implements RowMapper<ProductDto> {

    @Override
    public ProductDto mapRow(ResultSet resultSet, int i) throws SQLException {
        ProductDto productDto = new ProductDto();
        productDto.setProductId(resultSet.getLong("product_id"));
        return productDto;
    }
}