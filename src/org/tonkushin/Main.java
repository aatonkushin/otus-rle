package org.tonkushin;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

// Пример строки с аргументами
//encode rle /home/anton/test.txt /home/anton/test.rle
//decode rle /home/anton/test.rle /home/anton/test_decoded.txt

//encode huffman /home/anton/test.txt /home/anton/test.huffman
//decode huffman /home/anton/test.huffman /home/anton/test_decoded.txt

public class Main {
    private static String ENCODE = "encode";
    private static String DECODE = "decode";
    private static String RLE = "rle";
    private static String HUFFMAN = "huffman";

    public static void main(String[] args) throws IOException {
        if (args.length < 4) {
            printHelp();
            return;
        }

        String mode = args[0];
        String method = args[1];
        String sourceFilename = args[2];
        String destFileName = args[3];

        // Сжатие RLE
        if (RLE.equals(method)) {
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
        // Сжатие HUFFMAN
        } else if (HUFFMAN.equals(method)) {
            Huffman huffman = new Huffman();

            if (ENCODE.equals(mode)) {
                byte[] compressed = huffman.compress(readFile(sourceFilename));
                writeFile(destFileName, compressed);
            } else if (DECODE.equals(mode)) {
                byte[] decompressed = huffman.decompress(readFile(sourceFilename));
                writeFile(destFileName, decompressed);
            } else {
                printHelp();
            }

        } else {
            printHelp();
        }
    }

    private static void printHelp() {
        System.out.println("Укажите 3 аргумента: ");
        System.out.println("1: Режим сжатия (encode) / распаковки (decode)");
        System.out.println("2: Алгоритм сжатия (rle) / Хаффман (Huffman)");
        System.out.println("3: Путь к исходному файлу");
        System.out.println("4: Путь к результирующему файлу");
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