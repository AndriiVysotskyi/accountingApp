package telran.security.model;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import telran.security.dto.Account;
import telran.security.mongo.AccountMongo;
import telran.security.mongo.IAccountingManagement;
import telran.security.mongo.repo.AccountRepository;
import telran.view.ConsoleInputOutput;
import telran.view.InputOutput;

@Service
public class AccountingManagement implements IAccountingManagement {
	@Value("${exp_days}")
	int tTL;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	AccountRepository accounts;

	@Override
	public boolean addAccount(Account account) {
		if (accounts.existsById(account.getUsername())) {
			return false;
		}
		AccountMongo accountMongo = new AccountMongo(account.getUsername(), encoder.encode(account.getPassword()),
				account.getRoles(), LocalDate.now().plusDays(tTL));
		accounts.save(accountMongo);
		return true;
	}

	@Override
	public boolean removeAccount(String username) {
		if (!accounts.existsById(username)) {
			return false;
		}
		accounts.deleteById(username);
		return true;
	}

	@Override
	public boolean updatePassword(String username, String newpassword) {
		if (!accounts.existsById(username)) {
			return false;
		}
		AccountMongo accountMongo = accounts.findById(username).get();

		if (encoder.encode(newpassword).matches(accountMongo.getPassword())) {
			return false;
		}
		accountMongo.setPassword(encoder.encode(newpassword));
		accountMongo.setExpirationDate(LocalDate.now().plusDays(tTL));
		accounts.save(accountMongo);
		return true;
	}

	@Component
	public class EventListenerExampleBean {

		@EventListener
		public void onApplicationEvent(ContextRefreshedEvent event) {

			adminCheck("ROLE_ADMIN");
		}
	}

	public void adminCheck(String string) {
		if (accounts.findByRoles(string).isEmpty()) {
			createAdmin();
		}

	}

	private void createAdmin() {
		InputOutput inOut = new ConsoleInputOutput();
		System.out.println("######There is no Admin account in database#####");
		System.out.println("To start working:");
		String username = inOut.getString("Please enter username for admin user");

		String password = inOut.getPassword("Please enter password for admin user");
		String[] roles = new String[] { "ROLE_ADMIN" };
		Account acc = new Account(username, password, roles);
		addAccount(acc);
	}

}
