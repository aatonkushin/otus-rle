public class RunLengthEncoder {
    public static String encode(String inputString) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < inputString.length(); i++) {
            int count = 1;

            while (i < inputString.length() - 1 && inputString.charAt(i) == inputString.charAt(i + 1)) {
                count++;
                i++;
            }

            sb.append(inputString.charAt(i)).append(count);
        }

        return sb.toString();
    }

    public static String decode(String decodedString) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < decodedString.length(); i++) {
            char c = decodedString.charAt(i);

            StringBuilder numberSb = new StringBuilder();
            while (i + 1 < decodedString.length() && isNumber(String.valueOf(decodedString.charAt(i + 1)))) {
                numberSb.append(decodedString.charAt(i + 1));
                i++;
            }

            sb.append(repeat(c, Integer.parseInt(numberSb.toString())));
        }

        return sb.toString();
    }

    private static boolean isNumber(String str) {
        return str.matches("^\\d*$");
    }

    private static String repeat(char c, int n) {
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            sb.append(c);
        }

        return sb.toString();
    }
}
