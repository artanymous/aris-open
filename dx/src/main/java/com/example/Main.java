package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * com
 * ss
 * pipes
 * apk
 * data
 * directory
 * ...
 */
public class Main {
    public static void main(String[] args) {
//        String[] l = new File("/").list();
//        for (String p : l) {
//            System.out.println(p);
//        }

        String PACKAGE = "indi/ss/pipes";
//        File root = new File("/workspace/fcm/aris-open/pipes/build/intermediates/classes/release");
        String dx = "/Users/ss/workspace/fcm/dx/temp/";
        String output = "/Users/ss/workspace/fcm/dx/output/";
        File pipes = new File("/Users/ss/workspace/fcm/aris-open/pipes/build/intermediates/classes/release/" + PACKAGE);

        new File(output).mkdirs();
        String[] list = pipes.list();
        for (String name : list) {
            String from = pipes.getAbsolutePath() + "/" + name;
            File child = new File(from);
            if (child.isDirectory()) {
                try {
                    File fromFolder = new File(from);
                    String[] dexes = fromFolder.list();
                    if (dexes != null) {
                        for (String d : dexes) {
                            String to = dx + name + "/" + PACKAGE + "/" + name;
                            boolean b = new File(to).mkdirs();
                            System.out.println("mkdir: " + b);
                            copy(new File(from + "/" + d), new File(to + "/" + d));

                            String dxPath = "/Users/ss/Library/Android/sdk/build-tools/25.0.3/";
                            executeCommand(dxPath + "dx --dex --output=" + output + name + ".dex " + dx + name);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String executeCommand(String command) {
        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }

    private static void copy(File from, File to) throws IOException {
        if (from.isDirectory()) {
            to.mkdir();
            String[] list = from.list();
            for (String f : list) {
                copy(new File(from.getPath() + "/" + f), new File(to.getPath() + "/" + f));
            }
        } else {
            InputStream in = new FileInputStream(from);
            OutputStream out = new FileOutputStream(to);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file
            out.flush();
            out.close();
        }


    }
}
