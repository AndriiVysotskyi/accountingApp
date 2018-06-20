package telran.security.mongo;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.security.mongo.repo.AccountRepository;

@Service
public class AccountsImpl implements IAccounts {
	@Autowired
	AccountRepository accounts;

	@Override
	public String getPassword(String username) {
		return accounts.findById(username).get().getPassword();
	}

	@Override
	public String[] getRoles(String username) {
		return accounts.findById(username).get().getRoles();
	}

	@Override
	public boolean isUser(String username) {
		return accounts.existsById(username);
	}

	@Override
	public LocalDate getExpirationDate(String username) {
		return accounts.findById(username).get().getExpirationDate();
	}

}
