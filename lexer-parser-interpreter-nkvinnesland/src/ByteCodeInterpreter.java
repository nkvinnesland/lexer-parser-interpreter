import java.util.ArrayList;
import java.util.ArrayList;

public class ByteCodeInterpreter {

    // List to store the bytecode instructions and operands
    private ArrayList<Integer> bytecode;

    // List to represent the memory of the interpreter
    private ArrayList<Integer> memory;

    // Constants representing bytecode commands
    public static final int LOAD = 0;
    public static final int LOADI = 1;
    public static final int STORE = 2;

    // The accumulator stores intermediate results during execution
    private int accumulator;

    // The size of the memory
    private int memorySize;

    // Constructor to initialize the interpreter with a given memory size
    public ByteCodeInterpreter(int memSize) {
        this.memorySize = memSize;
        this.bytecode = new ArrayList<>();
        this.memory = new ArrayList<>();

        // Initialize memory to 0 for each memory location
        for (int i = 0; i < memSize; i++) {
            memory.add(0);
        }
        this.accumulator = 0; // Initialize accumulator to 0
    }

    // Method to generate bytecode by adding a command and its operand to the list
    public void generate(int command, int operand) {
        bytecode.add(command);
        bytecode.add(operand);
    }

    // Method to run the bytecode instructions
    public void run() {
        // Loop through bytecode array two elements at a time (command + operand)
        for (int i = 0; i < bytecode.size(); i += 2) {
            // Ensure there are at least two elements left (command and operand)
            if (i + 1 >= bytecode.size()) {
                System.out.println("Invalid bytecode: Missing operand for command at index " + i);
                break;
            }

            // Retrieve the current command and operand
            int command = bytecode.get(i);
            int operand = bytecode.get(i + 1);

            // Execute the command based on its type
            switch (command) {
                case LOAD:
                    runLoad(operand); // Load value from memory into the accumulator
                    break;
                case LOADI:
                    runLoadi(operand); // Load immediate value (operand) into the accumulator
                    break;
                case STORE:
                    runStore(operand); // Store accumulator value into memory
                    break;
                default:
                    System.out.println("Invalid command!"); // Invalid command error
            }
        }
    }

    // Helper method to execute LOAD command (loads from memory to accumulator)
    private void runLoad(int address) {
        // Check if the memory address is valid
        if (address < 0 || address >= memorySize) {
            System.out.println("Run-time error: Address out of bounds");
            return;
        }
        accumulator += memory.get(address); // Add value from memory to accumulator
    }

    // Helper method to execute LOADI command (loads immediate value into accumulator)
    public void runLoadi(int operand) {
        accumulator += operand; // Add operand directly to accumulator
    }

    // Helper method to execute STORE command (stores accumulator into memory)
    private void runStore(int address) {
        // Check if the memory address is valid
        if (address < 0 || address >= memorySize) {
            System.out.println("Run-time error: Address out of bounds");
            return;
        }
        memory.set(address, accumulator); // Store accumulator value in memory
        accumulator = 0; // Reset accumulator after storing
    }

    // Getter method for the bytecode, useful for testing and debugging
    public ArrayList<Integer> getBytecode() {
        return bytecode;
    }

    // Getter method for the memory, useful for testing and debugging
    public ArrayList<Integer> getMemory() {
        return memory;
    }

    // Getter method for the accumulator, useful for testing and debugging
    public int getAccumulator() {
        return accumulator;
    }

    // toString method to print the current state of the interpreter
    public String toString() {
        return "Byte Code Generated: " + getBytecode() + "\nMemory: " + getMemory();
    }
}