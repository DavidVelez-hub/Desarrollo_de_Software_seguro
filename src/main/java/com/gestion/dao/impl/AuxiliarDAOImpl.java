package com.gestion.dao.impl;

import com.gestion.dao.AuxiliarDAO;
import com.gestion.exception.DAOException;
import com.gestion.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuxiliarDAOImpl implements AuxiliarDAO {

    @Override
    public List<String> listarTiposDocumento() throws DAOException {
        return listarNombres("tipo_documento");
    }

    @Override
    public List<String> listarEstadosCiviles() throws DAOException {
        return listarNombres("estado_civil");
    }

    private List<String> listarNombres(String tabla) throws DAOException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT nombre FROM " + tabla + " ORDER BY nombre ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                lista.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar " + tabla + ": " + e.getMessage(), e);
        }
        return lista;
    }
}
