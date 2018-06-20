package telran.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class AccoutntingAuthorization extends WebSecurityConfigurerAdapter {
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.httpBasic();
		// http.authorizeRequests().anyRequest().permitAll();
		http.authorizeRequests().anyRequest().hasRole("ADMIN");
	}

}
