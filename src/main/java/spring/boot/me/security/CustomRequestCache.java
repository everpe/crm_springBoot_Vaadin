package spring.boot.me.security;

import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 *Un caché para realizar un seguimiento de las solicitudes no autenticadas 
 */
public class CustomRequestCache extends HttpSessionRequestCache {

	/**
	 *Guarda las solicitudes no autenticadas para que podamos redirigir al usuario a la página que
	 *intentaban acceder una vez que iniciaron sesión
	 */
	 @Override
	 public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
		 
		 if (!SecurityUtils.isFrameworkInternalRequest(request)) {
			 super.saveRequest(request, response);
		 }
	 }
	 
	 

}
