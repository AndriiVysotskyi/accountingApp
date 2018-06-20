package telran.security.mongo;

import telran.security.dto.Account;

public interface IAccountingManagement {
	boolean addAccount(Account account);

	boolean removeAccount(String username);

	boolean updatePassword(String username, String newpassword);
}
