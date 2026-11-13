package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavMovimientoTramiteEntity;

import java.util.List;

@Repository
public interface EfjavMovimientoTramiteJpaRepository extends JpaRepository<EfjavMovimientoTramiteEntity, Long> {
    List<EfjavMovimientoTramiteEntity> findByEfjavTramiteIdOrderByEfjavFechaMovimientoAsc(Long efjavTramiteId);
}
