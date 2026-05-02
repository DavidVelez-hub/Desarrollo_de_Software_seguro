package com.gestion.view;

import com.gestion.dao.AuxiliarDAO;
import com.gestion.dao.FuncionarioDAO;
import com.gestion.dao.impl.AuxiliarDAOImpl;
import com.gestion.dao.impl.FuncionarioDAOImpl;
import com.gestion.exception.DAOException;
import com.gestion.model.Funcionario;
import com.gestion.model.FormacionAcademica;
import com.gestion.model.GrupoFamiliar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FormularioFuncionario extends JDialog {
    
    private JTextField txtDocumento, txtNombre, txtTelefono, txtEmail, txtCargo;
    private JTextField txtFechaNac, txtFechaIngreso, txtDireccion;
    private JComboBox<String> cmbTipoDoc, cmbEstadoCivil;
    private JButton btnGuardar, btnCancelar;
    
    // Componentes para formación y familia
    private JTable tblFormacion, tblFamilia;
    private DefaultTableModel modeloFormacion, modeloFamilia;
    private JButton btnAddFormacion, btnDelFormacion, btnAddFamilia, btnDelFamilia;

    private boolean confirmado = false;
    private FuncionarioDAO dao = new FuncionarioDAOImpl();
    private AuxiliarDAO auxDao = new AuxiliarDAOImpl();
    private Funcionario funcionarioActual;

    public FormularioFuncionario(JFrame parent, Funcionario funcionario) {
        super(parent, funcionario == null ? "Nuevo Funcionario" : "Editar Funcionario", true);
        this.funcionarioActual = (funcionario != null) ? funcionario : new Funcionario();
        
        setSize(600, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Datos Básicos", crearPanelDatosBasicos());
        tabs.addTab("Formación y Familia", crearPanelRelacionados());
        
        add(tabs, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar Todo");
        btnCancelar = new JButton("Cancelar");
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        if (funcionario != null) {
            cargarDatosEnCampos();
            actualizarTablas();
        }

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardarDatos());
        
        btnAddFormacion.addActionListener(e -> {
            DialogoFormacion diag = new DialogoFormacion(this);
            diag.setVisible(true);
            if (diag.isConfirmado()) {
                funcionarioActual.getFormaciones().add(diag.getFormacion());
                actualizarTablas();
            }
        });
        
        btnDelFormacion.addActionListener(e -> {
            int fila = tblFormacion.getSelectedRow();
            if (fila != -1) {
                funcionarioActual.getFormaciones().remove(fila);
                actualizarTablas();
            }
        });

        btnAddFamilia.addActionListener(e -> {
            DialogoFamiliar diag = new DialogoFamiliar(this);
            diag.setVisible(true);
            if (diag.isConfirmado()) {
                funcionarioActual.getFamiliares().add(diag.getFamiliar());
                actualizarTablas();
            }
        });
        
        btnDelFamilia.addActionListener(e -> {
            int fila = tblFamilia.getSelectedRow();
            if (fila != -1) {
                funcionarioActual.getFamiliares().remove(fila);
                actualizarTablas();
            }
        });
    }

    private JPanel crearPanelDatosBasicos() {
        JPanel panel = new JPanel(new GridLayout(10, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("Nº Documento:"));
        txtDocumento = new JTextField();
        panel.add(txtDocumento);

        panel.add(new JLabel("Nombre Completo:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        panel.add(new JLabel("Tipo Documento:"));
        cmbTipoDoc = new JComboBox<>();
        cargarOpcionesTipoDoc();
        panel.add(cmbTipoDoc);
        
        panel.add(new JLabel("Estado Civil:"));
        cmbEstadoCivil = new JComboBox<>();
        cargarOpcionesEstadoCivil();
        panel.add(cmbEstadoCivil);

        panel.add(new JLabel("Fecha Nac (yyyy-mm-dd):"));
        txtFechaNac = new JTextField();
        panel.add(txtFechaNac);

        panel.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panel.add(txtTelefono);

        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);

        panel.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panel.add(txtDireccion);

        panel.add(new JLabel("Fecha Ingreso (yyyy-mm-dd):"));
        txtFechaIngreso = new JTextField();
        panel.add(txtFechaIngreso);

        panel.add(new JLabel("Cargo:"));
        txtCargo = new JTextField();
        panel.add(txtCargo);
        
        return panel;
    }

    private JPanel crearPanelRelacionados() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pFormacion = new JPanel(new BorderLayout(5, 5));
        pFormacion.setBorder(BorderFactory.createTitledBorder("Formación Académica"));
        modeloFormacion = new DefaultTableModel(new String[]{"Nivel", "Título", "Institución", "Estado"}, 0);
        tblFormacion = new JTable(modeloFormacion);
        pFormacion.add(new JScrollPane(tblFormacion), BorderLayout.CENTER);
        
        JPanel bFormacion = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAddFormacion = new JButton("Agregar Estudio");
        btnDelFormacion = new JButton("Quitar");
        bFormacion.add(btnAddFormacion);
        bFormacion.add(btnDelFormacion);
        pFormacion.add(bFormacion, BorderLayout.SOUTH);

        JPanel pFamilia = new JPanel(new BorderLayout(5, 5));
        pFamilia.setBorder(BorderFactory.createTitledBorder("Grupo Familiar"));
        modeloFamilia = new DefaultTableModel(new String[]{"Nombre", "Parentesco"}, 0);
        tblFamilia = new JTable(modeloFamilia);
        pFamilia.add(new JScrollPane(tblFamilia), BorderLayout.CENTER);
        
        JPanel bFamilia = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAddFamilia = new JButton("Agregar Familiar");
        btnDelFamilia = new JButton("Quitar");
        bFamilia.add(btnAddFamilia);
        bFamilia.add(btnDelFamilia);
        pFamilia.add(bFamilia, BorderLayout.SOUTH);

        panel.add(pFormacion);
        panel.add(pFamilia);
        return panel;
    }

    private void cargarDatosEnCampos() {
        txtDocumento.setText(funcionarioActual.getNumeroDocumento());
        txtNombre.setText(funcionarioActual.getNombreCompleto());
        cmbTipoDoc.setSelectedItem(funcionarioActual.getTipoDocumentoId());
        cmbEstadoCivil.setSelectedItem(funcionarioActual.getEstadoCivilId());
        txtTelefono.setText(funcionarioActual.getTelefono());
        txtEmail.setText(funcionarioActual.getEmail());
        txtDireccion.setText(funcionarioActual.getDireccion());
        txtCargo.setText(funcionarioActual.getCargo());
        
        if (funcionarioActual.getFechaNacimiento() != null)
            txtFechaNac.setText(funcionarioActual.getFechaNacimiento().toString());
        
        if (funcionarioActual.getFechaIngreso() != null)
            txtFechaIngreso.setText(funcionarioActual.getFechaIngreso().toString());
    }

    private void actualizarTablas() {
        modeloFormacion.setRowCount(0);
        for (FormacionAcademica fa : funcionarioActual.getFormaciones()) {
            modeloFormacion.addRow(new Object[]{fa.getNivelEstudio(), fa.getTituloObtenido(), fa.getInstitucion(), fa.getEstado()});
        }
        
        modeloFamilia.setRowCount(0);
        for (GrupoFamiliar gf : funcionarioActual.getFamiliares()) {
            modeloFamilia.addRow(new Object[]{gf.getNombre(), gf.getParentezco()});
        }
    }

    private void guardarDatos() {
        try {
            if (txtNombre.getText().trim().isEmpty() || txtDocumento.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y Documento son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            funcionarioActual.setNumeroDocumento(txtDocumento.getText());
            funcionarioActual.setNombreCompleto(txtNombre.getText());
            funcionarioActual.setTipoDocumentoId((String) cmbTipoDoc.getSelectedItem());
            funcionarioActual.setEstadoCivilId((String) cmbEstadoCivil.getSelectedItem());
            funcionarioActual.setTelefono(txtTelefono.getText());
            funcionarioActual.setEmail(txtEmail.getText());
            funcionarioActual.setDireccion(txtDireccion.getText());
            funcionarioActual.setCargo(txtCargo.getText());
            
            if (!txtFechaNac.getText().trim().isEmpty())
                funcionarioActual.setFechaNacimiento(LocalDate.parse(txtFechaNac.getText()));
            
            if (!txtFechaIngreso.getText().trim().isEmpty())
                funcionarioActual.setFechaIngreso(LocalDate.parse(txtFechaIngreso.getText()));

            if (funcionarioActual.getId() == 0) {
                dao.crear(funcionarioActual);
                JOptionPane.showMessageDialog(this, "Funcionario creado exitosamente con sus datos relacionados.");
            } else {
                dao.actualizar(funcionarioActual);
                JOptionPane.showMessageDialog(this, "Funcionario actualizado exitosamente.");
            }
            
            confirmado = true;
            dispose();

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
            List<String> tipos = auxDao.listarTiposDocumento();
            for (String t : tipos) cmbTipoDoc.addItem(t);
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tipos de documento");
        }
    }

    private void cargarOpcionesEstadoCivil() {
        try {
            List<String> estados = auxDao.listarEstadosCiviles();
            for (String e : estados) cmbEstadoCivil.addItem(e);
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar estados civiles");
        }
    }
    
    public boolean isConfirmado() { return confirmado; }
}