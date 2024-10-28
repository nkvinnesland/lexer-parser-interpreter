import java.util.ArrayList;
import java.util.Objects;

public class Parser {

    private ArrayList<Token> tokeList;
    private IdTable idTable;
    private ByteCodeInterpreter bytecodeInterpreter;
    int index = 0;

    public Parser(Lexer lexer, int memorySize) {
        this.tokeList = lexer.getAllTokens();
        this.idTable = new IdTable();
        this.bytecodeInterpreter = new ByteCodeInterpreter(memorySize);  // Instantiate ByteCodeInterpreter
    }

    @Override
    public String toString() {
        return "Parser{" +
                "tokeList=" + tokeList +
                ", idTable=" + idTable +
                ", bytecodeInterpreter=" + bytecodeInterpreter +
                ", index=" + index +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parser parser = (Parser) o;
        return index == parser.index && Objects.equals(tokeList, parser.tokeList) && Objects.equals(idTable, parser.idTable) && Objects.equals(bytecodeInterpreter, parser.bytecodeInterpreter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokeList, idTable, bytecodeInterpreter, index);
    }

    // Parse the entire program, handling multiple assignments
    public boolean parseProgram() {
        boolean isValid = true;
        while (index < tokeList.size()) {  // Iterate through the token list
            if (!parseAssignment()) {
                isValid = false;
                break;  // Stop parsing if an error is encountered
            }

            Token nextToken = tokeList.get(index);  // Peek at the next token
            if (nextToken.getType().equals(Lexer.EOFTOKEN)) {
                break;  // End parsing if we reach the end of the file
            }
        }

        // If parsing was successful, print "Valid Program"
        if (isValid) {
            System.out.println("Valid Program");
        } else {
            System.out.println("Invalid Program");
        }
        System.out.println(idTable.toString());
        return isValid;
    }

    // Parse a single assignment
    public boolean parseAssignment() {
        Token token = tokeList.get(index++);  // Fetch and advance token

        // Check if the token is an identifier
        if (!token.getType().equals(Lexer.IDTOKEN)) {
            System.out.println("Error: Expecting identifier, line " + token.getLineNumber());
            return false;
        }

        // Parse the identifier and add it to the IdTable (since it's on the left-hand side)
        if (!parseId(token, true)) {
            return false;  // Stop if error in left-hand side identifier
        }

        int address = idTable.getAddress(token.getValue());  // Get address for storing the result

        // Expect assignment operator
        token = tokeList.get(index++);
        if (!token.getType().equals(Lexer.ASSMTTOKEN)) {
            System.out.println("Error: Expecting assignment operator, line " + token.getLineNumber());
            return false;
        }

        // Parse the expression following the assignment operator and generate code
        if (!parseExpression(address)) {
            return false;  // Stop if error in the expression
        }

        return true;  // Successfully parsed assignment
    }

    // Parse an expression, handling identifiers and integers
    // This version also generates bytecode
    public boolean parseExpression(int storeAddress) {
        Token token = tokeList.get(index++);  // Fetch and advance token

        // Expect an identifier or an integer to start the expression
        if (!token.getType().equals(Lexer.IDTOKEN) && !token.getType().equals(Lexer.INTTOKEN)) {
            System.out.println("Error: Expected identifier or integer at the start of expression, line " + token.getLineNumber());
            return false;
        }

        // If it's an identifier, ensure it is defined before proceeding
        if (token.getType().equals(Lexer.IDTOKEN)) {
            int address = idTable.getAddress(token.getValue());
            if (address == -1) {
                System.out.println("Error: Identifier '" + token.getValue() + "' is not defined, line " + token.getLineNumber());
                return false;  // Undefined identifier
            }
            bytecodeInterpreter.generate(ByteCodeInterpreter.LOAD, address);
        } else {
            int value = Integer.parseInt(token.getValue());
            bytecodeInterpreter.generate(ByteCodeInterpreter.LOADI, value);
        }

        // Process subsequent operators and operands
        while (index < tokeList.size()) {
            token = tokeList.get(index);  // Peek at the next token (without advancing)

            // Check for operators
            if (token.getType().equals(Lexer.PLUSTOKEN)) {
                index++;  // Move past the operator

                // After an operator, we expect an identifier or integer
                token = tokeList.get(index++);  // Fetch and advance token
                if (!token.getType().equals(Lexer.IDTOKEN) && !token.getType().equals(Lexer.INTTOKEN)) {
                    System.out.println("Error: Expected identifier or integer after operator, line " + token.getLineNumber());
                    return false;
                }

                // If it's an identifier, ensure it is defined
                if (token.getType().equals(Lexer.IDTOKEN)) {
                    int address = idTable.getAddress(token.getValue());
                    if (address == -1) {
                        System.out.println("Error: Identifier '" + token.getValue() + "' is not defined, line " + token.getLineNumber());
                        return false;  // Undefined identifier
                    }
                    bytecodeInterpreter.generate(ByteCodeInterpreter.LOAD, address);
                } else {
                    int value = Integer.parseInt(token.getValue());
                    bytecodeInterpreter.generate(ByteCodeInterpreter.LOADI, value);
                }

            } else {
                // No operator, so we stop the expression parsing
                break;
            }
        }

        // After processing the expression, store the result in the specified memory address
        bytecodeInterpreter.generate(ByteCodeInterpreter.STORE, storeAddress);

        return true;  // Expression is valid
    }
    // Parse an identifier and check/add to the IdTable
    public boolean parseId(Token token, boolean isLeftHandSide) {
        if (token.getType().equals(Lexer.IDTOKEN)) {
            String id = token.getValue();
            if (isLeftHandSide) {
                // On the left-hand side, add the identifier to the IdTable
                idTable.addEntry(id);
            } else {
                // On the right-hand side, check if the identifier is already defined
                if (idTable.getAddress(id) == -1) {
                    System.out.println("Error: Identifier '" + id + "' used but not defined, line " + token.getLineNumber());
                    return false;
                }
            }
            return true;  // Identifier handled successfully
        } else {
            System.out.println("Error: Expecting identifier, line " + token.getLineNumber());
            return false;
        }
    }

    public ByteCodeInterpreter getByteCodeInterpreter() {
        return this.bytecodeInterpreter;
    }

    // Method to run the bytecode after parsing is complete
    public void runProgram() {
        bytecodeInterpreter.run();  // Execute the bytecode
    }
}

