package spring.boot.me.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


/**
 * Configuracion de Spring security
 * @author Ever
 */

@EnableWebSecurity //Activa el Spring Security
@Configuration //Le avisa a SpringBoot que esta clase será para configurar seguridad
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String LOGIN_PROCESSING_URL = "/login";
	private static final String LOGIN_FAILURE_URL = "/login?error";
	private static final String LOGIN_URL = "/login";
	private static final String LOGOUT_SUCCESS_URL = "/login";

	/*
	 *Bloquea solicitudes no autenticadas en todas las páginas, excepto la página de inicio de sesión 
	 */
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() //falsicación de solicitudes entre sitios
		  .requestCache().requestCache(new CustomRequestCache()) //rastrea solicitudes no autorizadas
		  .and().authorizeRequests() //Activa la autorización
		  .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()//permite el trafico interno
		  
		  .anyRequest().authenticated() //permite el trafico en general
		  
		  .and().formLogin() //permite el Login y acceso de users no identificados
		  .loginPage(LOGIN_URL).permitAll()
		  .loginProcessingUrl(LOGIN_PROCESSING_URL) //configura las url del pagina de Login
		  .failureUrl(LOGIN_FAILURE_URL)
		  .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL); //configura URL de cierre
	}

	/**
	 * Configura usuarios de prueba, no es lo recomendado hacerlo desde el codigo.
	 */
	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		UserDetails user =
				User.withUsername("user").password("{noop}password").roles("USER").build();
		UserDetails user2 =
				User.withUsername("user2").password("{noop}password2").roles("USER").build();
	  return new InMemoryUserDetailsManager(user,user2);
	}
	
	
	/**
	 * excluir la comunicación Vaadin-framework y los activos estáticos de SpringSeguridad
	 */
	@Override
	public void configure(WebSecurity web) {
	  web.ignoring().antMatchers(
	  "/VAADIN/**",
	  "/favicon.ico",
	  "/robots.txt",
	  "/manifest.webmanifest",
	  "/sw.js",
	  "/offline.html",
	  "/icons/**",
	  "/images/**",
	  "/styles/**",
	  "/h2-console/**");
	}
	  
	
}
