package org.run;

import org.core.DataWriterReader;
import picocli.CommandLine;

import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;

import static java.lang.System.exit;
import static org.core.OptionsParser.*;
import static org.utils.Info.displayInfo;
import static picocli.CommandLine.*;

@Command(name = "Main")
public class Main implements Runnable {
    @Option(names = "-p")
    public static String prefixForFilesName;
    @Option(names = "-o")
    public static String pathToFile;
    @Option(names = "-a", defaultValue = "false")
    public static boolean addToExistsFiles;
    @Option(names = "-s", defaultValue = "false")
    public static boolean shortInfo;
    @Option(names = "-f", defaultValue = "false")
    public static boolean fullInfo;
    @Parameters(description = "Input files")
    public static List<String> inputFiles;
    public static List<String> listString = new ArrayList<>();
    public static List<Long> listLong = new ArrayList<>();
    public static List<Double> listDouble = new ArrayList<>();

    public static int longCount = 0;
    public static int doubleCount = 0;
    public static int stringCount = 0;
    public static String shortestString;
    public static String longestString;
    public static Long minLong;
    public static Long maxLong;
    public static OptionalDouble averageLong;
    public static Double minDouble;
    public static Double maxDouble;
    public static OptionalDouble averageDouble;

    public static void main(String[] args) {
        try {
            startMain(args);
        } catch (UnmatchedArgumentException | MissingParameterException ex) {
            System.out.println(ex.getMessage());
            displayInfo();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            exit(1);
        }
    }

    private static void startMain(String[] args) throws Exception {
        DataWriterReader dataRdWr = new DataWriterReader();
        try {
            new CommandLine(new Main()).parseArgs(args);
            new CommandLine(new Main()).execute(args);
        } catch (UnmatchedArgumentException ms) {
            throw new UnmatchedArgumentException(new CommandLine(new Main()),
                    "\nНеизвестные команды: " + ms.getUnmatched());
        } catch (MissingParameterException ms) {
            throw new MissingParameterException(new CommandLine(new Main()), ms.getMissing(),
                    "\nНеправильный параметр для опции: " + ms.getMessage());
        }
        dataRdWr.createFilteredData(inputFiles);
        dataRdWr.writeDataInFile(pathForIntResultFile, listLong);
        dataRdWr.writeDataInFile(pathForFloatResultFile, listDouble);
        dataRdWr.writeDataInFile(pathForStringResultFile, listString);
        if (shortInfo) {
            printShortInfo();
        }
        if (fullInfo) {
            printFullInfo();
        }
    }

    @Override
    public void run() {
        runParser();
    }

    private static void printFullInfo() {
        printShortInfo();
        if (!listString.isEmpty()) {
            longestString = listString.stream().max(Comparator.comparingInt(String::length)).get();
            shortestString = listString.stream().min(Comparator.comparingInt(String::length)).get();
            System.out.println("Самая короткая строка: " + shortestString);
            System.out.println("Самая длинная строка: " + longestString);
        }
        if (!listLong.isEmpty()) {
            minLong = Collections.min(listLong);
            averageLong = LongStream.of(listLong.stream().mapToLong(l -> l).toArray()).average();
            maxLong = Collections.max(listLong);
            System.out.println("Минимальное целое число: " + minLong);
            System.out.println("Среднее целых чисел: " + averageLong.getAsDouble());
            System.out.println("Максимальное целое число: " + maxLong);
        }
        if (!listDouble.isEmpty()) {
            minDouble = Collections.min(listDouble);
            maxDouble = Collections.max(listDouble);
            averageDouble = DoubleStream.of(listDouble.stream().mapToDouble(l -> l).toArray()).average();
            System.out.println("Минимальное вещественное число: " + minDouble);
            System.out.println("Среднее вещественное число: " + averageDouble.getAsDouble());
            System.out.println("Максимальное вещественное число: " + maxDouble);
        }
    }

    private static void printShortInfo() {
        System.out.println("Количество записанных строк: " + stringCount);
        System.out.println("Количество записанных целых чиспел: " + longCount);
        System.out.println("Количество записанных вещественных чисел: " + doubleCount);
    }
}