package com.gestion.dao.impl;

import com.gestion.dao.FuncionarioDAO;
import com.gestion.exception.DAOException;
import com.gestion.model.Funcionario;
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
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
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
        } catch (SQLException e) {
            throw new DAOException("Error al crear funcionario: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Funcionario f) throws DAOException {
        String sql = "UPDATE funcionarios SET numero_documento=?, nombre_completo=?, tipo_documento_id=?, " +
                     "estado_civil_id=?, fecha_nacimiento=?, telefono=?, email=?, direccion=?, fecha_ingreso=?, cargo=? " +
                     "WHERE id=?";
                     
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
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
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar funcionario: " + e.getMessage(), e);
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
}