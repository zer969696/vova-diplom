package ru.vsu;

import ru.vsu.login.LoginDialog;
import ru.vsu.utils.ExecuteShellCommand;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        LoginDialog dialog = new LoginDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

//        Person one = new Person();
//        Person two = new Person();
//
//        one.generateKeys();
//        two.generateKeys();
//
//        one.receivePublicKeyFrom(two);
//        two.receivePublicKeyFrom(one);
//
//        one.generateCommonSecretKey();

        //one.encryptAndDecryptMessage("alloha");

        //System.exit(0);

    }
}
