package pe.edu.upt.efjavbiblioteca.domain.port.out;

import pe.edu.upt.efjavbiblioteca.domain.model.EfjavClsNotificacion;
import java.util.List;
import java.util.Optional;

public interface EfjavInfaNotificacionRepository {
    EfjavClsNotificacion efjavMtdGuardar(EfjavClsNotificacion efjavNotificacion);
    List<EfjavClsNotificacion> efjavMtdListarPorDestinatario(Long efjavDestinatarioId);
    Optional<EfjavClsNotificacion> efjavMtdBuscarPorId(Long efjavId);
    long efjavMtdContarNoLeidas(Long efjavDestinatarioId);
}
