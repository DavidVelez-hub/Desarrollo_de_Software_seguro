package com.gestion.view;

import com.gestion.model.FormacionAcademica;
import javax.swing.*;
import java.awt.*;

public class DialogoFormacion extends JDialog {
    private JTextField txtNivel, txtTitulo, txtInstitucion;
    private JComboBox<String> cmbEstado;
    private JButton btnAceptar, btnCancelar;
    private FormacionAcademica formacion;
    private boolean confirmado = false;

    public DialogoFormacion(Window parent) {
        super(parent, "Agregar Formación Académica", ModalityType.APPLICATION_MODAL);
        setSize(350, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 5, 5));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelCampos.add(new JLabel("Nivel de Estudio:"));
        txtNivel = new JTextField();
        panelCampos.add(txtNivel);

        panelCampos.add(new JLabel("Título Obtenido:"));
        txtTitulo = new JTextField();
        panelCampos.add(txtTitulo);

        panelCampos.add(new JLabel("Institución:"));
        txtInstitucion = new JTextField();
        panelCampos.add(txtInstitucion);

        panelCampos.add(new JLabel("Estado:"));
        cmbEstado = new JComboBox<>(new String[]{"Graduado", "En curso", "Aplazado", "Incompleto"});
        panelCampos.add(cmbEstado);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> {
            if (validar()) {
                formacion = new FormacionAcademica();
                formacion.setNivelEstudio(txtNivel.getText().trim());
                formacion.setTituloObtenido(txtTitulo.getText().trim());
                formacion.setInstitucion(txtInstitucion.getText().trim());
                formacion.setEstado((String) cmbEstado.getSelectedItem());
                confirmado = true;
                dispose();
            }
        });

        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private boolean validar() {
        if (txtNivel.getText().trim().isEmpty() || txtTitulo.getText().trim().isEmpty() || txtInstitucion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isConfirmado() { return confirmado; }
    public FormacionAcademica getFormacion() { return formacion; }
}
