package com.gestion.view;

import com.gestion.model.GrupoFamiliar;
import javax.swing.*;
import java.awt.*;

public class DialogoFamiliar extends JDialog {
    private JTextField txtNombre, txtParentezco;
    private JButton btnAceptar, btnCancelar;
    private GrupoFamiliar familiar;
    private boolean confirmado = false;

    public DialogoFamiliar(Window parent) {
        super(parent, "Agregar Familiar", ModalityType.APPLICATION_MODAL);
        setSize(300, 180);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 5, 5));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelCampos.add(new JLabel("Nombre Completo:"));
        txtNombre = new JTextField();
        panelCampos.add(txtNombre);

        panelCampos.add(new JLabel("Parentesco:"));
        txtParentezco = new JTextField();
        panelCampos.add(txtParentezco);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> {
            if (validar()) {
                familiar = new GrupoFamiliar();
                familiar.setNombre(txtNombre.getText().trim());
                familiar.setParentezco(txtParentezco.getText().trim());
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
        if (txtNombre.getText().trim().isEmpty() || txtParentezco.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isConfirmado() { return confirmado; }
    public GrupoFamiliar getFamiliar() { return familiar; }
}
