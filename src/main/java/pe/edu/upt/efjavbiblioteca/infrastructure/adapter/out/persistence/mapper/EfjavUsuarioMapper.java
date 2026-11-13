package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upt.efjavbiblioteca.domain.model.EfjavClsUsuario;
import pe.edu.upt.efjavbiblioteca.domain.model.EfjavEnuRol;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavRolEntity;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavUsuarioEntity;

import java.util.stream.Collectors;

@Component
public class EfjavUsuarioMapper {

    public EfjavClsUsuario efjavMtdToDomain(EfjavUsuarioEntity entity) {
        if (entity == null) return null;
        EfjavClsUsuario model = new EfjavClsUsuario();
        model.setEfjavId(entity.getEfjavId());
        model.setEfjavCodigo(entity.getEfjavCodigo());
        model.setEfjavNombres(entity.getEfjavNombres());
        model.setEfjavCorreo(entity.getEfjavCorreo());
        model.setEfjavPasswordHash(entity.getEfjavPasswordHash());
        model.setEfjavTotpSecret(entity.getEfjavTotpSecret());
        model.setEfjavTotpActivo(entity.getEfjavTotpActivo());
        model.setEfjavOtpActual(entity.getEfjavOtpActual());
        model.setEfjavOtpExpiracion(entity.getEfjavOtpExpiracion());
        model.setEfjavIntentosFallidos(entity.getEfjavIntentosFallidos());
        model.setEfjavBloqueado(entity.getEfjavBloqueado());
        model.setEfjavEstado(entity.getEfjavEstado());
        model.setEfjavFechaCreacion(entity.getEfjavFechaCreacion());
        if (entity.getEfjavRoles() != null) {
            model.setEfjavRoles(entity.getEfjavRoles().stream()
                    .map(r -> EfjavEnuRol.valueOf(r.getEfjavNombre()))
                    .collect(Collectors.toSet()));
        }
        return model;
    }

    public void efjavMtdUpdateEntity(EfjavClsUsuario model, EfjavUsuarioEntity entity) {
        if (model == null || entity == null) return;
        entity.setEfjavCodigo(model.getEfjavCodigo());
        entity.setEfjavNombres(model.getEfjavNombres());
        entity.setEfjavCorreo(model.getEfjavCorreo());
        entity.setEfjavPasswordHash(model.getEfjavPasswordHash());
        entity.setEfjavTotpSecret(model.getEfjavTotpSecret());
        entity.setEfjavTotpActivo(model.getEfjavTotpActivo());
        entity.setEfjavOtpActual(model.getEfjavOtpActual());
        entity.setEfjavOtpExpiracion(model.getEfjavOtpExpiracion());
        entity.setEfjavIntentosFallidos(model.getEfjavIntentosFallidos());
        entity.setEfjavBloqueado(model.getEfjavBloqueado());
        entity.setEfjavEstado(model.getEfjavEstado());
    }
}
