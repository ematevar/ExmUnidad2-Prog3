package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavUsuarioEntity;

import java.util.Optional;

@Repository
public interface EfjavUsuarioJpaRepository extends JpaRepository<EfjavUsuarioEntity, Long> {
    Optional<EfjavUsuarioEntity> findByEfjavCodigo(String efjavCodigo);
    Optional<EfjavUsuarioEntity> findByEfjavCorreo(String efjavCorreo);
}
