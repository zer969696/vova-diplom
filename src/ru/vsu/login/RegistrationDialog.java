package ru.vsu.login;

import org.apache.commons.codec.digest.DigestUtils;
import ru.vsu.utils.AESHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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

        passwordRepeatField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordRepeatField.setBackground(new Color(255, 255, 255));
            }
        });

        registrationButton.addActionListener(e -> {

            boolean flag = true;

            if (loginField.getText().equals("")) {
                loginField.setBackground(new Color(255, 0, 0, 50));
                flag = false;
            }

            if (keyField.getText().equals("")) {
                keyField.setBackground(new Color(255, 0, 0, 50));
                flag = false;
            }

            if (passwordField.getPassword().length == 0) {
                passwordField.setBackground(new Color(255, 0, 0, 50));
                flag = false;
            }

            if (passwordRepeatField.getPassword().length == 0) {
                passwordRepeatField.setBackground(new Color(255, 0, 0, 50));
                flag = false;
            }

            if (flag) {
                if (!Arrays.toString(passwordField.getPassword())
                        .equals(Arrays.toString(passwordRepeatField.getPassword()))) {
                    JOptionPane.showMessageDialog(this, "Пароли не совпадают", "Ошибка", JOptionPane.PLAIN_MESSAGE);
                } else {
                    File account = new File(loginField.getText() + ".md5");

                    try {
                        PrintWriter out = new PrintWriter(account.getAbsoluteFile());

                        //byte[] password = new String(passwordField.getPassword()).getBytes("UTF-8");
                        StringBuilder sbPassword = new StringBuilder();
                        for (char c : passwordField.getPassword()) {
                            sbPassword.append(c);
                        }
                        byte[] key = keyField.getText().getBytes("UTF-8");

                        StringBuilder sb = new StringBuilder();

                        sb.append("LOGIN=");
                        sb.append(loginField.getText());
                        sb.append("\n");
                        sb.append("PASSWORD=");
                        sb.append(
                                new AESHelper(
                                        DigestUtils.md5(key)
                                ).encrypt(sbPassword.toString())
                        );
                        out.write(sb.toString());

                        out.flush();
                        out.close();

                        JOptionPane.showMessageDialog(this, "Вы успешно зарегистрировались", "Успех", JOptionPane.PLAIN_MESSAGE);
                        this.dispose();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        cancelButton.addActionListener(e -> {
            this.dispose();
        });
    }
}
