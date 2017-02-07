package ru.vsu.login;

import org.apache.commons.codec.digest.DigestUtils;
import ru.vsu.chat.ChatStartDialog;
import ru.vsu.utils.AESHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LoginDialog extends JDialog {
    private JPanel contentPane;
    private JButton loginButton;
    private JButton registrationButton;
    private JLabel recreatePasswordLabel;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JTextField keyField;

    public LoginDialog() {

        recreatePasswordLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        setContentPane(contentPane);
        setModal(true);
//        getRootPane().setDefaultButton(buttonOK);

        loginButton.addActionListener(e -> {
            checkCredentials(loginField.getText(), passwordField.getPassword(), keyField.getText());
        });

        registrationButton.addActionListener(e -> {
            RegistrationDialog dialog = new RegistrationDialog();
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        loginField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                loginField.setBackground(new Color(255, 255, 255));
            }
        });

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setBackground(new Color(255, 255, 255));
            }
        });

        keyField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                keyField.setBackground(new Color(255, 255, 255));
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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

    private void checkCredentials(String loginText, char[] passwordText, String keyText) {

        boolean flag = true;

        if (loginText.equals("")) {
            loginField.setBackground(new Color(255, 0, 0, 50));
            flag = false;
        }

        if (passwordText.length == 0) {
            passwordField.setBackground(new Color(255, 0, 0, 50));
            flag = false;
        }

        if (keyText.equals("")) {
            keyField.setBackground(new Color(255, 0, 0, 50));
            flag = false;
        }

        if (flag) {

            File accountFile = new File(loginText + ".md5");
            if (accountFile.exists()) {

                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(accountFile));
                    Map<String, String> kvPair = new HashMap<>();

                    StringBuilder keyBuilder = new StringBuilder();
                    StringBuilder valueBuilder = new StringBuilder();

                    String[] temp;
                    String line = bufferedReader.readLine();
                    while (line != null) {

                        char[] lineArray = line.toCharArray();
                        int tmpIndex = line.indexOf("=");

                        for (int i = 0; i < tmpIndex; i++) {
                            keyBuilder.append(lineArray[i]);
                        }

                        for (int i = tmpIndex + 1; i < lineArray.length; i++) {
                            valueBuilder.append(lineArray[i]);
                        }

                        kvPair.put(keyBuilder.toString(), valueBuilder.toString());

                        keyBuilder.setLength(0);
                        valueBuilder.setLength(0);
                        line = bufferedReader.readLine();
                    }

                    if (loginText.equals(kvPair.get("LOGIN"))) {

                        byte[] password = new String(passwordField.getPassword()).getBytes("UTF-8");
                        StringBuilder sbPassword = new StringBuilder();
                        for (char c : passwordText) {
                            sbPassword.append(c);
                        }

                        if (sbPassword.toString().equals(
                                new AESHelper(
                                        DigestUtils.md5(keyText)
                                ).decrypt(kvPair.get("PASSWORD"))
                        )) {
                            ChatStartDialog dialog = new ChatStartDialog();
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
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Вы ввели неверный ключ", "Ошибка", JOptionPane.PLAIN_MESSAGE);
                }
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
