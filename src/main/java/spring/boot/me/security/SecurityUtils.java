package spring.boot.me.security;

import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

/**
 * Clase con los metodos utiles
 * @author Ever,
 *
 */
public final class SecurityUtils {

	private SecurityUtils() {
		
	}
	
	/**
	 * Determina si la solicitud es interna para Vaadin.
	 * @param request,la peticion que llega .
	 * @return, verdadero si es una solicitud de vaadin, falso de lo contrario.
	 */
	static boolean isFrameworkInternalRequest(HttpServletRequest request) {
		
		final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
		
		return parameterValue != null && Stream.of(ServletHelper.RequestType.values())
				.anyMatch(r -> r.getIdentifier().equals(parameterValue));  
	}
	
	/**
	 * Verifica si el User actual está logueado.
	 * @return
	 */
	 static boolean isUserLoggedIn() { 
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 
		//Como Spring Security siempre creará un {@link AnonymousAuthenticationToken}
	     //Tenemos que ignorar esos tokens explícitamente.
		return authentication != null
		  && !(authentication instanceof AnonymousAuthenticationToken)
		  && authentication.isAuthenticated();
		  
	}
	
	
}
