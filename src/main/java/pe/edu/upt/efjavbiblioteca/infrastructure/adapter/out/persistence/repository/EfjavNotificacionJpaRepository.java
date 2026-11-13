package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavNotificacionEntity;

import java.util.List;

@Repository
public interface EfjavNotificacionJpaRepository extends JpaRepository<EfjavNotificacionEntity, Long> {
    List<EfjavNotificacionEntity> findByEfjavDestinatarioIdOrderByEfjavFechaCreacionDesc(Long efjavDestinatarioId);
    long countByEfjavDestinatarioIdAndEfjavLeidaFalse(Long efjavDestinatarioId);
}
