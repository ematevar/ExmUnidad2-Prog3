package pe.edu.upt.efjavbiblioteca.domain.port.out;

import pe.edu.upt.efjavbiblioteca.domain.model.EfjavClsFirma;
import java.util.Optional;

public interface EfjavInfaFirmaRepository {
    EfjavClsFirma efjavMtdGuardar(EfjavClsFirma efjavFirma);
    Optional<EfjavClsFirma> efjavMtdBuscarPorId(Long efjavId);
}
