package pe.edu.upt.efjavbiblioteca.domain.port.out;

import pe.edu.upt.efjavbiblioteca.domain.model.EfjavClsPrestamo;
import java.util.List;
import java.util.Optional;

public interface EfjavInfaPrestamoRepository {
    EfjavClsPrestamo efjavMtdGuardar(EfjavClsPrestamo efjavPrestamo);
    Optional<EfjavClsPrestamo> efjavMtdBuscarPorId(Long efjavId);
    Optional<EfjavClsPrestamo> efjavMtdBuscarPorCodigo(String efjavCodigo);
    List<EfjavClsPrestamo> efjavMtdListarPorEstudiante(Long efjavEstudianteId);
    List<EfjavClsPrestamo> efjavMtdListarTodos();
    List<EfjavClsPrestamo> efjavMtdListarPendientes();
}
