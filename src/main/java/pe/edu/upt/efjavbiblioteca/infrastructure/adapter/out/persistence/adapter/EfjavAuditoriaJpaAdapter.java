package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.adapter;

import org.springframework.stereotype.Component;
import pe.edu.upt.efjavbiblioteca.domain.port.out.EfjavInfaAuditoriaRepository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavAuditoriaEntity;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository.EfjavAuditoriaJpaRepository;

@Component
public class EfjavAuditoriaJpaAdapter implements EfjavInfaAuditoriaRepository {

    private final EfjavAuditoriaJpaRepository efjavAuditoriaJpaRepository;

    public EfjavAuditoriaJpaAdapter(EfjavAuditoriaJpaRepository efjavAuditoriaJpaRepository) {
        this.efjavAuditoriaJpaRepository = efjavAuditoriaJpaRepository;
    }

    @Override
    public void efjavMtdRegistrar(String efjavUsuarioCodigo, String efjavOperacion, String efjavEntidad,
                                  String efjavDetalle, String efjavIp, String efjavEquipo, String efjavResultado) {
        EfjavAuditoriaEntity entity = new EfjavAuditoriaEntity(
                efjavUsuarioCodigo,
                efjavOperacion,
                efjavEntidad,
                efjavDetalle,
                efjavIp != null ? efjavIp : "127.0.0.1",
                efjavEquipo != null ? efjavEquipo : "DESCONOCIDO",
                efjavResultado
        );
        efjavAuditoriaJpaRepository.save(entity);
    }
}
