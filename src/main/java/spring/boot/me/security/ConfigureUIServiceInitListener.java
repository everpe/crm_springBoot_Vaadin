package spring.boot.me.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import spring.boot.me.ui.view.login.LoginView;
import org.springframework.stereotype.Component;

/**
 * Restringe el acceso a las vistas de Vaadin.
 * @author Ever
 *
 */
@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

	@Override
	public void serviceInit(ServiceInitEvent event) {
		event.getSource().addUIInitListener(uiEvent -> { 
			final UI ui = uiEvent.getUI();
			ui.addBeforeEnterListener(this::authenticateNavigation);
	  });
	}

	
	private void authenticateNavigation(BeforeEnterEvent event) {
		if (!LoginView.class.equals(event.getNavigationTarget())
				&& !SecurityUtils.isUserLoggedIn()) {//Redirige al inicio de sesion si no está logueado
			event.rerouteTo(LoginView.class);
		}
  }

}
