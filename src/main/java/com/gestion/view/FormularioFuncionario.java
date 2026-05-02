package com.gestion.view;

import com.gestion.dao.AuxiliarDAO;
import com.gestion.dao.FuncionarioDAO;
import com.gestion.dao.impl.AuxiliarDAOImpl;
import com.gestion.dao.impl.FuncionarioDAOImpl;
import com.gestion.exception.DAOException;
import com.gestion.model.Funcionario;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FormularioFuncionario extends JDialog {
    
    private JTextField txtDocumento, txtNombre, txtTelefono, txtEmail, txtCargo;
    private JTextField txtFechaNac, txtFechaIngreso, txtDireccion;
    private JComboBox<String> cmbTipoDoc, cmbEstadoCivil; // ComboBoxes para las relaciones
    private JButton btnGuardar, btnCancelar;
    
    private boolean confirmado = false;
    private FuncionarioDAO dao = new FuncionarioDAOImpl();
    private AuxiliarDAO auxDao = new AuxiliarDAOImpl();

    // Constructor: Recibe el funcionario si es edición, o null si es nuevo
    public FormularioFuncionario(JFrame parent, Funcionario funcionario) {
        super(parent, funcionario == null ? "Nuevo Funcionario" : "Editar Funcionario", true); // true = Modal
        
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // --- Panel Central con los campos ---
        JPanel panelCampos = new JPanel(new GridLayout(10, 2, 5, 5));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panelCampos.add(new JLabel("Nº Documento:"));
        txtDocumento = new JTextField();
        panelCampos.add(txtDocumento);

        panelCampos.add(new JLabel("Nombre Completo:"));
        txtNombre = new JTextField();
        panelCampos.add(txtNombre);

        panelCampos.add(new JLabel("Tipo Documento:"));
        cmbTipoDoc = new JComboBox<>();
        cargarOpcionesTipoDoc();
        panelCampos.add(cmbTipoDoc);
        
        panelCampos.add(new JLabel("Estado Civil:"));
        cmbEstadoCivil = new JComboBox<>();
        cargarOpcionesEstadoCivil();
        panelCampos.add(cmbEstadoCivil);

        panelCampos.add(new JLabel("Fecha Nac (yyyy-mm-dd):"));
        txtFechaNac = new JTextField();
        panelCampos.add(txtFechaNac);

        panelCampos.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelCampos.add(txtTelefono);

        panelCampos.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelCampos.add(txtEmail);

        panelCampos.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panelCampos.add(txtDireccion);

        panelCampos.add(new JLabel("Fecha Ingreso (yyyy-mm-dd):"));
        txtFechaIngreso = new JTextField();
        panelCampos.add(txtFechaIngreso);

        panelCampos.add(new JLabel("Cargo:"));
        txtCargo = new JTextField();
        panelCampos.add(txtCargo);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        if (funcionario != null) {
            txtDocumento.setText(funcionario.getNumeroDocumento());
            txtNombre.setText(funcionario.getNombreCompleto());
            cmbTipoDoc.setSelectedItem(funcionario.getTipoDocumentoId());
            cmbEstadoCivil.setSelectedItem(funcionario.getEstadoCivilId());
            txtTelefono.setText(funcionario.getTelefono());
            txtEmail.setText(funcionario.getEmail());
            txtDireccion.setText(funcionario.getDireccion());
            txtCargo.setText(funcionario.getCargo());
            
            if (funcionario.getFechaNacimiento() != null)
                txtFechaNac.setText(funcionario.getFechaNacimiento().toString());
            
            if (funcionario.getFechaIngreso() != null)
                txtFechaIngreso.setText(funcionario.getFechaIngreso().toString());
        }

        btnCancelar.addActionListener(e -> dispose());
        
        btnGuardar.addActionListener(e -> guardarDatos(funcionario));
    }

    private void guardarDatos(Funcionario funcionarioExistente) {
        try {
            // 1. Validar campos básicos
            if (txtNombre.getText().trim().isEmpty() || txtDocumento.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y Documento son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Funcionario f = (funcionarioExistente != null) ? funcionarioExistente : new Funcionario();
            
            f.setNumeroDocumento(txtDocumento.getText());
            f.setNombreCompleto(txtNombre.getText());
            f.setTipoDocumentoId((String) cmbTipoDoc.getSelectedItem());
            f.setEstadoCivilId((String) cmbEstadoCivil.getSelectedItem());
            f.setTelefono(txtTelefono.getText());
            f.setEmail(txtEmail.getText());
            f.setDireccion(txtDireccion.getText());
            f.setCargo(txtCargo.getText());
            
            if (!txtFechaNac.getText().trim().isEmpty())
                f.setFechaNacimiento(LocalDate.parse(txtFechaNac.getText()));
            
            if (!txtFechaIngreso.getText().trim().isEmpty())
                f.setFechaIngreso(LocalDate.parse(txtFechaIngreso.getText()));

            if (funcionarioExistente == null) {
                dao.crear(f);
                JOptionPane.showMessageDialog(this, "Creado exitosamente");
            } else {
                dao.actualizar(f);
                JOptionPane.showMessageDialog(this, "Actualizado exitosamente");
            }
            
            confirmado = true;
            dispose(); // Cerrar ventana

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Error en formato de fecha. Usa: yyyy-mm-dd", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this, "Error de Base de Datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarOpcionesTipoDoc() {
        try {
            java.util.List<String> tipos = auxDao.listarTiposDocumento();
            for (String t : tipos) cmbTipoDoc.addItem(t);
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tipos de documento");
        }
    }

    private void cargarOpcionesEstadoCivil() {
        try {
            java.util.List<String> estados = auxDao.listarEstadosCiviles();
            for (String e : estados) cmbEstadoCivil.addItem(e);
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar estados civiles");
        }
    }
    
    public boolean isConfirmado() {
        return confirmado;
    }
}