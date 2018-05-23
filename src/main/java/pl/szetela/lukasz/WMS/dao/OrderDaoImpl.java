package pl.szetela.lukasz.WMS.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import pl.szetela.lukasz.WMS.dto.OrderDto;
import pl.szetela.lukasz.WMS.dto.ResponseProduct;
import pl.szetela.lukasz.WMS.mappers.ExtendedOrderDtoRowMapper;
import pl.szetela.lukasz.WMS.mappers.OrderDtoRowMapper;
import pl.szetela.lukasz.WMS.mappers.ResponseProductRowMapper;
import pl.szetela.lukasz.WMS.models.Order;
import pl.szetela.lukasz.WMS.utils.StringUtils;

import java.sql.PreparedStatement;
import java.util.List;

import static pl.szetela.lukasz.WMS.queries.OrderNativeQueries.*;

public class OrderDaoImpl implements IOrderDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveOrder(Order order) {
        jdbcTemplate.update(SAVE_ORDER, order.getOrderDate(), order.getShippingCost(), order.getStatus(), order.getSubTotal(), order.getTax(),
                order.getTaxRate(), order.getTotalCost(), order.getOrderer().getId());
    }

    @Override
    public void saveOrderProduct(Long orderId, Long product_id, Integer number, Integer ordinal, Boolean status) {
        jdbcTemplate.update(SAVE_ORDER_PRODUCT, orderId, product_id, number, ordinal, status);
    }

    @Override
    public Long findOrderIdByOrdererId(Long ordererId) {
        return jdbcTemplate.queryForObject(FIND_ORDER_BY_ORDERER_ID, new Object[]{ordererId}, Long.class);
    }

    @Override
    public List<OrderDto> findOrdersByStatuses(List<String> statuses) {
        final String sql = FIND_ORDERS_BY_STATUSES + StringUtils.prepareProperNumberQuestionTagsInBrackets(statuses.size());
        PreparedStatementCreator creator = connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < statuses.size(); i++) {
                statement.setString(i + 1, statuses.get(i));
            }
            return statement;
        };
        return jdbcTemplate.query(creator, new OrderDtoRowMapper());
    }

    @Override
    public List<OrderDto> findOrdersByDate(String date) {
        return jdbcTemplate.query(FIND_ORDERS_BY_DATE, new Object[]{date}, new ExtendedOrderDtoRowMapper());
    }

    @Override
    public List<OrderDto> findOrdersByDatesInRange(String dateFrom, String dateTo) {
        return jdbcTemplate.query(FIND_ORDERS_BY_DATES_IN_RANGE, new Object[]{dateFrom, dateTo}, new ExtendedOrderDtoRowMapper());
    }

    @Override
    public List<OrderDto> findOrdersByMonth(String date) {
        return jdbcTemplate.query(FIND_ORDERS_BY_MONTH, new Object[]{date}, new ExtendedOrderDtoRowMapper());
    }

    @Override
    public List<OrderDto> findOrdersByQuarter(String date) {
        return jdbcTemplate.query(FIND_ORDERS_BY_QUARTER, new Object[]{date}, new ExtendedOrderDtoRowMapper());
    }

    @Override
    public List<OrderDto> findOrdersByYear(String date) {
        return jdbcTemplate.query(FIND_ORDERS_BY_YEAR, new Object[]{date}, new ExtendedOrderDtoRowMapper());
    }
    @Override
    public List<OrderDto> findOrdersByExecutorId(Long executorId) {
        return jdbcTemplate.query(FIND_ORDERS_BY_EXECUTOR, new Object[]{executorId}, new ExtendedOrderDtoRowMapper());
    }

    @Override
    public List<ResponseProduct> findOrderProductForOrder(Long orderId) {
        return jdbcTemplate.query(FIND_ORDER_PRODUCT, new Object[]{orderId}, new ResponseProductRowMapper());
    }

    public void updateOrder(Order order) {
        Long executorId = null;
        if (order.getExecutor() != null) {
            executorId = order.getExecutor().getId();
        }
        jdbcTemplate.update(UPDATE_ORDER, order.getStatus(), executorId, order.getPickingTime(), order.getCurrentRealizeTimestamp(), order.getId());
    }

    public void updateOrderProduct(Long orderId, Long productId, Boolean status) {
        jdbcTemplate.update(UPDATE_ORDER_PRODUCT, status, orderId, productId);
    }
}
