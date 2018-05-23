package pl.szetela.lukasz.WMS.dao;

import pl.szetela.lukasz.WMS.dto.OrderDto;
import pl.szetela.lukasz.WMS.dto.ResponseProduct;
import pl.szetela.lukasz.WMS.models.Order;

import java.util.List;

public interface IOrderDao {
    void saveOrder(Order order);

    void saveOrderProduct(Long orderId, Long product_id, Integer number, Integer ordinal, Boolean status);

    void updateOrder(Order order);

    void updateOrderProduct(Long orderId, Long productId, Boolean status);

    Long findOrderIdByOrdererId(Long ordererId);

    List<ResponseProduct> findOrderProductForOrder(Long orderId);

    List<OrderDto> findOrdersByStatuses(List<String> statuses);

    List<OrderDto> findOrdersByDate(String date);

    List<OrderDto> findOrdersByDatesInRange(String dateFrom, String dateTo);

    List<OrderDto> findOrdersByMonth(String date);

    List<OrderDto> findOrdersByQuarter(String date);

    List<OrderDto> findOrdersByYear(String date);

    List<OrderDto> findOrdersByExecutorId(Long executorId);
}
