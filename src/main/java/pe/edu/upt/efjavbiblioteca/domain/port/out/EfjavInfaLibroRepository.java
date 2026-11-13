package pe.edu.upt.efjavbiblioteca.domain.port.out;

import pe.edu.upt.efjavbiblioteca.domain.model.EfjavClsLibro;
import java.util.List;
import java.util.Optional;

public interface EfjavInfaLibroRepository {
    List<EfjavClsLibro> efjavMtdListarCatalogo();
    Optional<EfjavClsLibro> efjavMtdBuscarPorId(Long efjavId);
    Optional<EfjavClsLibro> efjavMtdBuscarPorCodigo(String efjavCodigo);
    EfjavClsLibro efjavMtdGuardar(EfjavClsLibro efjavLibro);
}
