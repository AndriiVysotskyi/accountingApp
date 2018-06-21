package telran.view;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ConsoleInputOutput implements InputOutput {
	Scanner scanner = new Scanner(System.in);

	@Override
	public String getString(String prompt) {
		System.out.println(prompt);
		return scanner.nextLine();
	}

	@Override
	public void display(Object obj) {
		System.out.print(obj);

	}

	@Override
	public String getPassword(String prompt) {
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
		return password;
	}

	private static String getPasswordWithinEclipse(String msg) throws Exception {
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

	private static String getPasswordMasked(Console cons, String msg) {
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
