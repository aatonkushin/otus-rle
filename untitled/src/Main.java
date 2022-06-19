import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//encode /home/anton/test.txt /home/anton/test.rle
//decode /home/anton/test.rle /home/anton/test_decoded.txt

public class Main {
    private static String ENCODE = "encode";
    private static String DECODE = "decode";

    public static void main(String[] args) throws IOException {
        simpleTest();

        if (args.length < 3) {
            printHelp();
            return;
        }

        Path readPath = Paths.get(args[1]);
        Path writePath = Paths.get(args[2]);

        if (args[0].equals(ENCODE)) {
            String inputString = Files.readAllLines(readPath).get(0);
            String encodedString = RunLengthEncoder.encode(inputString);
            Files.writeString(writePath, encodedString);

            System.out.printf("Файл %s успешно сжат в файл %s \n", args[1], args[2]);
        } else if (args[0].equals(DECODE)) {
            String encodedString = Files.readAllLines(readPath).get(0);
            String decodedString = RunLengthEncoder.decode(encodedString);
            Files.writeString(writePath, decodedString);

            System.out.printf("Файл %s успешно распакован в файл %s \n", args[1], args[2]);
        } else {
            printHelp();
        }
    }

    private static void printHelp() {
        System.out.println("Укажите 3 аргумента: ");
        System.out.println("1: Режим сжатия (encode) / распаковки (decode)");
        System.out.println("2: Путь к исходному файлу");
        System.out.println("3: Путь к результирующему файлу");
        System.out.println("Текст не должен содержать цифры и символов переноса строки");
    }

    private static void simpleTest() {
        String inputString = "abbcccddddeeeeeffffffggggggghhhhhhhhjjjjjjjjjkkkkkkkkkklllllllllll";
        String encodedString = RunLengthEncoder.encode(inputString);
        String decodedString = RunLengthEncoder.decode(encodedString);
        System.out.printf("Исходная строка: %s (%d)\n", inputString, inputString.length());
        System.out.printf("Строка после сжатия: %s (%d)\n", encodedString, encodedString.length());
        System.out.printf("Строка после распаковки: %s (%d)\n", decodedString, decodedString.length());
        System.out.println(inputString.equals(decodedString) ? "Строки равны" : "Строки НЕ равны");
    }
}