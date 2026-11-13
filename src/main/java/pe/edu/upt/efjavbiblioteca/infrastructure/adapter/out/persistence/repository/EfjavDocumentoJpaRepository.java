package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavDocumentoEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface EfjavDocumentoJpaRepository extends JpaRepository<EfjavDocumentoEntity, Long> {
    List<EfjavDocumentoEntity> findByEfjavTramiteId(Long efjavTramiteId);
    Optional<EfjavDocumentoEntity> findFirstByEfjavTramiteIdOrderByEfjavFechaSubidaDesc(Long efjavTramiteId);
}
