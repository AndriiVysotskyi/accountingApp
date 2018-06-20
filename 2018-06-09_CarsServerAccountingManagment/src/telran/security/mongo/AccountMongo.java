package telran.security.mongo;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accounts")
public class AccountMongo {
	@Id
	String username;
	String password;
	String[] roles;
	LocalDate expirationDate;

	public AccountMongo() {
	}

	public AccountMongo(String username, String password, String[] roles, LocalDate expirationDate) {
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.expirationDate = expirationDate;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

}
