package ru.vsu;

import ru.vsu.login.LoginDialog;

public class Main {

    public static void main(String[] args) {
        LoginDialog dialog = new LoginDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        //System.exit(0);
    }
}
