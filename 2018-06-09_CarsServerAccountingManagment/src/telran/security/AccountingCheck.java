package telran.security;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import telran.security.mongo.IAccounts;

@Configuration
public class AccountingCheck implements UserDetailsService {
	@Autowired
	IAccounts accounts;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if (!accounts.isUser(username))
			throw new UsernameNotFoundException("user not found");
		LocalDate expirationDate = accounts.getExpirationDate(username);
		if (expirationDate.isBefore(LocalDate.now()))
			throw new UsernameNotFoundException("user not found");
		UserDetails user = User.withUsername(username).password(accounts.getPassword(username))
				.authorities(accounts.getRoles(username)).build();
		return user;

		// String password = accounts.getPassword(username);
		// if (password == null)
		// throw new UsernameNotFoundException("");
		// return new User(username, "{bcrypt}" + password,
		// AuthorityUtils.createAuthorityList(accounts.getRoles(username)));

		// String password = accounts.getPassword(username);
		// if (password == null)
		// throw new UsernameNotFoundException("");
		// return new User(username, password,
		// AuthorityUtils.createAuthorityList(accounts.getRoles(username)));
	}
}
