package pl.szetela.lukasz.WMS.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.szetela.lukasz.WMS.models.User;

public interface UserDao extends JpaRepository<User, Long> {

    User findUserByUsername(String username);
    User findByFirstNameAndLastName(String firstName, String lastName);

}
