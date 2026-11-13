package pe.edu.upt.efjavbiblioteca.domain.port.out;

import pe.edu.upt.efjavbiblioteca.domain.model.EfjavClsTramite;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EfjavInfaTramiteRepository {
    EfjavClsTramite efjavMtdGuardar(EfjavClsTramite efjavTramite);
    Optional<EfjavClsTramite> efjavMtdBuscarPorId(Long efjavId);
    Optional<EfjavClsTramite> efjavMtdBuscarPorCodigo(String efjavCodigo);
    List<EfjavClsTramite> efjavMtdListarPorEstudiante(Long efjavEstudianteId);
    List<EfjavClsTramite> efjavMtdListarPorUbicacion(Long efjavInstanciaId);
    List<EfjavClsTramite> efjavMtdListarCulminados(LocalDateTime fechaInicio, LocalDateTime fechaFin, String criterio);
}
