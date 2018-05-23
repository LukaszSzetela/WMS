package pl.szetela.lukasz.WMS.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.szetela.lukasz.WMS.dto.OrderDto;
import pl.szetela.lukasz.WMS.dto.ResponseOrders;
import pl.szetela.lukasz.WMS.models.Order;
import pl.szetela.lukasz.WMS.services.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(path = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.getOderDtos();
        return ResponseEntity.ok(orders);
    }

    @GetMapping(path = "/api/orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> getOderById(@PathVariable Long id) {
        OrderDto order = orderService.getOrderDto(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping(path = "/api/orders", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveOrder(@RequestBody Object order, HttpServletRequest request) {
        Long userId = (Long)request.getSession().getAttribute("u_id");
        Long orderId = orderService.convertToOrder(order, userId);
        OrderDto save = orderService.getOrderDto(orderId);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/api/orders/{id}")
                .buildAndExpand(save.getId())
                .toUri();
        return ResponseEntity.created(location).body(save);
    }

    @DeleteMapping(value = "/api/orders/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/changestatus", params = {"id", "status", "action"})
    public ResponseEntity<ResponseOrders> changeStatus(@RequestParam("id") Long id, @RequestParam("status") String status, @RequestParam("action") String action, HttpServletRequest request) {
        Long userId = (Long)request.getSession().getAttribute("u_id");
        ResponseOrders response = orderService.changeStatus(id, status, action, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/currentorder")
    public ResponseEntity<OrderDto> getCurrentOrderForUser(HttpServletRequest request) {
        Long userId = (Long)request.getSession().getAttribute("u_id");
        Order order = orderService.getCurrentOrderForUser(userId);
        return ResponseEntity.ok(order == null ?  new OrderDto() : orderService.createOrderDto(order));
    }

    @GetMapping(path = "/updateOrder", params = {"id", "isComplete"})
    public void updateOrder(@RequestParam("id") Long id, @RequestParam(value = "productId", required = false) Long productId, @RequestParam(value = "status", required = false) Boolean status, @RequestParam("isComplete") Boolean isComplete) {
        OrderDto order = orderService.getOrderDto(id);
        if (status == null && order != null) {
            order.getProducts().forEach(y -> orderService.updateOrderProduct(order.getId(), y.getProductId(), isComplete));
        } else if (status != null) {
            orderService.updateOrderProduct(order.getId(), productId, status);
        }
    }
}
