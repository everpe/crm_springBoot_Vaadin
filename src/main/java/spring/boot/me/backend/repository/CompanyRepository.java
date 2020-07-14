package spring.boot.me.backend.repository;

import spring.boot.me.backend.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

//Ya trae muchos metodos de consulta con la bd implementados que se usan en Los servicios
public interface CompanyRepository extends JpaRepository<Company, Long> {
	
}
