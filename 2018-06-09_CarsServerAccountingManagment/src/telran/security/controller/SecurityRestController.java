package telran.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.security.dto.Account;
import telran.security.dto.SecurityApiConstants;
import telran.security.mongo.IAccountingManagement;

@EnableMongoRepositories("telran.security.mongo.repo")
@ComponentScan({ "telran.security.model", "telran.security" })
@EntityScan("telran.security.mongo")
@RestController
public class SecurityRestController {

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	IAccountingManagement accountManagment;

	@PostMapping(value = SecurityApiConstants.ADD_USER)
	boolean addUser(@RequestBody Account account) {
		return accountManagment.addAccount(account);
	}

	@RequestMapping(value = SecurityApiConstants.REMOVE_USER)
	boolean removeUser(String username) {
		return accountManagment.removeAccount(username);
	}

	@PostMapping(value = SecurityApiConstants.CHANGE_PASSWORD)
	boolean updatePassword(@RequestBody Account account) {
		return accountManagment.updatePassword(account.getUsername(), account.getPassword());
	}

}
