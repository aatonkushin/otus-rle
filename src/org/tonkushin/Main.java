package org.tonkushin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

// Пример строки с аргументами
//encode /home/anton/test.txt /home/anton/test.rle
//decode /home/anton/test.rle /home/anton/test_decoded.txt

public class Main {
    private static String ENCODE = "encode";
    private static String DECODE = "decode";

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            printHelp();
            return;
        }

        String mode = args[0];
        String sourceFilename = args[1];
        String destFileName = args[2];

        if (ENCODE.equals(mode)) {
            byte[] src = readFile(sourceFilename);
            byte[] bytes = RunLengthEncoder.encode(src);
            writeFile(destFileName, bytes);
            System.out.printf("Файл %s (%d байт) успешно сжат в файл %s (%d байт) \n", sourceFilename, src.length, destFileName, bytes.length);
        } else if (DECODE.equals(mode)) {
            byte[] src = readFile(sourceFilename);
            byte[] bytes = RunLengthEncoder.decode(src);
            writeFile(destFileName, bytes);
            System.out.printf("Файл %s (%d байт) успешно распакован в файл %s (%d байт) \n", sourceFilename, src.length, destFileName, bytes.length);
        } else {
            printHelp();
        }
    }

    private static void printHelp() {
        System.out.println("Укажите 3 аргумента: ");
        System.out.println("1: Режим сжатия (encode) / распаковки (decode)");
        System.out.println("2: Путь к исходному файлу");
        System.out.println("3: Путь к результирующему файлу");
    }

    private static byte[] readFile(String filename){
        byte[] bytes = null;
        try (FileInputStream stream = new FileInputStream(filename)) {
            bytes = stream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    private static void writeFile(String filename, byte[] bytes) {
        try (FileOutputStream stream = new FileOutputStream(filename)) {
            stream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}