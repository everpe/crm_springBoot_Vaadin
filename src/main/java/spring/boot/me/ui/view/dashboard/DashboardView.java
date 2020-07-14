package spring.boot.me.ui.view.dashboard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import spring.boot.me.backend.service.CompanyService;
import spring.boot.me.backend.service.ContactService;
import spring.boot.me.ui.MainLayout;

import java.util.Map;

@Route(value = "dashboard", layout = MainLayout.class) //nombre de ruta y la vista padre
@PageTitle("Dashboard | Vaadin CRM")
public class DashboardView extends VerticalLayout{
	
	private ContactService contactService;
	private CompanyService companyService;
	
	
	public DashboardView(ContactService contactService, CompanyService companyService) { 
		this.contactService = contactService;
		this.companyService = companyService;
		addClassName("dashboard-view");//nombre para CSS que tiene la vista padre
		setDefaultHorizontalComponentAlignment(Alignment.CENTER); 
		
		add(getContactStats(),getCompaniesChart());
	}
	
	
	/**
	 * Obtiene la cantidad de contactos a partir del Servicio
	 * @return, un span con el número de contactos.
	 */
	private Component getContactStats() {
		Span stats = new Span(contactService.count() + " contacts"); 
		stats.addClassName("contact-stats");//CSS
		return stats;
	}


	/**
	 * Pinta el el grafico circular.
	 * @return el grafico.
	 */
	private Chart getCompaniesChart() {
		Chart chart = new Chart(ChartType.PIE); 
		DataSeries dataSeries = new DataSeries(); 
		Map<String, Integer> companies = companyService.getStats();//obtiene el map con compañias:N° empleados
		companies.forEach((company, employees) ->
			dataSeries.add(new DataSeriesItem(company, employees))
		); 
		chart.getConfiguration().setSeries(dataSeries); 
		return chart;
	}
	
}
