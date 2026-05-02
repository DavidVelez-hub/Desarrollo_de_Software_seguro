package com.gestion.dao;

import com.gestion.exception.DAOException;
import com.gestion.model.Funcionario;
import java.util.List;

public interface FuncionarioDAO {
    
    List<Funcionario> listarTodos() throws DAOException;
    Funcionario obtenerPorId(int id) throws DAOException;
    void crear(Funcionario funcionario) throws DAOException;
    void actualizar(Funcionario funcionario) throws DAOException;
    void eliminar(int id) throws DAOException;
}