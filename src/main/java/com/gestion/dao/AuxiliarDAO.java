package com.gestion.dao;

import com.gestion.exception.DAOException;
import java.util.List;

public interface AuxiliarDAO {
    List<String> listarTiposDocumento() throws DAOException;
    List<String> listarEstadosCiviles() throws DAOException;
}
