package pl.szetela.lukasz.WMS.mappers;

import pl.szetela.lukasz.WMS.dto.OrderDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExtendedOrderDtoRowMapper extends OrderDtoRowMapper {

    @Override
    public OrderDto mapRow(ResultSet resultSet, int i) throws SQLException {
        OrderDto order = super.mapRow(resultSet, i);
        order.setPickingTime(resultSet.getString("picking_time"));
        return order;
    }
}
