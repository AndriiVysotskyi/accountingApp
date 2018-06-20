package telran.security.mongo;

import java.time.LocalDate;

public interface IAccounts {
	String getPassword(String username);

	String[] getRoles(String username);

	boolean isUser(String username);

	LocalDate getExpirationDate(String username);

}
