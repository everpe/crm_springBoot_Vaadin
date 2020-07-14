package spring.boot.me.ui.view.list;

import java.util.List;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import spring.boot.me.backend.entity.Company;
import spring.boot.me.backend.entity.Contact;

public class ContactForm extends FormLayout {
	
	//Campos del fomrulario para un Contacto
	TextField firstName = new TextField("First name"); 
	TextField lastName = new TextField("Last name");
	EmailField email = new EmailField("Email");
	
	//Lista de los estados del Contacto
	ComboBox<Contact.Status> status = new ComboBox<>("Status");
	//Lista de todas las compañias
	ComboBox<Company> company = new ComboBox<>("Company");
	
	//Botones del Form
	Button save = new Button("Save");
	Button delete = new Button("Delete");
	Button close = new Button("Cancel");
	
	//Clase binder para vincular el Object Contact con el fomrulario
	Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);
	
	//El contacto con el que se hará en Binding, este se setea no por constructor desde la MainView
	private Contact contact;
	
	
	
	/**
	 * 
	 * @param companies, las compañias para el combobox de compañias
	 * @param contact, el contacto con el que se establecerá el binding
	 */
	public ContactForm(List<Company> companies) {
		
		addClassName("contact-form");//le define nombre de Clase CSS para el form
		binder.bindInstanceFields(this); 
		
		//le agrega las copañias que llegan de la bd para poder modificarsela al Contacto.
		company.setItems(companies);
		company.setItemLabelGenerator(Company::getName); 
		//Agrega todos los estados a la lista para el campo estado del contacto.
		status.setItems(Contact.Status.values()); 
		
		addClassName("contact-form"); 
		add(firstName,lastName,email,company,status,createButtonsLayout()); //llama a la configuracion de los botones
	}
	
	
	/**
	 * Define el contacto con el que se vinculará el Form.
	 * @param contact, el Objeto Contacto que llegara de la Grid UI.
	 */
	public void setContact(Contact contact) {
		this.contact = contact; 
		binder.readBean(contact); 
	}

	
	
	/**
	 * Acomoda los botones en plantilla horizontal, ademas de definir sus estilos.
	 * @return,la plantilla horizontal con los botones.
	 */
	private HorizontalLayout createButtonsLayout() {
		
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY); 
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		//Agrega eventos de teclado para abrir o cerrar el formulario
		save.addClickShortcut(Key.ENTER); 
		close.addClickShortcut(Key.ESCAPE);
		//return new HorizontalLayout(save, delete, close); 
		
		//Eventos para los botones
		save.addClickListener(event -> validateAndSave()); 
		delete.addClickListener(event -> fireEvent(new DeleteEvent(this, contact))); 
		close.addClickListener(event -> fireEvent(new CloseEvent(this))); 
		
		//Valida el form cada vez que hay un cambio.si es invalido desabilitra el boton de Save
		binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid())); 
		return new HorizontalLayout(save, delete, close);
	}
	 
	
	private void validateAndSave() {
		try {
			binder.writeBean(contact); 
			fireEvent(new SaveEvent(this, contact)); 
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}

	
	
	
	// Events API para el fomrulario, con el fin de que sea reutilizable ???????
	public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
		private Contact contact;
		
		protected ContactFormEvent(ContactForm source, Contact contact) { 
			super(source, false);
			this.contact = contact;
		}
		public Contact getContact() {
			return contact;
		}
	}
	public static class SaveEvent extends ContactFormEvent {
		SaveEvent(ContactForm source, Contact contact) {
			super(source, contact);
		}
	}
	
	public static class DeleteEvent extends ContactFormEvent {
		DeleteEvent(ContactForm source, Contact contact) {
			super(source, contact);
		}
	}
	
	public static class CloseEvent extends ContactFormEvent {
		CloseEvent(ContactForm source) {
			super(source, null);
		}
	}
	
	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) { 
		return getEventBus().addListener(eventType, listener);
	}
	
	
	

}
