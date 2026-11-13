package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavPrestamoEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface EfjavPrestamoJpaRepository extends JpaRepository<EfjavPrestamoEntity, Long> {
    Optional<EfjavPrestamoEntity> findByEfjavCodigo(String efjavCodigo);
    List<EfjavPrestamoEntity> findByEfjavEstudianteId(Long efjavEstudianteId);
    List<EfjavPrestamoEntity> findByEfjavEstado(String efjavEstado);
}
