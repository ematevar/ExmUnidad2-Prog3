package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "efjav_libros")
public class EfjavLibroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "efjav_id")
    private Long efjavId;

    @Column(name = "efjav_codigo", nullable = false, unique = true, length = 50)
    private String efjavCodigo;

    @Column(name = "efjav_titulo", nullable = false, length = 255)
    private String efjavTitulo;

    @Column(name = "efjav_autor", nullable = false, length = 150)
    private String efjavAutor;

    @Column(name = "efjav_categoria", nullable = false, length = 100)
    private String efjavCategoria;

    @Column(name = "efjav_ejemplares_totales", nullable = false)
    private Integer efjavEjemplaresTotales;

    @Column(name = "efjav_ejemplares_disponibles", nullable = false)
    private Integer efjavEjemplaresDisponibles;

    @Column(name = "efjav_estado", length = 30)
    private String efjavEstado;

    public EfjavLibroEntity() {}

    // Getters y Setters
    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public String getEfjavCodigo() { return efjavCodigo; }
    public void setEfjavCodigo(String efjavCodigo) { this.efjavCodigo = efjavCodigo; }

    public String getEfjavTitulo() { return efjavTitulo; }
    public void setEfjavTitulo(String efjavTitulo) { this.efjavTitulo = efjavTitulo; }

    public String getEfjavAutor() { return efjavAutor; }
    public void setEfjavAutor(String efjavAutor) { this.efjavAutor = efjavAutor; }

    public String getEfjavCategoria() { return efjavCategoria; }
    public void setEfjavCategoria(String efjavCategoria) { this.efjavCategoria = efjavCategoria; }

    public Integer getEfjavEjemplaresTotales() { return efjavEjemplaresTotales; }
    public void setEfjavEjemplaresTotales(Integer efjavEjemplaresTotales) { this.efjavEjemplaresTotales = efjavEjemplaresTotales; }

    public Integer getEfjavEjemplaresDisponibles() { return efjavEjemplaresDisponibles; }
    public void setEfjavEjemplaresDisponibles(Integer efjavEjemplaresDisponibles) { this.efjavEjemplaresDisponibles = efjavEjemplaresDisponibles; }

    public String getEfjavEstado() { return efjavEstado; }
    public void setEfjavEstado(String efjavEstado) { this.efjavEstado = efjavEstado; }
}
