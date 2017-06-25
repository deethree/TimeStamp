package main;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by Eike on 17.06.2017.
 */
public class Updater {

    private FTP_Handler ftp_handler;
    private Alert_Windows alerts = new Alert_Windows();
    private String newBuild = null;


    public boolean checkForUpdate() throws Exception {
        try {
            ftp_handler = new FTP_Handler("ws.udag.de", "53081.webmaster", "timestamp.APP#2017");
            ftp_handler.downloadFile("/timestamp.tobiasbormann.de/update/build.txt", "ver/newbuild.txt");
            System.out.println("build erfolgreich heruntergeladen");
            boolean doUpdate = checkVersion();
            if(doUpdate) {
                update();
                ftp_handler.disconnect();
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fehler beim updaten");
            return false;
        }
    }

    private boolean checkVersion() {
        if(File_Handler.fileExist("ver/newbuild.txt")) {
            try (BufferedReader reader = new BufferedReader((new InputStreamReader( new FileInputStream("ver/newbuild.txt"), "UTF-8")))) {
                //Wenn durch Windows TextoCOdierung der UTF-8 Stream nicht mit readable Code anfängt, dann lösche es raus
                reader.mark(1);
                if (reader.read() != 0xFEFF)
                    reader.reset();
                newBuild = reader.readLine();
                System.out.println("old build: " + Updater_Main.build);
                //float newBuildFloat = Float.parseFloat(newBuild);
                //if(!newBuild.equals(null) && !newBuild.equals(Updater_Main.build)) {
                if(Float.parseFloat(newBuild) > Float.parseFloat(Updater_Main.build) && !newBuild.equals(null)) {
                    if(alerts.confirmDialog("Update", "Es ist ein Update verfügbar", "Möchtest du TimeStamp aktualisieren?")) {
                        System.out.println("True");
                        return true;
                    } else {
                        System.out.println("False");
                        return false;
                    }
                }
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("Update konnte nicht ausgeführt werden, Server nicht erreichbar");
            }
            return false;
        }
        System.out.println("build Datei nicht vorhanden");
        return false;
    }

    private void update() throws IOException {
        System.out.println("newbuild: " + newBuild);
        ftp_handler.downloadFile("/timestamp.tobiasbormann.de/update/TimeStamp_" + newBuild + ".jar", "TimeStamp.jar");
        System.out.println("heruntergeladen");
    }

    public void showChangeLog() throws IOException {
        ArrayList<String> log = File_Handler.fileLoader("ver/newbuild.txt");
        System.out.println("Changelog");

        String content = "";
        for(String entry : log) {
            content = content + entry + "\n";
        }

        JTextArea textArea = new JTextArea(content);
        textArea.setColumns(30);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setSize(textArea.getPreferredSize().width, 1);
        JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Update verfügbar",
                JOptionPane.WARNING_MESSAGE);

        renameBuildFile();
    }

    public void renameBuildFile() throws IOException {
        if(File_Handler.fileExist("ver/newbuild.txt")) {
            File_Handler.deleteFile("ver/newbuild.txt");
            System.out.println("build.txt gelöscht");
        }
        /*
        if(File_Handler.fileExist("ver/newbuild.txt")) {
            File oldName = new File("ver/newbuild.txt");
            File newName = new File("ver/build.txt");
            if (!oldName.renameTo(newName)) {
                System.out.println("Fehler beim umbennen");
            }
        } else System.out.println("newbuild exisitert nicht");

        Runtime runTime = Runtime.getRuntime();
        runTime.exec("java -jar TimeStamp.jar");
        System.out.println("neues Programm gestartet");
        */
    }

}
