package ru.vsu.login;

import org.apache.commons.codec.digest.DigestUtils;
import ru.vsu.utils.AESHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PasswordRecoveryDialog extends JDialog {
    private JPanel contentPane;
    private JTextField keyTextField;
    private JButton recoverPasswordButton;
    private JLabel passwordLabel;
    private JTextField loginTextField;

    public PasswordRecoveryDialog() {
        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        keyTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                keyTextField.setBackground(new Color(255, 255, 255));
            }
        });

        loginTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                loginTextField.setBackground(new Color(255, 255, 255));
            }
        });

        recoverPasswordButton.addActionListener(e -> {

            boolean flag = true;

            if (keyTextField.getText().equals("")) {
                keyTextField.setBackground(new Color(255, 0, 0, 50));
                flag = false;
            }

            if (loginTextField.getText().equals("")) {
                loginTextField.setBackground(new Color(255, 0, 0, 50));
                flag = false;
            }

            if (flag) {

                File accountFile = new File(loginTextField.getText() + ".md5");

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

                        if (DigestUtils.md5Hex(keyTextField.getText()).equals(kvPair.get("KEY"))) {
                            passwordLabel.setText(
                                    new AESHelper(
                                            DigestUtils.md5(keyTextField.getText())).decrypt(kvPair.get("PASSWORD")
                                    )
                            );
                        } else {
                            JOptionPane.showMessageDialog(this, "Вы ввели неверное ключевое слово", "Ошибка", JOptionPane.PLAIN_MESSAGE);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Такого логина не существует", "Ошибка", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
    }
}
