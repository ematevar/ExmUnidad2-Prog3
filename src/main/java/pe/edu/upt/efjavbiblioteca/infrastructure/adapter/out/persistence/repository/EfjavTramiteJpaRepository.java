package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavTramiteEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EfjavTramiteJpaRepository extends JpaRepository<EfjavTramiteEntity, Long> {
    Optional<EfjavTramiteEntity> findByEfjavCodigoSeguimiento(String efjavCodigoSeguimiento);
    List<EfjavTramiteEntity> findByEfjavEstudianteId(Long efjavEstudianteId);
    List<EfjavTramiteEntity> findByEfjavUbicacionActualIdAndEfjavEstadoNotIn(Long efjavUbicacionId, List<String> estadosExcluidos);
    List<EfjavTramiteEntity> findByEfjavEstadoInAndEfjavFechaEnvioBetween(List<String> estados, LocalDateTime inicio, LocalDateTime fin);
}
