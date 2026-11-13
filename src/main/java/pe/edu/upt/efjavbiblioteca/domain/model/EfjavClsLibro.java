package pe.edu.upt.efjavbiblioteca.domain.model;

public class EfjavClsLibro {
    private Long efjavId;
    private String efjavCodigo;
    private String efjavTitulo;
    private String efjavAutor;
    private String efjavCategoria;
    private Integer efjavEjemplaresTotales;
    private Integer efjavEjemplaresDisponibles;
    private String efjavEstado;

    public EfjavClsLibro() {
        this.efjavEjemplaresTotales = 1;
        this.efjavEjemplaresDisponibles = 1;
        this.efjavEstado = "DISPONIBLE";
    }

    public boolean efjavFncHayDisponibilidad() {
        return efjavEjemplaresDisponibles != null && efjavEjemplaresDisponibles > 0;
    }

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
