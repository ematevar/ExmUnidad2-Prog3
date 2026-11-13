package pe.edu.upt.efjavbiblioteca.domain.port.out;

import pe.edu.upt.efjavbiblioteca.domain.model.EfjavClsPenalidad;
import java.util.List;
import java.util.Optional;

public interface EfjavInfaPenalidadRepository {
    EfjavClsPenalidad efjavMtdGuardar(EfjavClsPenalidad efjavPenalidad);
    List<EfjavClsPenalidad> efjavMtdListarPorEstudiante(Long efjavEstudianteId);
    Optional<EfjavClsPenalidad> efjavMtdBuscarPenalidadVigente(Long efjavEstudianteId);
}
