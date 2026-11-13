package pe.edu.upt.efjavbiblioteca.domain.port.out;

import pe.edu.upt.efjavbiblioteca.domain.model.EfjavClsEquipoAutorizado;
import java.util.List;
import java.util.Optional;

public interface EfjavInfaEquipoAutorizadoRepository {
    Optional<EfjavClsEquipoAutorizado> efjavMtdBuscarPorIdentificador(String efjavIdentificador);
    List<EfjavClsEquipoAutorizado> efjavMtdListarTodos();
    EfjavClsEquipoAutorizado efjavMtdGuardar(EfjavClsEquipoAutorizado efjavEquipo);
}
