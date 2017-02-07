package ru.vsu.chat;

import ru.vsu.utils.NumberGenerator;
import ru.vsu.utils.Person;

import javax.swing.*;

public class ChatStartDialog extends JDialog {
    private JPanel contentPane;
    private JButton startChatButton;
    private JButton connectToChatButton;

    public ChatStartDialog() {
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        startChatButton.addActionListener(e -> {
            Person currentPerson = new Person();
            currentPerson.generateKeys();

            ChatCreationDialog dialog = new ChatCreationDialog(currentPerson);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        connectToChatButton.addActionListener(e -> {
            Person currentPerson = new Person();
            currentPerson.generateKeys();

            ChatCreationDialog dialog = new ChatCreationDialog(currentPerson);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
    }
}
