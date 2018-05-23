package pl.szetela.lukasz.WMS.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.szetela.lukasz.WMS.models.Order;

@Repository
@Transactional
public interface OrderDao extends JpaRepository<Order, Long>, IOrderDao {
}
