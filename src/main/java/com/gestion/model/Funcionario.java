package com.gestion.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Funcionario {
    private int id;
    private String numeroDocumento;
    private String nombreCompleto;
    private String tipoDocumentoId; 
    private String estadoCivilId;   
    private LocalDate fechaNacimiento;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDate fechaIngreso;
    private String cargo;
    
    private String tipoDocumentoNombre;
    private String estadoCivilNombre;

    private List<FormacionAcademica> formaciones = new ArrayList<>();
    private List<GrupoFamiliar> familiares = new ArrayList<>();

    public Funcionario() {}

    public Funcionario(String numeroDocumento, String nombreCompleto, String tipoDocumentoId) {
        this.numeroDocumento = numeroDocumento;
        this.nombreCompleto = nombreCompleto;
        this.tipoDocumentoId = tipoDocumentoId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getTipoDocumentoId() { return tipoDocumentoId; }
    public void setTipoDocumentoId(String tipoDocumentoId) { this.tipoDocumentoId = tipoDocumentoId; }

    public String getEstadoCivilId() { return estadoCivilId; }
    public void setEstadoCivilId(String estadoCivilId) { this.estadoCivilId = estadoCivilId; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getTipoDocumentoNombre() { return tipoDocumentoNombre; }
    public void setTipoDocumentoNombre(String tipoDocumentoNombre) { this.tipoDocumentoNombre = tipoDocumentoNombre; }

    public String getEstadoCivilNombre() { return estadoCivilNombre; }
    public void setEstadoCivilNombre(String estadoCivilNombre) { this.estadoCivilNombre = estadoCivilNombre; }

    public List<FormacionAcademica> getFormaciones() { return formaciones; }
    public void setFormaciones(List<FormacionAcademica> formaciones) { this.formaciones = formaciones; }

    public List<GrupoFamiliar> getFamiliares() { return familiares; }
    public void setFamiliares(List<GrupoFamiliar> familiares) { this.familiares = familiares; }

    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", documento='" + numeroDocumento + '\'' +
                ", nombre='" + nombreCompleto + '\'' +
                ", tipoDoc='" + tipoDocumentoId + '\'' +
                ", estadoCivil='" + estadoCivilId + '\'' +
                ", cargo='" + cargo + '\'' +
                '}';
    }
}