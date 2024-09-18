import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

public class NosjParser {

    public static String parseNosj(String input) {
        if (isMap(input)) return handleMap(input);
        else {
            exitProgram("Invalid Nosj Format.");
            return null;
        }
    }

    public static String handleMap(String input) {
        String trimmedInput = input.substring(2, input.length() - 2);

        if (trimmedInput.isEmpty()) return "begin-map\nend-map";

        if (trimmedInput.endsWith(",")) {
            exitProgram("Invalid Nosj Format: Trailing comma detected.");
            return null;
        }

        Set<String> seenKeys = new HashSet<>();
        StringBuilder output = new StringBuilder("begin-map\n");
        StringBuilder element = new StringBuilder();
        int level = 0;

        for (char c : trimmedInput.toCharArray()) {
            if (c == '(') level++;
            else if (c == ')') level--;

            if (c == ',' && level == 0) {
                String parsedElement = parseElement(element.toString(), seenKeys);
                if (parsedElement != null) {
                    output.append(parsedElement).append("\n");
                }
                element.setLength(0);
            }
            else element.append(c);
        }

        if (!element.toString().isEmpty()) {
            String parsedElement = parseElement(element.toString(), seenKeys);
            if (parsedElement != null) output.append(parsedElement).append("\n");
        }

        output.append("end-map");
        return output.toString();
    }

    public static boolean isMap(String input) {
        return input.startsWith("(<") && input.endsWith(">)");
    }

    public static String parseElement(String input, Set<String> seenKeys) {
        String[] parts = input.split(":", 2);

        if (parts.length < 2) {
            exitProgram("Invalid element format: " + input);
            return null;
        } else if (!parts[0].matches("[a-z]+")) {
            exitProgram("Invalid character in key: " + parts[0]);
            return null;
        }

        String keyName = parts[0];

        if (seenKeys.contains(keyName)) {
            exitProgram("Duplicate key found: " + keyName);
            return null;
        }
        else seenKeys.add(keyName);

        String value = parts[1];

        if (isMap(value)) return keyName + " -- map -- \n" + handleMap(value);

        return keyName + processValue(value);
    }

    public static String processValue(String input) {
        if (isNum(input)) return " -- num -- " + parseNum(input);
        else if (isComplexString(input)) return " -- string -- " + parseComplexString(input);
        else if (isSimpleString(input)) return " -- string -- " + parseSimpleString(input);
        else {
            exitProgram("Invalid Nosj Format for value: " + input);
            return null;
        }
    }

    public static boolean isNum(String input) {
        for (char c : input.toCharArray()) if (c != '0' && c != '1') return false;
        return true;
    }

    public static boolean isComplexString(String input) {
        if (!input.contains("%")) return false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (!isAsciiLetterOrDigit(c) && c != '%') {
                exitProgram("Invalid complex-string value: " + input);
                return false;
            }
        }
        return true;
    }

    public static boolean isSimpleString (String input) {
            if (input.charAt(input.length() - 1) != 's') return false;

            for (int i = 0; i < input.length() - 1; i++) {
                char c = input.charAt(i);
                if (!isAsciiLetterOrDigit(c) && c != ' ' && c != '\t') {
                    exitProgram("Invalid simple-string value: " + c);
                }
            }
            return true;
    }

    private static boolean isAsciiLetterOrDigit(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9');
    }

    public static BigInteger parseNum(String binaryString) {
        if (binaryString.isEmpty()) {
            exitProgram("Empty binary string.");
            return BigInteger.ZERO;
        }

        BigInteger number = new BigInteger(binaryString, 2);
        if (binaryString.charAt(0) == '1') number = number.subtract(BigInteger.valueOf(2).pow(binaryString.length()));
        return number;
    }

    public static String parseSimpleString(String input) {
        return input.substring(0, input.length() - 1);
    }

    public static String parseComplexString(String input) {
        try {
            return URLDecoder.decode(input, "UTF-8");
        } catch (Exception e) {
            exitProgram("Invalid format for complex-string: " + input);
            return null;
        }
    }

    public static String readNosjFile(String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            StringBuilder contentBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null && line.trim().isEmpty()) {}

            if (line != null) contentBuilder.append(line.trim());

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    NosjParser.exitProgram("Invalid new line in nosj file.");
                }
            }

            if (contentBuilder.length() == 0) {
                NosjParser.exitProgram("Empty nosj file.");
                return null;
            }

            return contentBuilder.toString();

        } catch (IOException e) {
            NosjParser.exitProgram("File reading error: " + e.getMessage());
            return null;
        }
    }

    public static void exitProgram(String exitMessage) {
        System.err.println("ERROR -- " + exitMessage + "\n");
        System.exit(66);
    }
}
