package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavPenalidadEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface EfjavPenalidadJpaRepository extends JpaRepository<EfjavPenalidadEntity, Long> {
    List<EfjavPenalidadEntity> findByEfjavEstudianteId(Long efjavEstudianteId);
    Optional<EfjavPenalidadEntity> findFirstByEfjavEstudianteIdAndEfjavEstado(Long efjavEstudianteId, String efjavEstado);
}
