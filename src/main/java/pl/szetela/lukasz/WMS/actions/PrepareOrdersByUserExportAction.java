package pl.szetela.lukasz.WMS.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.szetela.lukasz.WMS.dto.OrderDto;
import pl.szetela.lukasz.WMS.services.OrderService;
import pl.szetela.lukasz.WMS.services.UserService;

import java.util.List;

@Component
public class PrepareOrdersByUserExportAction extends AbstractPrepareOrdersAction {

    private OrderService orderService;

    private Long executorId;

    @Autowired
    public PrepareOrdersByUserExportAction(UserService userService, OrderService orderService) {
        super(userService);
        this.orderService = orderService;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
    }

    @Override
    protected List<OrderDto> getOrders() {
        return orderService.getOrdersByExecutor(executorId);
    }
}
