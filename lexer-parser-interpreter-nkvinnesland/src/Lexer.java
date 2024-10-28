import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * Class to build an array of Tokens from an input file
 * Includes line number tracking for each token.
 * @author wolberd
 * @see Token
 * @see Parser
 */
public class Lexer {

    String buffer;
    int index = 0;
    int lineNumber = 1;  // Track line numbers
    public static final String INTTOKEN = "INT";
    public static final String DIVTOKEN = "DIV";
    public static final String SUBTOKEN = "SUB";
    public static final String MULTTOKEN = "MULT";
    public static final String IDTOKEN = "ID";
    public static final String ASSMTTOKEN = "ASSMT";
    public static final String PLUSTOKEN = "PLUS";
    public static final String EOFTOKEN = "EOF";

    /**
     * Call getInput to get the file data into our buffer
     * @param fileName the file we open
     */
    public Lexer(String fileName) {
        getInput(fileName);
    }

    /**
     * Reads given file into the data member buffer
     * @param fileName name of file to parse
     */
    private void getInput(String fileName) {
        try {
            Path filePath = Paths.get(fileName);
            byte[] allBytes = Files.readAllBytes(filePath);
            buffer = new String(allBytes);
        } catch (IOException e) {
            System.out.println("You did not enter a valid file name in the run arguments.");
            System.out.println("Please enter a string to be parsed:");
            Scanner scanner = new Scanner(System.in);
            buffer = scanner.nextLine();
        }
    }

    /**
     * Return all tokens in the file
     * @return ArrayList of Token
     */
    public ArrayList<Token> getAllTokens() {
        ArrayList<Token> tokens = new ArrayList<>();
        Token toke = getNextToken();
        Token eof = new Token(EOFTOKEN, "-", lineNumber);

        // Loop until no more tokens are available
        while (toke != null) {
            tokens.add(toke);
            toke = getNextToken();  // Get the next token
        }

        // Add the EOF token after processing all valid tokens
        tokens.add(eof);

        return tokens;
    }

    public Token getNextToken() {
        // Skip over any whitespace and track newlines
        while (index < buffer.length() && Character.isWhitespace(buffer.charAt(index))) {
            if (buffer.charAt(index) == '\n') {
                lineNumber++;  // Increment line number on newline
            }
            index++;  // Move index forward to skip whitespace
        }

        // Check if index reached the end of buffer
        if (index >= buffer.length()) {
            return null;  // No more tokens
        }

        // Read the current character
        char c = buffer.charAt(index);

        // Handle letters (Identifiers)
        if (Character.isLetter(c)) {
            return getIdentifier();
        }

        // Handle digits (Integers)
        if (Character.isDigit(c)) {
            return getInteger();
        }

        // Handle assignment token '='
        if (c == '=') {
            index++;
            return new Token(ASSMTTOKEN, "=", lineNumber);
        }

        // Handle plus token '+'
        if (c == '+') {
            index++;
            return new Token(PLUSTOKEN, "+", lineNumber);
        }

        // Handle multiplication token '*'
        if (c == '*') {
            index++;
            return new Token(MULTTOKEN, "*", lineNumber);
        }

        // Handle division token '/'
        if (c == '/') {
            index++;
            return new Token(DIVTOKEN, "/", lineNumber);
        }

        // Handle subtraction token '-'
        if (c == '-') {
            index++;
            return new Token(SUBTOKEN, "-", lineNumber);
        }

        // Handle any unknown character
        index++;
        return new Token(IDTOKEN, "UNKNOWN", lineNumber);
    }

    // Gets identifier value after initial check catches a letter
    private Token getIdentifier() {
        StringBuilder idsb = new StringBuilder();
        while (index < buffer.length() && (Character.isLetter(buffer.charAt(index)) || Character.isDigit(buffer.charAt(index)))) {
            idsb.append(buffer.charAt(index));
            index++;
        }
        return new Token(IDTOKEN, idsb.toString(), lineNumber);
    }

    // Gets integers after initial check catches a digit
    private Token getInteger() {
        StringBuilder intsb = new StringBuilder();
        while (index < buffer.length() && Character.isDigit(buffer.charAt(index))) {
            intsb.append(buffer.charAt(index));
            index++;
        }
        return new Token(INTTOKEN, intsb.toString(), lineNumber);
    }

    @Override
    public String toString() {
        return "Lexer{" +
                "buffer='" + buffer + '\'' +
                ", index=" + index +
                ", lineNumber=" + lineNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lexer lexer = (Lexer) o;
        return index == lexer.index && lineNumber == lexer.lineNumber && Objects.equals(buffer, lexer.buffer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buffer, index, lineNumber);
    }

    /**
     * Before your run this starter code
     * Select Run | Edit Configurations from the main menu.
     * In Program arguments add the name of file you want to test (e.g., test.txt)
     * @param args args[0]
     */
    public static void main(String[] args) {
        String fileName = "";
        if (args.length == 0) {
            System.out.println("You can test a different file by adding it as an argument");
            System.out.println("See comment above main");
            System.out.println("For this run, testWhitespace2.txt used");
            fileName = "testWhitespace2.txt";
        } else {
            fileName = args[0];
        }

        Lexer lexer = new Lexer(fileName);
        // Just print out the text from the file
        System.out.println(lexer.buffer);
        // Call getAllTokens to process and print the tokens
        ArrayList<Token> tokes = lexer.getAllTokens();
        System.out.println(tokes);
    }
}

	