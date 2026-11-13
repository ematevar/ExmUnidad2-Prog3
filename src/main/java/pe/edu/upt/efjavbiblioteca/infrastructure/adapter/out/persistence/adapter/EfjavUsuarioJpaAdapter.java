package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.adapter;

import org.springframework.stereotype.Component;
import pe.edu.upt.efjavbiblioteca.domain.model.EfjavClsUsuario;
import pe.edu.upt.efjavbiblioteca.domain.port.out.EfjavInfaUsuarioRepository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavUsuarioEntity;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.mapper.EfjavUsuarioMapper;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository.EfjavUsuarioJpaRepository;

import java.util.Optional;

@Component
public class EfjavUsuarioJpaAdapter implements EfjavInfaUsuarioRepository {

    private final EfjavUsuarioJpaRepository efjavUsuarioJpaRepository;
    private final EfjavUsuarioMapper efjavUsuarioMapper;

    public EfjavUsuarioJpaAdapter(EfjavUsuarioJpaRepository efjavUsuarioJpaRepository, EfjavUsuarioMapper efjavUsuarioMapper) {
        this.efjavUsuarioJpaRepository = efjavUsuarioJpaRepository;
        this.efjavUsuarioMapper = efjavUsuarioMapper;
    }

    @Override
    public Optional<EfjavClsUsuario> efjavMtdBuscarPorCodigo(String efjavCodigo) {
        return efjavUsuarioJpaRepository.findByEfjavCodigo(efjavCodigo)
                .map(efjavUsuarioMapper::efjavMtdToDomain);
    }

    @Override
    public Optional<EfjavClsUsuario> efjavMtdBuscarPorCorreo(String efjavCorreo) {
        return efjavUsuarioJpaRepository.findByEfjavCorreo(efjavCorreo)
                .map(efjavUsuarioMapper::efjavMtdToDomain);
    }

    @Override
    public Optional<EfjavClsUsuario> efjavMtdBuscarPorId(Long efjavId) {
        return efjavUsuarioJpaRepository.findById(efjavId)
                .map(efjavUsuarioMapper::efjavMtdToDomain);
    }

    @Override
    public EfjavClsUsuario efjavMtdGuardar(EfjavClsUsuario efjavUsuario) {
        EfjavUsuarioEntity entity = null;
        if (efjavUsuario.getEfjavId() != null) {
            entity = efjavUsuarioJpaRepository.findById(efjavUsuario.getEfjavId()).orElse(new EfjavUsuarioEntity());
        } else {
            entity = new EfjavUsuarioEntity();
        }
        efjavUsuarioMapper.efjavMtdUpdateEntity(efjavUsuario, entity);
        EfjavUsuarioEntity guardado = efjavUsuarioJpaRepository.save(entity);
        return efjavUsuarioMapper.efjavMtdToDomain(guardado);
    }
}
