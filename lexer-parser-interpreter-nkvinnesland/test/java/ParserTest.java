import java.util.ArrayList;

/**
 * Create a for loop to test Parser result
 * Find expected result here:  https://docs.google.com/document/d/1Nfx15sYNPsPS3PS-qK8fjQ8VvEozeijMfCECXlYZx2o/edit#heading=h.d51f4t5t6g61
 */
public class ParserTest {

    public static void main(String[] args) {
        // Array of test files to be processed
        String[] fileArray = {
                "test.txt", "testExpectingId2.txt", "testExpectingAssignOp.txt",
                "testExpectingIdOrInt2.txt", "testMultiplePlus.txt", "testWhiteSpace.txt",
                "testWhitespace2.txt", "testImbalancedOperators.txt", "testInvalidCharacters.txt",
                "testMissingAssignment.txt", "testMissingExpressions.txt", "testMissingOperands.txt",
                "testMultipleConsecutiveAssignments.txt"
        };


        for (String file : fileArray) {
            System.out.println("Processing file: " + file);

            // Create Lexer and Parser
            Lexer lexer = new Lexer(file);
            Parser parser = new Parser(lexer, 10);  // Adjust memory size if needed

            System.out.println("Parser Output: ");
            if (parser.parseProgram()) {
                // If parsing is successful, run the bytecode
                parser.runProgram();

                // Retrieve and print the memory after execution
                ByteCodeInterpreter interpreter = parser.getByteCodeInterpreter();
                ArrayList<Integer> memory = interpreter.getMemory();
                ArrayList<Integer> bytecode = interpreter.getBytecode();

                System.out.println("Bytecode Generated:");
                for (int i = 0; i < bytecode.size(); i++){
                    System.out.print(bytecode.get(i) + " ");
                }
                System.out.println();

                System.out.println("Memory after execution:");
                for (int i = 0; i < memory.size(); i++) {
                    System.out.print(memory.get(i) + " ");
                }
                System.out.println();  // Newline for better formatting
            } else {
                System.out.println("Parsing failed for file: " + file);
            }

            System.out.println("-----------------------------------------------------------------------------------------");
        }
    }
}
