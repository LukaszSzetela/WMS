package pl.szetela.lukasz.WMS.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.szetela.lukasz.WMS.dto.OrderDto;
import pl.szetela.lukasz.WMS.services.OrderService;
import pl.szetela.lukasz.WMS.services.UserService;

import java.util.List;

@Component
public class PrepareOrdersPerCurrentQuarterExportAction extends AbstractPrepareOrdersAction {

    private OrderService orderService;

    private String date;

    @Autowired
    public PrepareOrdersPerCurrentQuarterExportAction(UserService userService, OrderService orderService) {
        super(userService);
        this.orderService = orderService;
    }

    @Override
    protected List<OrderDto> getOrders() {
        return orderService.getOrdersByQuarter(date);
    }

    public void setDate(String date) {
        this.date = date;
    }
}
