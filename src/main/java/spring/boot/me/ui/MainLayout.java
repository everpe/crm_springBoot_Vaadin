package spring.boot.me.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

import spring.boot.me.ui.view.list.ListView;
import spring.boot.me.ui.view.dashboard.DashboardView;

//tiene los estilos propios y de vistas hijas
@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {//le da un encabezado y diseño Responsive(AppLayout)
	
	public MainLayout() {
		createHeader();
		createDrawer();
	}
	
	private void createHeader() {
		H1 logo = new H1("Vaadin CRM");
		logo.addClassName("logo");
		//logout
		Anchor logout = new Anchor("logout", "Log out");
		
		//encabezado con icono, titulo y el link de logout
		HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo,logout);
		header.expand(logo);//agranda el logo para que el link de logout se desplace a la derecha
		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER); //centra los componentes verticalmente
		header.setWidth("100%");
		header.addClassName("header");
		addToNavbar(header); //le agrega el header a un Nav
	}
	
	private void createDrawer() {
		RouterLink listLink = new RouterLink("ListaContactos", ListView.class); //crea el link lista, con su vista contactos
		listLink.setHighlightCondition(HighlightConditions.sameLocation()); //por si coloca ruta vacia lo redireccione tambien a "list"
	
		
		//agrega-envuelve verticalmente los links con sus vistas.
		addToDrawer(new VerticalLayout(listLink,
			new RouterLink("Dashboard", DashboardView.class)//Nuevo link Dashboar con su vista
		));
	}

}
