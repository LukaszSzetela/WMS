package pl.szetela.lukasz.WMS.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.szetela.lukasz.WMS.dao.CustomerDao;
import pl.szetela.lukasz.WMS.dto.CustomerDto;
import pl.szetela.lukasz.WMS.models.Customer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class CustomerService {

    private CustomerDao customerDao;

    @Autowired
    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public CustomerDto transformToDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(customer.getId());
        customerDto.setName(customer.getName());
        customerDto.setDeliveryCost(customer.getDeliveryCost());
        customerDto.setEmail(customer.getEmail());
        customerDto.setVatRate(customer.getVatRate());
        customerDto.setNip(customer.getNip());
        customerDto.setRegon(customer.getRegon());
        return customerDto;
    }

    public Customer toCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setId(customerDto.getCustomerId());
        customer.setName(customerDto.getName());
        customer.setDeliveryCost(customerDto.getDeliveryCost());
        customer.setEmail(customerDto.getEmail());
        customer.setVatRate(customerDto.getVatRate());
        customer.setRegon(customerDto.getRegon());
        customer.setNip(customerDto.getNip());
        customer.setFromDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return customer;
    }

    public Customer saveCustomerAndGet(Customer customerToSave) {
        Customer save;
        if (customerToSave.getId() == null) {
            save = customerDao.save(customerToSave);
        } else {
            Customer customer = customerDao.findOne(customerToSave.getId());
            save = customerDao.save(fillCustomer(customer, customerToSave));
        }
        return save;
    }

    private Customer fillCustomer(Customer customer, Customer customerToSave) {
        customer.setName(customerToSave.getName());
        customer.setNip(customerToSave.getNip());
        customer.setRegon(customerToSave.getRegon());
        customer.setEmail(customerToSave.getEmail());
        customer.setVatRate(customerToSave.getVatRate());
        customer.setDeliveryCost(customerToSave.getDeliveryCost());
        return customer;
    }
}
