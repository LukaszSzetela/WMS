package pl.szetela.lukasz.WMS.actions;

import org.springframework.stereotype.Component;
import pl.szetela.lukasz.WMS.dto.CustomerDto;
import pl.szetela.lukasz.WMS.dto.OrderDto;
import pl.szetela.lukasz.WMS.dto.UserDto;
import pl.szetela.lukasz.WMS.models.Customer;
import pl.szetela.lukasz.WMS.models.User;
import pl.szetela.lukasz.WMS.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Component
public abstract class AbstractPrepareOrdersAction {

    private UserService userService;

    protected abstract List<OrderDto> getOrders();

    public AbstractPrepareOrdersAction(UserService userService) {
        this.userService = userService;
    }

    public List<OrderDto> execute() {
        List<OrderDto> orders = getOrders();
        return orders.stream().peek(y -> {
            User executor = userService.getUserById(y.getOrdererId());
            UserDto executorDto = new UserDto();
            executorDto.setFirstName(executor.getFirstName());
            executorDto.setLastName(executor.getLastName());
            executorDto.setEmail(executor.getEmail());
            y.setExecutor(executorDto);
            Customer orderer = userService.getCustomerByUserId(y.getOrdererId());
            CustomerDto customer = new CustomerDto();
            customer.setName(orderer.getName());
            customer.setEmail(orderer.getEmail());
            y.setOrderer(customer);
        }).collect(Collectors.toList());
    }

}
