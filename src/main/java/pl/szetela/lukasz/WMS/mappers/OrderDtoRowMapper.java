package pl.szetela.lukasz.WMS.mappers;

import org.springframework.jdbc.core.RowMapper;
import pl.szetela.lukasz.WMS.dto.OrderDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDtoRowMapper  implements RowMapper<OrderDto> {

    @Override
    public OrderDto mapRow(ResultSet resultSet, int i) throws SQLException {
        OrderDto order = new OrderDto();
        order.setId(resultSet.getLong("order_id"));
        order.setOrderDate(resultSet.getDate("order_date"));
        order.setShippingCost(resultSet.getDouble("shipping_cost"));
        order.setStatus(resultSet.getString("status"));
        order.setSubTotal(resultSet.getDouble("sub_total"));
        order.setTax(resultSet.getDouble("tax"));
        order.setTaxRate(resultSet.getDouble("tax_rate"));
        order.setTotalCost(resultSet.getDouble("total_cost"));
        order.setExecutorId(resultSet.getLong("executor"));
        order.setOrdererId(resultSet.getLong("orderer"));
        return order;
    }
}