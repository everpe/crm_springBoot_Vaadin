package spring.boot.me.backend.service;

import spring.boot.me.backend.entity.Company;
import spring.boot.me.backend.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyService {

	 private CompanyRepository companyRepository;
	 
	  public CompanyService(CompanyRepository companyRepository) {
		  this.companyRepository = companyRepository;
	  }
	  
	  /**
	   * Encuentra todas las compañias de la bd mediante el repositorio.
	   * @return
	   */
	  public List<Company> findAll() {
		  return companyRepository.findAll();
	  }
	  
	  /**
	   * Para pintar cantidad de contactos por Compañia
	   * @return,mapa clave-valor,Nombre de compañia:cantidad de contactos
	   */
	  public Map<String, Integer> getStats() {
		  HashMap<String, Integer> stats = new HashMap<>();
		  findAll().forEach(company -> stats.put(company.getName(), company.getEmployees().size())); 
		  return stats;
	  }


	
}
