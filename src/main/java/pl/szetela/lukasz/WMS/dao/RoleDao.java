package pl.szetela.lukasz.WMS.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.szetela.lukasz.WMS.models.Role;

public interface RoleDao extends JpaRepository<Role, Long> {
}
