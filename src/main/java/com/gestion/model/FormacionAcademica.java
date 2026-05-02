package com.gestion.model;

public class FormacionAcademica {
    private int id;
    private int funcionarioId;
    private String nivelEstudio;
    private String tituloObtenido;
    private String institucion;
    private String estado;

    public FormacionAcademica() {}

    public FormacionAcademica(String nivelEstudio, String tituloObtenido, String institucion, String estado) {
        this.nivelEstudio = nivelEstudio;
        this.tituloObtenido = tituloObtenido;
        this.institucion = institucion;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(int funcionarioId) { this.funcionarioId = funcionarioId; }

    public String getNivelEstudio() { return nivelEstudio; }
    public void setNivelEstudio(String nivelEstudio) { this.nivelEstudio = nivelEstudio; }

    public String getTituloObtenido() { return tituloObtenido; }
    public void setTituloObtenido(String tituloObtenido) { this.tituloObtenido = tituloObtenido; }

    public String getInstitucion() { return institucion; }
    public void setInstitucion(String institucion) { this.institucion = institucion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return nivelEstudio + " - " + tituloObtenido + " (" + estado + ")";
    }
}
