package spring.boot.me.backend.repository;

import spring.boot.me.backend.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	
	//CONSULTA Personalizada JPQL para consultas con JPA 
	 @Query("select c from Contact c " +
			 "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
			 "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))") 
	 List<Contact> search(@Param("searchTerm") String searchTerm); 
	
}

