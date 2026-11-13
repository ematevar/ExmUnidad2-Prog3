package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavFirmaEntity;

@Repository
public interface EfjavFirmaJpaRepository extends JpaRepository<EfjavFirmaEntity, Long> {
}
