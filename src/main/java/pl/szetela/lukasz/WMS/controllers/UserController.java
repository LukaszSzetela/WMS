package pl.szetela.lukasz.WMS.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.szetela.lukasz.WMS.dao.CustomerDao;
import pl.szetela.lukasz.WMS.dto.CustomerDto;
import pl.szetela.lukasz.WMS.dto.SessionUser;
import pl.szetela.lukasz.WMS.dto.UserDto;
import pl.szetela.lukasz.WMS.models.Customer;
import pl.szetela.lukasz.WMS.models.User;
import pl.szetela.lukasz.WMS.dao.UserDao;
import pl.szetela.lukasz.WMS.services.CustomerService;
import pl.szetela.lukasz.WMS.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class UserController {

    private UserDao userDao;
    private UserService userService;
    private CustomerDao customerDao;
    private CustomerService customerService;


    @Autowired
    public UserController(UserDao userDao, UserService userService, CustomerDao customerDao, CustomerService customerService) {
        this.userDao = userDao;
        this.userService = userService;
        this.customerDao = customerDao;
        this.customerService = customerService;
    }

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> allUsers() {
        List<User> allUsers = userDao.findAll();
        List<UserDto> userDtos = allUsers.stream().map(y -> userService.transformToDto(y)).collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping(path = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerDto>> allCustomers() {
        List<Customer> customers = customerDao.findAll();
        List<CustomerDto> customerDtos = customers.stream().map(y -> customerService.transformToDto(y)).collect(Collectors.toList());
        return ResponseEntity.ok(customerDtos);
    }

    @GetMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userDao.findOne(id);
        return ResponseEntity.ok(userService.transformToDto(user));
    }

    @PostMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody UserDto user) {
        User userToSave = userService.toUser(user);
        User save = userService.saveUserAndGet(userToSave);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/users/{id}")
                .buildAndExpand(save.getId())
                .toUri();
        return ResponseEntity.created(location).body(userService.transformToDto(save));
    }

    @PostMapping(path = "/customers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCustomer(@RequestBody CustomerDto customer) {
        Customer customerToSave = customerService.toCustomer(customer);
        Customer save = customerService.saveCustomerAndGet(customerToSave);
        if (customer.getUserId() != null) {
            User user = userService.getUserById(customer.getUserId());
            user.setCustomer(save);
            userDao.save(user);
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/customers/{id}")
                .buildAndExpand(save.getId())
                .toUri();
        return ResponseEntity.created(location).body(save);
    }

    @GetMapping(path = "/userId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SessionUser>> allUsers(HttpServletRequest request) {
        List<SessionUser> objects = new ArrayList<>();
        Integer userId = Integer.parseInt(String.valueOf(request.getSession().getAttribute("u_id")));
        String role = String.valueOf(request.getSession().getAttribute("u_role"));
        SessionUser sessionUser = new SessionUser(userId, role);
        Customer customer = userService.getCustomerByUserId(userId.longValue());
        userService.setUserProperties(customer, sessionUser);
        objects.add(sessionUser);
        return ResponseEntity.ok(objects);
    }

}
