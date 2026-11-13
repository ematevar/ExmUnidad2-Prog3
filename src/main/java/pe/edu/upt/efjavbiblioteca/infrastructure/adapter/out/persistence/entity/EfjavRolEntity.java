package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "efjav_roles")
public class EfjavRolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "efjav_id")
    private Long efjavId;

    @Column(name = "efjav_nombre", nullable = false, unique = true, length = 50)
    private String efjavNombre;

    @Column(name = "efjav_descripcion", nullable = false, length = 150)
    private String efjavDescripcion;

    public EfjavRolEntity() {}

    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public String getEfjavNombre() { return efjavNombre; }
    public void setEfjavNombre(String efjavNombre) { this.efjavNombre = efjavNombre; }

    public String getEfjavDescripcion() { return efjavDescripcion; }
    public void setEfjavDescripcion(String efjavDescripcion) { this.efjavDescripcion = efjavDescripcion; }
}
