package pl.szetela.lukasz.WMS.mappers;

import pl.szetela.lukasz.WMS.dto.ProductDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDtoRowMapper extends AbstractProductDtoRowMapper {

    @Override
    public ProductDto mapRow(ResultSet resultSet, int i) throws SQLException {
        ProductDto productDto = super.mapRow(resultSet, i);
        productDto.setNumber(resultSet.getInt("number"));
        productDto.setZB(resultSet.getInt("ZB"));
        productDto.setZI(resultSet.getInt("ZI"));
        return productDto;
    }
}
