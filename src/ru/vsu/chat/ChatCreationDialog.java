package ru.vsu.chat;

import ru.vsu.utils.Person;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.*;

public class ChatCreationDialog extends JDialog {
    private JPanel contentPane;
    private JButton saveButton;
    private JButton openButton;
    private JProgressBar progressBar;
    private JButton chatReadyButton;
    private JSeparator chatReadySeparator;

    private Person secondPerson;

    public ChatCreationDialog(Person person) {
        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        progressBar.setMaximum(0);
        progressBar.setMaximum(100);

        saveButton.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("public.key"));
            fc.showSaveDialog(contentPane);

            try {
                FileOutputStream fos = new FileOutputStream(fc.getSelectedFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                oos.writeObject(person);

                oos.flush();
                oos.close();

                progressBar.setValue(progressBar.getMaximum() / 2);

                saveButton.setEnabled(false);
                saveButton.setText("Сохранено");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        openButton.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(contentPane);

            try {
                FileInputStream fis = new FileInputStream(fc.getSelectedFile());
                ObjectInputStream ois = new ObjectInputStream(fis);

                Person tmpPerson = (Person) ois.readObject();
                setSecondPerson(tmpPerson);

                progressBar.setValue(progressBar.getMaximum());

                openButton.setEnabled(false);
                openButton.setText("Открыто");
            } catch (IOException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        });

        progressBar.addChangeListener(e -> {
            if (progressBar.getValue() == progressBar.getMaximum()) {
                chatReadySeparator.setVisible(true);
                chatReadyButton.setVisible(true);

                pack();
                getContentPane().validate();
                getContentPane().repaint();
            }
        });

        chatReadyButton.addActionListener(e -> {
            MainChatDialog dialog = new MainChatDialog(person, getSecondPerson());
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);

            this.dispose();
        });
    }

    private void setSecondPerson(Person tmpPerson) {
        secondPerson = tmpPerson;
    }

    public Person getSecondPerson() {
        return secondPerson;
    }
}
