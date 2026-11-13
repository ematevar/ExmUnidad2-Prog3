package pe.edu.upt.efjavbiblioteca.domain.port.out;

public interface EfjavInfaAuditoriaRepository {
    void efjavMtdRegistrar(String efjavUsuarioCodigo, String efjavOperacion, String efjavEntidad, 
                          String efjavDetalle, String efjavIp, String efjavEquipo, String efjavResultado);
}
