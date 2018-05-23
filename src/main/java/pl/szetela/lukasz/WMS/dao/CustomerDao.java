package pl.szetela.lukasz.WMS.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.szetela.lukasz.WMS.models.Customer;

public interface CustomerDao extends JpaRepository<Customer, Long> {
}
