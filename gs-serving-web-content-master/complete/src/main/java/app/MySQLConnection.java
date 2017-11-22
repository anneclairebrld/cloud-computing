package app;

import java.util.List;
import org.springframework.data.repository.CrudRepository;


//MySQLConnection inherits several methods for working with User class: ex: saving, deleting .. finding ..User entities
public interface MySQLConnection extends CrudRepository<User, Long>{
    List<User> findByLastName(String lastName);
}


