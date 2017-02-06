package ru.vsu.login;

import ru.vsu.chat.ChatDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {
    private JPanel contentPane;
    private JButton loginButton;
    private JButton registrationButton;
    private JLabel recreatePasswordLabel;
    private JTextField loginField;
    private JPasswordField passwordField;

    public LoginDialog() {

        recreatePasswordLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        setContentPane(contentPane);
        setModal(true);
//        getRootPane().setDefaultButton(buttonOK);

        loginButton.addActionListener(e -> {
            checkCredentials(loginField.getText(), passwordField.getPassword());
        });

        registrationButton.addActionListener(e -> {
            RegistrationDialog dialog = new RegistrationDialog();
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        loginField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loginField.setBackground(new Color(255, 255, 255));
            }
        });

        passwordField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                passwordField.setBackground(new Color(255, 255, 255));
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void checkCredentials(String loginText, char[] passwordText) {

        boolean flag = true;

        if (loginText.equals("")) {
            loginField.setBackground(new Color(255, 0, 0, 50));
            flag = false;
        }

        if (passwordText.length == 0) {
            passwordField.setBackground(new Color(255, 0, 0, 50));
            flag = false;
        }

        if (flag) {
            if (loginText.equals("123")) {

                StringBuilder sb = new StringBuilder();
                for (char c : passwordText) {
                    sb.append(c);
                }

                if (sb.toString().equals("123")) {
                    ChatDialog dialog = new ChatDialog();
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);

                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Вы ввели неверный пароль", "Ошибка", JOptionPane.PLAIN_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Данный логин незарегистрирован", "Ошибка", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
