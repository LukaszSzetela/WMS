package pl.szetela.lukasz.WMS.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.szetela.lukasz.WMS.dao.UserDao;
import pl.szetela.lukasz.WMS.dto.SessionUser;
import pl.szetela.lukasz.WMS.dto.UserDto;
import pl.szetela.lukasz.WMS.models.Customer;
import pl.szetela.lukasz.WMS.models.Role;
import pl.szetela.lukasz.WMS.models.User;
import pl.szetela.lukasz.WMS.dao.RoleDao;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private RoleDao roleDao;
    private UserDao userDao;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(RoleDao roleDao, UserDao userDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleDao = roleDao;
        this.userDao = userDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Customer getCustomerByUserId(Long userId) {
        return userDao.findOne(userId).getCustomer();
    }

    public User getUserById(Long userId) {
        return userDao.findOne(userId);
    }

    public User getUserByFirstNameAndLastName(String firstName, String lastName) {
        return userDao.findByFirstNameAndLastName(firstName, lastName);
    }

    public UserDto transformToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole().getName());
        userDto.setActive(user.isActive());
        if (user.getCustomer() != null) {
            userDto.setCustomerId(user.getCustomer().getId());
        }
        return userDto;
    }

    public User toUser(UserDto userDto) {
        User user = new User();
        List<Role> roles = roleDao.findAll();
        user.setId(userDto.getUserId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        }

        Optional<Role> role = roles.stream().filter(y -> userDto.getRole().toUpperCase().equals(y.getName().toUpperCase())).findFirst();
        role.ifPresent(user::setRole);

        if (userDto.getActive() == null || !userDto.getActive()) {
            user.setActive(false);
        } else {
            user.setActive(true);
        }
        return user;
    }

    public void setUserProperties(Customer customer, SessionUser sessionUser) {
        if (customer != null) {
            sessionUser.setDeliveryCost(customer.getDeliveryCost());
            sessionUser.setVatRate(customer.getVatRate());
        } else {
            setDefaultUserProperties(sessionUser);
        }
    }

    public User saveUserAndGet(User userToSave) {
        User save;
        if (userToSave.getId() == null) {
            save = userDao.save(userToSave);
        } else {
            User user = userDao.findOne(userToSave.getId());
            save = userDao.save(fillUser(user, userToSave));
        }
        return save;
    }

    private void setDefaultUserProperties(SessionUser sessionUser) {
        sessionUser.setDeliveryCost(100.0);
        sessionUser.setVatRate(0.23);
    }

    private User fillUser(User user, User userToSave) {
        user.setFirstName(userToSave.getFirstName());
        user.setLastName(userToSave.getLastName());
        user.setUsername(userToSave.getUsername());
        user.setEmail(userToSave.getEmail());
        user.setRole(userToSave.getRole());
        user.setActive(userToSave.isActive());
        return user;
    }
}
