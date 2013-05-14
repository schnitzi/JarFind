/*
 * Copyright (c) 2013 cXense ASA.  All rights reserved.
 */
package org.computronium;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Searches jar files.
 */
public class JarFind {

    public static void main(String[] args) throws IOException {

        boolean badUsage = false;
        String[] dirs = new String[]{ "." };

        if (args.length != 1 && args.length != 3) {
            badUsage = true;
        } else if (args.length == 3) {
            if (!args[1].equals("-cp") && !args[1].equals("-classpath")) {
                badUsage = true;
            } else {
                dirs = args[2].split(":");
            }
        }

        if (badUsage) {
            System.err.println("Usage: java JarFind <target> [-cp dir1:dir2:...]");
            System.exit(-1);
        }

        JarFind jarFind = new JarFind();
        jarFind.search(args[0], dirs);
    }

    private void search(final String target, final String[] dirs) throws IOException {
        for (String dirName : dirs) {
            File dir = new File(dirName);
            if (dir.exists()) {
                search(target, dir);
            }
        }
    }

    private void search(final String target, File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    search(target, file);
                } else if (file.getName().endsWith(".jar")) {
                    boolean fileNamePrinted = false;
                    ZipFile zipFile = new ZipFile(file);
                    Enumeration<? extends ZipEntry> entries = zipFile.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        if (entry.getName().contains(target)) {
                            if (!fileNamePrinted) {
                                System.out.println(file.getAbsoluteFile() + ":");
                                fileNamePrinted = true;
                            }
                            System.out.println("    " + entry.getName());
                        }
                    }
                }
            }
        }
    }
}
