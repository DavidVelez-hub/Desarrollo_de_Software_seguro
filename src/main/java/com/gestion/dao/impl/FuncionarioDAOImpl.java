package com.gestion.dao.impl;

import com.gestion.dao.FuncionarioDAO;
import com.gestion.exception.DAOException;
import com.gestion.model.FormacionAcademica;
import com.gestion.model.Funcionario;
import com.gestion.model.GrupoFamiliar;
import com.gestion.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAOImpl implements FuncionarioDAO {

    @Override
    public List<Funcionario> listarTodos() throws DAOException {
        List<Funcionario> lista = new ArrayList<>();
        
        String sql = "SELECT f.*, t.nombre as tipo_doc, e.nombre as estado_civil " +
                     "FROM funcionarios f " +
                     "LEFT JOIN tipo_documento t ON f.tipo_documento_id = t.nombre " +
                     "LEFT JOIN estado_civil e ON f.estado_civil_id = e.nombre " +
                     "ORDER BY f.id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearFuncionario(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar funcionarios: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public Funcionario obtenerPorId(int id) throws DAOException {
        Funcionario f = null;
        String sql = "SELECT f.*, t.nombre as tipo_doc, e.nombre as estado_civil " +
                     "FROM funcionarios f " +
                     "LEFT JOIN tipo_documento t ON f.tipo_documento_id = t.nombre " +
                     "LEFT JOIN estado_civil e ON f.estado_civil_id = e.nombre " +
                     "WHERE f.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    f = mapearFuncionario(rs);
                    cargarDatosRelacionados(f, conn);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al obtener funcionario por ID: " + e.getMessage(), e);
        }
        return f;
    }

    @Override
    public void crear(Funcionario f) throws DAOException {
        String sql = "INSERT INTO funcionarios (numero_documento, nombre_completo, tipo_documento_id, " +
                     "estado_civil_id, fecha_nacimiento, telefono, email, direccion, fecha_ingreso, cargo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, f.getNumeroDocumento());
                pstmt.setString(2, f.getNombreCompleto());
                pstmt.setString(3, f.getTipoDocumentoId());
                pstmt.setString(4, f.getEstadoCivilId());
                pstmt.setDate(5, Date.valueOf(f.getFechaNacimiento()));
                pstmt.setString(6, f.getTelefono());
                pstmt.setString(7, f.getEmail());
                pstmt.setString(8, f.getDireccion());
                pstmt.setDate(9, Date.valueOf(f.getFechaIngreso()));
                pstmt.setString(10, f.getCargo());
                
                pstmt.executeUpdate();
                
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        f.setId(generatedKeys.getInt(1));
                    }
                }
            }
            
            guardarDatosRelacionados(f, conn);
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            }
            throw new DAOException("Error al crear funcionario: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { /* ignore */ }
            }
        }
    }

    @Override
    public void actualizar(Funcionario f) throws DAOException {
        String sql = "UPDATE funcionarios SET numero_documento=?, nombre_completo=?, tipo_documento_id=?, " +
                     "estado_civil_id=?, fecha_nacimiento=?, telefono=?, email=?, direccion=?, fecha_ingreso=?, cargo=? " +
                     "WHERE id=?";
                     
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, f.getNumeroDocumento());
                pstmt.setString(2, f.getNombreCompleto());
                pstmt.setString(3, f.getTipoDocumentoId());
                pstmt.setString(4, f.getEstadoCivilId());
                pstmt.setDate(5, Date.valueOf(f.getFechaNacimiento()));
                pstmt.setString(6, f.getTelefono());
                pstmt.setString(7, f.getEmail());
                pstmt.setString(8, f.getDireccion());
                pstmt.setDate(9, Date.valueOf(f.getFechaIngreso()));
                pstmt.setString(10, f.getCargo());
                pstmt.setInt(11, f.getId());
                
                pstmt.executeUpdate();
            }
            
            guardarDatosRelacionados(f, conn);
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            }
            throw new DAOException("Error al actualizar funcionario: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { /* ignore */ }
            }
        }
    }

    @Override
    public void eliminar(int id) throws DAOException {
        String sql = "DELETE FROM funcionarios WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new DAOException("No se encontró el funcionario con ID: " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar funcionario: " + e.getMessage(), e);
        }
    }

    private Funcionario mapearFuncionario(ResultSet rs) throws SQLException {
        Funcionario f = new Funcionario();
        f.setId(rs.getInt("id"));
        f.setNumeroDocumento(rs.getString("numero_documento"));
        f.setNombreCompleto(rs.getString("nombre_completo"));
        f.setTipoDocumentoId(rs.getString("tipo_documento_id"));
        f.setEstadoCivilId(rs.getString("estado_civil_id"));
        
        Date fechaNac = rs.getDate("fecha_nacimiento");
        if (fechaNac != null) {
            f.setFechaNacimiento(fechaNac.toLocalDate());
        }
        
        f.setTelefono(rs.getString("telefono"));
        f.setEmail(rs.getString("email"));
        f.setDireccion(rs.getString("direccion"));
        
        Date fechaIng = rs.getDate("fecha_ingreso");
        if (fechaIng != null) {
            f.setFechaIngreso(fechaIng.toLocalDate());
        }
        
        f.setCargo(rs.getString("cargo"));
        
        try {
            f.setTipoDocumentoNombre(rs.getString("tipo_doc"));
            f.setEstadoCivilNombre(rs.getString("estado_civil"));
        } catch (SQLException ignore) {
        }
        
        return f;
    }

    private void cargarDatosRelacionados(Funcionario f, Connection conn) throws SQLException {
       
        String sqlFormacion = "SELECT * FROM formacion_academica WHERE funcionario_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlFormacion)) {
            pstmt.setInt(1, f.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    FormacionAcademica fa = new FormacionAcademica();
                    fa.setId(rs.getInt("id"));
                    fa.setFuncionarioId(rs.getInt("funcionario_id"));
                    fa.setNivelEstudio(rs.getString("nivel_estudio"));
                    fa.setTituloObtenido(rs.getString("titulo_obtenido"));
                    fa.setInstitucion(rs.getString("institucion"));
                    fa.setEstado(rs.getString("estado"));
                    f.getFormaciones().add(fa);
                }
            }
        }

        String sqlFamiliar = "SELECT * FROM grupo_familiar WHERE funcionario_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlFamiliar)) {
            pstmt.setInt(1, f.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    GrupoFamiliar gf = new GrupoFamiliar();
                    gf.setId(rs.getInt("id"));
                    gf.setFuncionarioId(rs.getInt("funcionario_id"));
                    gf.setNombre(rs.getString("nombre"));
                    gf.setParentezco(rs.getString("parentezco"));
                    f.getFamiliares().add(gf);
                }
            }
        }
    }

    private void guardarDatosRelacionados(Funcionario f, Connection conn) throws SQLException {
        
        String deleteFormacion = "DELETE FROM formacion_academica WHERE funcionario_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteFormacion)) {
            pstmt.setInt(1, f.getId());
            pstmt.executeUpdate();
        }

        String deleteFamiliar = "DELETE FROM grupo_familiar WHERE funcionario_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteFamiliar)) {
            pstmt.setInt(1, f.getId());
            pstmt.executeUpdate();
        }

        String insertFormacion = "INSERT INTO formacion_academica (funcionario_id, nivel_estudio, titulo_obtenido, institucion, estado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertFormacion)) {
            for (FormacionAcademica fa : f.getFormaciones()) {
                pstmt.setInt(1, f.getId());
                pstmt.setString(2, fa.getNivelEstudio());
                pstmt.setString(3, fa.getTituloObtenido());
                pstmt.setString(4, fa.getInstitucion());
                pstmt.setString(5, fa.getEstado());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }

        String insertFamiliar = "INSERT INTO grupo_familiar (funcionario_id, nombre, parentezco) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertFamiliar)) {
            for (GrupoFamiliar gf : f.getFamiliares()) {
                pstmt.setInt(1, f.getId());
                pstmt.setString(2, gf.getNombre());
                pstmt.setString(3, gf.getParentezco());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }
}