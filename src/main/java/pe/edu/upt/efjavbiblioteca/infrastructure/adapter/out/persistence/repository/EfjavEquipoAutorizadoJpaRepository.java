package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavEquipoAutorizadoEntity;

import java.util.Optional;

@Repository
public interface EfjavEquipoAutorizadoJpaRepository extends JpaRepository<EfjavEquipoAutorizadoEntity, Long> {
    Optional<EfjavEquipoAutorizadoEntity> findByEfjavIdentificadorEquipo(String efjavIdentificadorEquipo);
}
