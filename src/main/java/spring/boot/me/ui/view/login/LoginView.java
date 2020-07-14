package spring.boot.me.ui.view.login;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Collections;

@Route("login") 
@PageTitle("Login | My CRM")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
	
	//login provisto por vaadin que va a usar Spring
	private LoginForm login = new LoginForm();
	
	//vista del fomrmulario
	public LoginView(){
		 addClassName("login-view");
		 setSizeFull();
		 setAlignItems(Alignment.CENTER); //Centra todo lo que vaya a tener
		 setJustifyContentMode(JustifyContentMode.CENTER);
		 
		 login.setAction("login"); //publica la Accion Post para SpringSecurity
		 add(new H1("Vaadin CRM"), login);
	}
	
	
	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		// inform the user about an authentication error
		if(beforeEnterEvent.getLocation() 
			.getQueryParameters().getParameters().containsKey("error")) {
				login.setError(true);
		}
	}
	
}
