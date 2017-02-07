package ru.vsu.chat;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import ru.vsu.utils.ExecuteShellCommand;
import ru.vsu.utils.Person;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class MainChatDialog extends JDialog {
    private JPanel contentPane;
    private JTextArea textAreaEncrypted;
    private JButton encryptButton;
    private JButton decryptButton;
    private JTextArea textAreaDecrypted;
    private JLabel resultLabel;
    private JButton buttonOK;

    private final String TYPE_ENCRYPT = "encrypt";
    private final String TYPE_DECRYPT = "decrypt";

    public MainChatDialog(Person me, Person another) {
        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        ExecuteShellCommand com = new ExecuteShellCommand();
        try {
            com.executeCommand();
        } catch (IOException e) {
            e.printStackTrace();
        }

        encryptButton.addActionListener(e -> {
            String text = textAreaEncrypted.getText();

            textAreaDecrypted.setText("");
            doCipher(me, another, text, TYPE_ENCRYPT);
        });

        decryptButton.addActionListener(e -> {
            String text = textAreaEncrypted.getText();

            textAreaDecrypted.setText("");
            doCipher(me, another, text, TYPE_DECRYPT);
        });

        textAreaEncrypted.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                resultLabel.setText("Результат:");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                resultLabel.setText("Результат:");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                resultLabel.setText("Результат:");
            }
        });
    }

    void doCipher(Person me, Person another, String text, String type) {
        me.receivePublicKeyFrom(another);
        me.generateCommonSecretKey();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (type.equals("encrypt")) {
                    me.encryptAndDecryptMessage(text, baos, "-e");
                } else if (type.equals("decrypt")) {
                    me.encryptAndDecryptMessage(text, baos, "-d");
                }

                String content = new String(baos.toByteArray(), StandardCharsets.UTF_8);
                content = content.split("\n")[0];

                if (!content.equals("")) {
                    if (type.equals("encrypt")) {
                        textAreaDecrypted.setText(content);
                        resultLabel.setText("Результат: ТЕКСТ ЗАШИФРОВАН");
                    } else if (type.equals("decrypt")) {
                        byte[] bytes = new byte[0];
                        try {
                            bytes = Hex.decodeHex(content.toCharArray());
                        } catch (DecoderException e) {
                            e.printStackTrace();
                        }
                        try {
                            textAreaDecrypted.setText(new String(bytes, "UTF-8"));
                            resultLabel.setText("Результат: ТЕКСТ РАСШИФРОВАН");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

}
