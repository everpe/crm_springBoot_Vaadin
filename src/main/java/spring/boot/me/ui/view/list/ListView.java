package spring.boot.me.ui.view.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import spring.boot.me.backend.entity.Company;
import spring.boot.me.backend.entity.Contact;
import spring.boot.me.backend.service.CompanyService;
import spring.boot.me.backend.service.ContactService;
import spring.boot.me.ui.MainLayout;

import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "", layout = MainLayout.class) // coincide con la ruta vacia, y se le añade vista padre el MainLayout
@PageTitle("Contacts | Vaadin CRM")
public class ListView extends VerticalLayout {

	private Grid<Contact> grid = new Grid<>(Contact.class);
	// Inyection de Spring para conectar el backend con UI.
	private ContactService contactService;
	// para el filtro
	private TextField filterText = new TextField();
	// Para el Form
	private ContactForm form;

	/**
	 * Construct a new Vaadin view. Build the initial UI state for the user
	 * accessing the application.
	 * 
	 * @param service The message service. Automatically injected Spring managed
	 *                bean.
	 */
    public ListView(ContactService contactService,CompanyService companyService) {
    	this.contactService = contactService; 
		addClassName("list-view");//Clase para el CSS
		setSizeFull();
		HorizontalLayout horizontal=getToolbar(); 
		configureGrid();
		
		
		//Se le envia ese parametro al contructor para que inicialice el campo de compañias
		form = new ContactForm(companyService.findAll());
		
		//llama el save API del ContactForm y envia el evento que contiene el contacto seleccionado
		form.addListener(ContactForm.SaveEvent.class, this::saveContact); 
		form.addListener(ContactForm.DeleteEvent.class, this::deleteContact); 
		//promesa para cerrar el form
		form.addListener(ContactForm.CloseEvent.class, e -> closeEditor());
		
		
		
		//Contenedor para la tabla y el Form
		Div content = new Div(grid, form);
		content.addClassName("content");//le da los estilos para que se acomde horizontal en CSS
		content.setSizeFull();
		
		add(horizontal,content);
		
		updateList();
		//limpia Contacts viejos,cierra el form
		closeEditor();
    }

	/**
	 * Obtiene el contacto del evento y lo envia al servicio para que lo guarde
	 * 
	 * @param event, el evento de seleccionar una fila de la grid
	 */
	private void saveContact(ContactForm.SaveEvent event) {
		contactService.save(event.getContact());
		updateList();
		closeEditor();
	}

	/**
	 * Obtiene el Contacto del evento para enviarselo al servicio y que lo elimine.
	 * 
	 * @param event, el evento de seleccionar una fila de la grid.
	 */
	private void deleteContact(ContactForm.DeleteEvent event) {
		contactService.delete(event.getContact());
		updateList();
		closeEditor();
	}

	/**
	 * Define las columnas de la Grid(Tabla).
	 */
	private void configureGrid() {
		grid.addClassName("contact-grid");// Clase para el CSS
		grid.setSizeFull();
		grid.removeColumnByKey("company");
		grid.setColumns("firstName", "lastName", "email", "status");// define las columnas normales
		grid.addColumn(contact -> {
			Company company = contact.getCompany();
			return company == null ? "-" : company.getName();// nonmbre de compañia o guion si esta vacia
		}).setHeader("Company");

		grid.getColumns().forEach(col -> col.setAutoWidth(true));// hace que cada columna tome un width automatico
																	// dependiendo su contenido

		// Obtiene el Contact seleccionado en la Grid
		grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));
	}

	/**
	 * Establece el contacto seleccionado en ContactForm y oculta o muestra el form,
	 * dependiendo de la selección. También establece el nombre de la clase CSS de
	 * "edición"
	 * 
	 * @param contact,el contacto que se obtuvó en el evento
	 */
	public void editContact(Contact contact) {
		if (contact == null) {
			closeEditor();
		} else {
			form.setContact(contact);
			form.setVisible(true);
			addClassName("editing");
		}
	}

	/**
	 * Cierra el Formulario.
	 */
	private void closeEditor() {
		form.setContact(null);
		form.setVisible(false);
		removeClassName("editing");
	}

	/**
	 * Estilos del campo Filtro y el boton para Nuevo Contacto
	 */
	private HorizontalLayout getToolbar() {
		filterText.setPlaceholder("Filtrar por Nombre...");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);// para que cambie apenas se escriba
		filterText.addValueChangeListener(e -> updateList());

		// Boton para nuevo Contacto
		Button addContactButton = new Button("Add contact", new Icon(VaadinIcon.PLUS_CIRCLE));
		addContactButton.addClickListener(click -> addContact()); // Evento click para el boton

		HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
		toolbar.addClassName("toolbar");// nombre de clase CSS para la plantilla horizontal
		return toolbar;
	}

	/**
	 * Actualiza la lista de Contacts de la GRID.
	 */
	private void updateList() {
		// Se le agrega el parametro del filtro para que realice la consulta del Contact
		// repository
		grid.setItems(contactService.findAll(filterText.getValue()));
	}

	/**
	 * Limpia cualquier objeto contacto seleccionado previo, y crea una instancia
	 * nueva
	 */
	void addContact() {
		grid.asSingleSelect().clear();
		editContact(new Contact());
	}

	
}
