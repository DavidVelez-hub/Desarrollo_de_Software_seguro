package com.gestion.model;

public class GrupoFamiliar {
    private int id;
    private int funcionarioId;
    private String nombre;
    private String parentezco;

    public GrupoFamiliar() {}

    public GrupoFamiliar(String nombre, String parentezco) {
        this.nombre = nombre;
        this.parentezco = parentezco;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(int funcionarioId) { this.funcionarioId = funcionarioId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getParentezco() { return parentezco; }
    public void setParentezco(String parentezco) { this.parentezco = parentezco; }

    @Override
    public String toString() {
        return nombre + " (" + parentezco + ")";
    }
}
