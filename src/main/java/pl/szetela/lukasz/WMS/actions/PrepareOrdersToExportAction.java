package pl.szetela.lukasz.WMS.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.szetela.lukasz.WMS.dto.OrderDto;
import pl.szetela.lukasz.WMS.services.OrderService;
import pl.szetela.lukasz.WMS.services.UserService;

import java.util.List;

@Component
public class PrepareOrdersToExportAction extends AbstractPrepareOrdersAction {

    private OrderService orderService;

    private String statuses;

    @Autowired
    public PrepareOrdersToExportAction(UserService userService, OrderService orderService) {
        super(userService);
        this.orderService = orderService;
    }

    public void setStatuses(String statuses) {
        this.statuses = statuses;
    }

    @Override
    protected List<OrderDto> getOrders() {
        return orderService.getOrdersByStatuses(statuses);
    }
}
