package ru.vsu.login;

import javax.swing.*;

public class RegistrationDialog extends JDialog {
    private JPanel contentPane;
    private JTextField loginField;
    private JTextField keyField;
    private JButton registrationButton;
    private JButton cancelButton;
    private JPasswordField passwordField;
    private JPasswordField passwordRepeatField;
    private JButton buttonOK;

    public RegistrationDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
    }
}
