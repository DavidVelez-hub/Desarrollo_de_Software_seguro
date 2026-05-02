package com.gestion.view;

import com.gestion.dao.FuncionarioDAO;
import com.gestion.dao.impl.FuncionarioDAOImpl;
import com.gestion.exception.DAOException;
import com.gestion.model.Funcionario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private JTable tblFuncionarios;
    private DefaultTableModel modeloTabla;
    private FuncionarioDAO dao;
    
    private JButton btnNuevo, btnEditar, btnEliminar, btnRefrescar, btnVerPorId;

    public VentanaPrincipal() {
        setTitle("Gestión de Funcionarios - CRUD");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        dao = new FuncionarioDAOImpl();
        setLayout(new BorderLayout(10, 10));
        
        String[] columnas = {"ID", "Doc. Número", "Nombre Completo", "Teléfono", "Email", "Cargo"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tblFuncionarios = new JTable(modeloTabla);
        tblFuncionarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        add(new JScrollPane(tblFuncionarios), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnNuevo = new JButton("Nuevo");
        btnVerPorId = new JButton("Ver por ID");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnRefrescar = new JButton("Refrescar");

        panelBotones.add(btnNuevo);
        panelBotones.add(btnVerPorId);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        add(panelBotones, BorderLayout.SOUTH);

        cargarDatosTabla();
        
        btnNuevo.addActionListener(e -> {
            FormularioFuncionario formulario = new FormularioFuncionario(this, null);
            formulario.setVisible(true);
            if (formulario.isConfirmado()) {
                cargarDatosTabla(); 
            }
        });

        btnEditar.addActionListener(e -> {
            int fila = tblFuncionarios.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un registro para editar.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            try {
                Funcionario funcionarioEditar = dao.obtenerPorId(id);
                    
                if (funcionarioEditar != null) {
                    FormularioFuncionario formulario = new FormularioFuncionario(this, funcionarioEditar);
                    formulario.setVisible(true);
                    if (formulario.isConfirmado()) {
                        cargarDatosTabla();
                    }
                }
            } catch (DAOException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar datos para edición: " + ex.getMessage());
            }
        });

        btnEliminar.addActionListener(e -> eliminarFuncionario());
        btnRefrescar.addActionListener(e -> cargarDatosTabla());
        btnVerPorId.addActionListener(e -> verPorId());
    }

    private void verPorId() {
        String input = JOptionPane.showInputDialog(this, "Ingrese el ID del funcionario a buscar:");
        if (input != null && !input.trim().isEmpty()) {
            try {
                int idBuscado = Integer.parseInt(input.trim());
                Funcionario f = dao.obtenerPorId(idBuscado);
                if (f != null) {
                    String mensaje = "Detalles del Funcionario:\n\n" +
                                     "ID: " + f.getId() + "\n" +
                                     "Documento: " + f.getNumeroDocumento() + "\n" +
                                     "Nombre: " + f.getNombreCompleto() + "\n" +
                                     "Tipo Doc: " + f.getTipoDocumentoId() + "\n" +
                                     "Estado Civil: " + f.getEstadoCivilId() + "\n" +
                                     "Fecha Nacimiento: " + (f.getFechaNacimiento() != null ? f.getFechaNacimiento() : "N/A") + "\n" +
                                     "Teléfono: " + f.getTelefono() + "\n" +
                                     "Email: " + f.getEmail() + "\n" +
                                     "Dirección: " + f.getDireccion() + "\n" +
                                     "Fecha Ingreso: " + (f.getFechaIngreso() != null ? f.getFechaIngreso() : "N/A") + "\n" +
                                     "Cargo: " + f.getCargo();
                    JOptionPane.showMessageDialog(this, mensaje, "Funcionario Encontrado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún funcionario con el ID: " + idBuscado, "No encontrado", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (DAOException ex) {
                JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarDatosTabla() {
        try {
            modeloTabla.setRowCount(0);
            List<Funcionario> lista = dao.listarTodos();
            for (Funcionario f : lista) {
                Object[] fila = {
                    f.getId(), f.getNumeroDocumento(), f.getNombreCompleto(), 
                    f.getTelefono(), f.getEmail(), f.getCargo()
                };
                modeloTabla.addRow(fila);
            }
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void eliminarFuncionario() {
        int fila = tblFuncionarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un funcionario para eliminar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 2);

        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar a " + nombre + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dao.eliminar(id);
                cargarDatosTabla();
                JOptionPane.showMessageDialog(this, "Eliminado correctamente.");
            } catch (DAOException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    }
}