package ru.vsu.utils;

import java.io.*;

/**
 * Created by Evgeniy Evzerov on 03.02.17.
 * VIstar
 */
public class ExecuteShellCommand {
    StringBuffer output = new StringBuffer();

    public void executeCommands(String filePathName, String type, ByteArrayOutputStream baos) throws IOException {

        File tempScript = createTempScript(filePathName, type);

        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            //pb.inheritIO();
            Process process = pb.start();
            InputStream is = process.getInputStream();
            StreamGobbler pOut = new StreamGobbler(is, new PrintStream(baos));
            pOut.start();

            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            tempScript.delete();
        }
    }

    public void executeCommand() throws IOException {

        File tempScript = createTempScript();

        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();

            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            tempScript.delete();
        }
    }

    private File createTempScript(String filePathName, String type) throws IOException {
        File tempScript = File.createTempFile("script", null);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
                tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);

        printWriter.println("#!/bin/bash");
        printWriter.println("cd kuznechik");
        printWriter.println("./program -P " + filePathName + " -D kek.txt " + type);

        printWriter.close();

        return tempScript;
    }

    private File createTempScript() throws IOException {
        File tempScript = File.createTempFile("script", null);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
                tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);

        printWriter.println("#!/bin/bash");
        printWriter.println("cd /home/benz/Encryptor-With-Kuznyechik-master/");
        printWriter.println("make all");

        printWriter.close();

        return tempScript;
    }

}
