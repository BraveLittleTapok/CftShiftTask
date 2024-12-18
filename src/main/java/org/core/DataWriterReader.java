package org.core;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.run.Main.*;

public class DataWriterReader {
    public void createFilteredData(List<String> files) throws IOException {
        File tmpFileWithData = createTempFile(files);
        try (BufferedReader reader = new BufferedReader(new FileReader(tmpFileWithData))) {
            String line = reader.readLine();
            while (line != null) {
                if (line.contains(".")) {
                    writeDoubleValues(line);
                } else if (NumberUtils.isDigits(line.replace("-", ""))) {
                    listLong.add(Long.parseLong(line));
                    longCount++;
                } else if (StringUtils.isNotEmpty(line) && line.matches(".*[a-zA-Zа-яА-ЯёЁ].*")) {
                    listString.add(line);
                    stringCount++;
                } else {
                    throw new IOException("Не получилось записать строку: " + line + "\n");
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Проблемы с временным файлом" + e.getMessage());
        }
        tmpFileWithData.deleteOnExit();
    }

    private File createTempFile(List<String> files) throws IOException {
        File dirTmp = new File("src/main/resources");
        if (!dirTmp.exists()) {
            dirTmp.mkdirs();
        }
        File tmpFile = new File(dirTmp + "/targetFile.tmp");
        if (files == null) {
            return writeInnerFiles(tmpFile);
        } else {
            return writeOuterFiles(tmpFile, files);
        }
    }

    public <T> void writeDataInFile(String path, List<T> list) throws Exception {
        if (!list.isEmpty()) {
            FileOutputStream outInt;
            File outputDir = new File(OptionsParser.pathToFile);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            try {
                if (addToExistsFiles) {
                    outInt = new FileOutputStream(path, true);
                } else {
                    outInt = new FileOutputStream(path);
                }
            } catch (IOException ms) {
                String message = "\nПроблемы с созданием директории для файлов результата: " + ms.getMessage()
                        + "\nВыберите другую директорию";
                throw new Exception(message);
            }
            if (!list.isEmpty()) {
                for (T element : list) {
                    outInt.write(element.toString().getBytes());
                    outInt.write("\n".getBytes());
                }
            }
            outInt.close();
        }
    }

    private void copyIntoStream(String file, FileOutputStream outputStream) throws IOException {
        try (InputStream inputStream = new FileInputStream(file);
             OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8")) {
            IOUtils.copy(inputStream, writer);
            writer.write("\n");
        }
    }

    private void writeDoubleValues(String line) {
        String regex = "^[+-]?\\d+(\\.\\d+)?([eE][+-]?\\d+)?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            listDouble.add(Double.parseDouble(line));
            doubleCount++;
        }
    }

    private File writeInnerFiles(File outputFile) {
        try (FileOutputStream outputStream = new FileOutputStream(outputFile);
             InputStream inputStream1 = DataWriterReader.class.getClassLoader()
                     .getResourceAsStream("input_files/in1.txt");
             InputStream inputStream2 = DataWriterReader.class.getClassLoader()
                     .getResourceAsStream("input_files/in2.txt")) {
            IOUtils.copy(inputStream1, outputStream);
            IOUtils.write("\n", outputStream);
            IOUtils.copy(inputStream2, outputStream);
            IOUtils.write("\n", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputFile;
    }

    private File writeOuterFiles(File tmpFile, List<String> filesPath) throws IOException {
        try {
            try (FileOutputStream outputStream = new FileOutputStream(tmpFile, true)) {
                for (String file : filesPath) {
                    copyIntoStream(file, outputStream);
                }
            }
        } catch (IOException ex) {
            String message = "Некорректный файл для чтения: " + ex.getMessage() +
                    "\nПроверьте имя, путь и расширение файла. Корректный формат: txt";
            throw new IOException(message);
        }
        return tmpFile;
    }

}
