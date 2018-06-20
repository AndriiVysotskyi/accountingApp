package telran.security.mongo.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.security.mongo.AccountMongo;

public interface AccountRepository extends MongoRepository<AccountMongo, String> {
	
	List<AccountMongo> findByRoles(String string);

}
