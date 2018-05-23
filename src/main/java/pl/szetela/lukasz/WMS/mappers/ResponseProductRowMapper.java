package pl.szetela.lukasz.WMS.mappers;

import org.springframework.jdbc.core.RowMapper;
import pl.szetela.lukasz.WMS.dto.ResponseProduct;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResponseProductRowMapper implements RowMapper<ResponseProduct> {

    @Override
    public ResponseProduct mapRow(ResultSet resultSet, int i) throws SQLException {
        ResponseProduct response = new ResponseProduct();
        response.setProductId(resultSet.getLong("product_id"));
        response.setNumber(resultSet.getInt("number"));
        response.setOrdinal(resultSet.getInt("ordinal"));
        response.setIsComplete(resultSet.getBoolean("status"));
        return response;
    }
}
