package org.core;

import org.run.Main;

import java.util.Objects;

import static org.run.Main.prefixForFilesName;

public class OptionsParser {
    public static String pathToFile = "";
    public static StringBuilder outputFileNameInt = new StringBuilder("/integers.txt");
    public static StringBuilder outputFileNameFloat = new StringBuilder("/float.txt");
    public static StringBuilder outputFileNameString = new StringBuilder("/strings.txt");
    public static String pathForIntResultFile;
    public static String pathForFloatResultFile;
    public static String pathForStringResultFile;

    public static void runParser() {
        pathToFile = Objects.requireNonNullElse(Main.pathToFile, "output_files");
        if (prefixForFilesName != null) {
            outputFileNameInt.insert(1, prefixForFilesName);
            outputFileNameFloat.insert(1, prefixForFilesName);
            outputFileNameString.insert(1, prefixForFilesName);
        }
        pathForIntResultFile = pathToFile + outputFileNameInt;
        pathForFloatResultFile = pathToFile + outputFileNameFloat;
        pathForStringResultFile = pathToFile + outputFileNameString;
    }
}
