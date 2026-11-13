package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavSesionEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface EfjavSesionJpaRepository extends JpaRepository<EfjavSesionEntity, Long> {
    Optional<EfjavSesionEntity> findByEfjavSessionToken(String efjavSessionToken);
    List<EfjavSesionEntity> findByEfjavUsuarioIdAndEfjavEstado(Long efjavUsuarioId, String efjavEstado);
}
