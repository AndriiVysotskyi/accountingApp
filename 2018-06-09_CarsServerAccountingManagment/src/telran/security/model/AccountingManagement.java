package telran.security.model;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
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
		String password = null;
		String pwdMessage = "Enter the password: ";
		Console cons = System.console();
		if (cons == null) {
			// We are in the Eclipse IDE.
			try {
				System.out.println("LOG: Running within Eclipse IDE...");
				System.out.println("LOG: Password will not be masked");
				password = getPasswordWithinEclipse(pwdMessage);
				System.out.println("LOG: Password entered");
			} catch (Exception e) {
				System.err.println("Error getting password" + e.getMessage());
				System.exit(1);
			}
		} else {
			password = getPasswordMasked(cons, pwdMessage);
		}
		// String password = inOut.getString("Please enter password for admin user");
		String[] roles = new String[] { "ROLE_ADMIN" };
		Account acc = new Account(username, password, roles);
		addAccount(acc);
	}

	public static String getPasswordWithinEclipse(String msg) throws Exception {
		// In Eclipse IDE
		System.out.print(msg);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String password = reader.readLine();
		if (password != null) {
			if (password.length() <= 0) {
				System.out.println("Invalid input\n");
				throw new Exception("Error reading in password");
			}
		}
		return password;
	}

	public static String getPasswordMasked(Console cons, String msg) {
		char[] passwd;
		while (true) {
			passwd = cons.readPassword("%s", msg);
			if (passwd != null) {
				if (passwd.length > 0) {
					return new String(passwd);
				} else {
					System.out.println("Invalid input\n");
				}
			}
		}
	}
}
