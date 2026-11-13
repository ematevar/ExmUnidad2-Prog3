package pe.edu.upt.efjavbiblioteca.domain.port.out;

import pe.edu.upt.efjavbiblioteca.domain.model.EfjavClsUsuario;
import java.util.Optional;

public interface EfjavInfaUsuarioRepository {
    Optional<EfjavClsUsuario> efjavMtdBuscarPorCodigo(String efjavCodigo);
    Optional<EfjavClsUsuario> efjavMtdBuscarPorCorreo(String efjavCorreo);
    Optional<EfjavClsUsuario> efjavMtdBuscarPorId(Long efjavId);
    EfjavClsUsuario efjavMtdGuardar(EfjavClsUsuario efjavUsuario);
}
