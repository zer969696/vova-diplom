package ru.vsu.chat;

import ru.vsu.utils.Person;

import javax.swing.*;
import java.io.*;
import java.util.Locale;

public class MainChatDialog extends JDialog {
    private JPanel contentPane;
    private JButton saveButton;
    private JButton openButton;
    private JFileChooser fileChooser;

    public MainChatDialog(Person person) {
        setContentPane(contentPane);
        setModal(true);

        saveButton.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("test.txt"));
            fc.showSaveDialog(contentPane);

            try {
                FileOutputStream fos = new FileOutputStream("public.key");
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                oos.writeObject(person);

                oos.flush();
                oos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
