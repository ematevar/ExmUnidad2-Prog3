package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavLibroEntity;

import java.util.Optional;

@Repository
public interface EfjavLibroJpaRepository extends JpaRepository<EfjavLibroEntity, Long> {
    Optional<EfjavLibroEntity> findByEfjavCodigo(String efjavCodigo);
}
