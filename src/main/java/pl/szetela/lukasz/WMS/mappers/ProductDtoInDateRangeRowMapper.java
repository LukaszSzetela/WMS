package pl.szetela.lukasz.WMS.mappers;

import pl.szetela.lukasz.WMS.dto.ProductDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDtoInDateRangeRowMapper extends AbstractProductDtoRowMapper {

    @Override
    public ProductDto mapRow(ResultSet resultSet, int i) throws SQLException {
        ProductDto productDto = super.mapRow(resultSet, i);
        productDto.setDemand(resultSet.getInt("demand"));
        return productDto;
    }
}
