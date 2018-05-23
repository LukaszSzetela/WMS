package pl.szetela.lukasz.WMS.services;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.szetela.lukasz.WMS.dao.OrderDao;
import pl.szetela.lukasz.WMS.dao.UserDao;
import pl.szetela.lukasz.WMS.dto.OrderDto;
import pl.szetela.lukasz.WMS.dto.ResponseOrders;
import pl.szetela.lukasz.WMS.dto.ResponseProduct;
import pl.szetela.lukasz.WMS.enums.Status;
import pl.szetela.lukasz.WMS.models.Order;
import pl.szetela.lukasz.WMS.models.Product;
import pl.szetela.lukasz.WMS.models.User;
import pl.szetela.lukasz.WMS.utils.StringUtils;

import javax.cache.Cache;
import javax.cache.CacheManager;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@Service
public class OrderService {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(OrderService.class);
    private OrderDao orderDao;
    private ProductLogService productLogService;
    private UserDao userDao;
    private ProductService productService;
    private OrderTimerService orderTimerService;
    private CacheManager cacheManager;

    @Autowired
    public OrderService(OrderDao orderDao, ProductLogService productLogService, UserDao userDao, ProductService productService, OrderTimerService orderTimerService, CacheManager cacheManager) {
        this.orderDao = orderDao;
        this.productLogService = productLogService;
        this.userDao = userDao;
        this.productService = productService;
        this.orderTimerService = orderTimerService;
        this.cacheManager = cacheManager;
    }

    public Order getById(Long id) {
        return orderDao.findOne(id);
    }

    public List<OrderDto> getOderDtos() {
        List<OrderDto> result = new ArrayList<>();
        List<Order> orders = getAll();

        orders.forEach(y -> result.add(createOrderDto(y)));
        return result;
    }

    public OrderDto getOrderDto(Long orderId) {
        Order order = getById(orderId);
        return createOrderDto(order);
    }

    public List<ResponseProduct> getOrderProductForOrder(Long orderId) {
        return orderDao.findOrderProductForOrder(orderId);
    }

    public Long getOrderIdByOrderer(Long orderer) {
        return orderDao.findOrderIdByOrdererId(orderer);
    }

    public List<Order> getAll() {
        return orderDao.findAll();
    }

    public List<OrderDto> getOrdersByStatuses(String statuses) {
        List<String> statusesToQuery = StringUtils.prepareStringArrayToQuery(statuses);
        return orderDao.findOrdersByStatuses(statusesToQuery);
    }

    public List<OrderDto> getOrdersByDate(String date) {
        return orderDao.findOrdersByDate(date);
    }

    public List<OrderDto> getOrdersByMonth(String date) {
        return orderDao.findOrdersByMonth(date);
    }

    public List<OrderDto> getOrdersByDateInRange(String dateFrom, String dateTo) {
        return orderDao.findOrdersByDatesInRange(dateFrom, dateTo);
    }

    public List<OrderDto> getOrdersByQuarter(String date) {
        return orderDao.findOrdersByQuarter(date);
    }

    public List<OrderDto> getOrdersByYear(String date) {
        return orderDao.findOrdersByYear(date);
    }

    public List<OrderDto> getOrdersByExecutor(Long executorId) {
        return orderDao.findOrdersByExecutorId(executorId);
    }

    public void saveOrder(Order order) {
        orderDao.saveOrder(order);
    }

    public void updateOrder(Order order) {
        orderDao.updateOrder(order);
    }

    public void saveOrderProduct(List<LinkedHashMap<String, Object>> items, Long orderId) {
        AtomicInteger ordinalNumber = new AtomicInteger(1);
        items.forEach(y -> {
            Long productId = Long.parseLong(y.get("id").toString());
            Integer quantity = Integer.parseInt(y.get("quantity").toString());
            orderDao.saveOrderProduct(orderId, productId, quantity, ordinalNumber.getAndIncrement(), false);
        });
    }

    public void deleteById(Long id) {
        orderDao.delete(id);
    }

    public void updateOrderProduct(Long orderId, Long productId, Boolean status) {
        orderDao.updateOrderProduct(orderId, productId, status);
    }

    public Long convertToOrder(Object orderFromRequest, Long userId) {
        Order result = new Order();
        LinkedHashMap<String, Object> order = (LinkedHashMap<String, Object>) orderFromRequest;
        Double shippingCost = Double.valueOf(String.valueOf(order.get("shipping")));
        Double tax = Double.valueOf(String.valueOf(order.get("tax")));
        Double taxRate = Double.valueOf(String.valueOf(order.get("taxRate")));
        Double subTotal = Double.valueOf(String.valueOf(order.get("subTotal")));
        Double totalCost = Double.valueOf(String.valueOf(order.get("totalCost")));
        List<LinkedHashMap<String, Object>> items = (List<LinkedHashMap<String, Object>>) order.get("items");

        User orderer = userDao.findOne(userId);

        result.setOrderer(orderer);
        result.setShippingCost(shippingCost);
        result.setStatus(Status.ORDERED.getDescription());
        result.setSubTotal(subTotal);
        result.setTax(tax);
        result.setTaxRate(taxRate);
        result.setTotalCost(totalCost);

        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        result.setOrderDate(timestamp);

        saveOrder(result);

        Long orderId = getOrderIdByOrderer(orderer.getId());

        saveOrderProduct(items, orderId);

        productLogService.createProductLogs(items);

        return orderId;
    }

    public ResponseOrders changeStatus(Long orderId, String status, String action, Long userId) {
        Order order = getById(orderId);
        checkIfNeedChangeStatusCurrentOrder(status, order, userId);
        switch (action) {
            case "cancelOrder":
                order.setStatus(Status.CANCELLED.getDescription());
                break;
            case "acceptOrder":
                order.setStatus(Status.ACCEPTED.getDescription());
                enterReservedNumberForProducts(order);
                break;
            case "assignOrder":
                order.setStatus(Status.ASSIGNED.getDescription());
                assignOrderToUser(userId, order);
                break;
            case "realizeOrder":
                order.setStatus(Status.REALIZE.getDescription());
                orderTimerService.run(order);
                break;
            case "pauseOrder":
                order.setStatus(Status.PAUSE.getDescription());
                orderTimerService.stop(order);
                break;
            case "finishOrder":
                order.setStatus(Status.COMPLETED.getDescription());
                orderTimerService.stop(order);
                decreaseReservedNumberForProducts(order);
                break;
            default:
                break;
        }
        updateOrder(order);
        Order currentOrder = getCurrentOrderForUser(userId);
        if (currentOrder == null) {
            return new ResponseOrders(createOrderDto(order), null);
        }
        return new ResponseOrders(createOrderDto(order), createOrderDto(currentOrder));
    }

    public void assignOrderToUser(Long userId, Order order) {
        User user = userDao.findOne(userId);
        order.setExecutor(user);
    }

    public Order getCurrentOrderForUser(Long userId) {
        User user = userDao.findOne(userId);
        List<Order> executorOrders = user.getExecutorOrders();
        Order order = null;
        try {
            order = executorOrders.stream().filter(isCurrentOrder()).findFirst().get();
        } catch (NoSuchElementException e) {
            logger.warn("There is no current order for the user: " + user.getId());
        }
        return order;
    }

    public OrderDto createOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        if (order != null) {
            orderDto.setId(order.getId());
            orderDto.setOrdererId(order.getOrderer().getId());
            orderDto.setShippingCost(order.getShippingCost());
            orderDto.setTax(order.getTax());
            orderDto.setOrderDate(order.getOrderDate());
            orderDto.setTaxRate(order.getTaxRate());
            orderDto.setSubTotal(order.getSubTotal());
            orderDto.setTotalCost(order.getTotalCost());
            orderDto.setStatus(order.getStatus());
            orderDto.setExecutorId(order.getExecutor() != null ? order.getExecutor().getId() : null);
            orderDto.setPickingTime(order.getPickingTime());

            List<ResponseProduct> productDtos = getOrderProductForOrder(order.getId());
            productDtos.forEach(y -> {
                Product product = productService.getById(y.getProductId());
                y.setName(product.getName());
                y.setPrice(product.getPrice());
                y.setInStock(product.getNumber());
            });

            orderDto.setProducts(productDtos);
            orderDto.setVerification(verifyOrder(productDtos));
        }
        return orderDto;
    }

    private Boolean verifyOrder(List<ResponseProduct> productsDtos) {
        long count = productsDtos.stream().filter(isNumberBiggerThanInStock()).count();
        return count == 0;
    }

    private void checkIfNeedChangeStatusCurrentOrder(String status, Order order, Long userId) {
        if (status.equals(Status.ASSIGNED.getDescription()) || status.equals(Status.PAUSE.getDescription())) {
            Order currentOrder = getCurrentOrderForUser(userId);
            if (currentOrder != null && !currentOrder.getId().equals(order.getId())) {
                currentOrder.setStatus(Status.PAUSE.getDescription());
                orderTimerService.stop(order);
                updateOrder(currentOrder);
            }
        }
    }

    private Predicate<ResponseProduct> isNumberBiggerThanInStock() {
        return y -> y.getNumber() > y.getInStock();
    }

    private Predicate<Order> isCurrentOrder() {
        return y -> y.getStatus().equals("Realize");
    }

    private void enterReservedNumberForProducts(Order order) {
        OrderDto orderDto = createOrderDto(order);
        orderDto.getProducts().forEach(y -> {
            Product product = productService.getById(y.getProductId());
            product.setNumber(product.getNumber() - y.getNumber());
            product.setReservedNumber(product.getReservedNumber() + y.getNumber());
            Cache<Long, Product> cache = cacheManager.getCache("productCache");
            cache.replace(product.getProductId(), product);
            productService.save(product);
        });
    }

    private void decreaseReservedNumberForProducts(Order order) {
        OrderDto orderDto = createOrderDto(order);
        orderDto.getProducts().forEach(y -> {
            Product product = productService.getById(y.getProductId());
            product.setReservedNumber(product.getReservedNumber() - y.getNumber());
            Cache<Long, Product> cache = cacheManager.getCache("productCache");
            cache.replace(product.getProductId(), product);
            productService.save(product);
        });
    }
}
