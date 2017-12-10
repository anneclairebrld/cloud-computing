package database;
import org.springframework.data.repository.CrudRepository;

//this will automatically be done by spring. CRUD : create, read, update and delete authorizations basically
public interface StorageRepository extends CrudRepository<Storage, Long>{
}
