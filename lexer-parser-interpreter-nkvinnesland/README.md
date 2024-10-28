**Project Title**

Lexer: A Simple Tokenizer for Parsing Text Files in Java

**Overview**

This project is a simple lexer implemented in Java that processes input text from a file and breaks it into tokens. The lexer recognizes a variety of token types, such as integers, identifiers, operators (+, -, *, /), and assignment operators (=). It is designed to demonstrate basic file handling and text processing in Java. The tokens extracted from the input file are represented using the Token class, and the lexer builds a list of tokens, which can be used by further processes such as parsing.

**How to Deploy the App**

Clone or download the repository to your local machine.
Ensure that you have Java installed (preferably JDK 11 or later).
Compile the code using the following command:

javac Lexer.java Token.java
javac Token.java

To run the application, provide the name of the file you wish to parse as a command-line argument. For example:

java Lexer test.txt

If no file is provided, a default file (testExpectingAssignOp.txt) will be used, or the program will prompt you to input a string for parsing.

**Development Process**

The development process for the lexer began by identifying the structure of tokens that the lexer should recognize: integers, identifiers, operators, and assignment tokens. We used Java’s StringBuilder to process characters from the input buffer and create tokens based on the input file.

**Bug Fixes and Solutions**

One bug we encountered early on was related to handling whitespace. Initially, the lexer failed to skip over spaces correctly, causing invalid tokens to be generated. The issue was fixed by adding a loop that skips over any whitespace before processing the next token:

while (index < buffer.length() && Character.isWhitespace(buffer.charAt(index))) {
    index++;
}

Another bug arose when reaching the end of the file. The lexer didn’t handle this properly at first, leading to multiple EOF assignments at the end of each test. This was resolved by explicitly checking for the end of the file:

if (index >= buffer.length()) {
    return null;
}

**Key Java Concepts**

Throughout development, key Java concepts such as file I/O, string manipulation, and object-oriented programming were reinforced. For instance, we used the Files.readAllBytes() method to read the entire file into a string buffer:

byte[] allBytes = Files.readAllBytes(filePath);
buffer = new String(allBytes);
This enabled us to efficiently manage input and then process it using character iteration.

Matching Algorithm
The algorithm for matching characters and creating tokens follows a simple pattern. For identifiers, the lexer captures continuous letters and digits:

private Token getIdentifier() {
    StringBuilder idsb = new StringBuilder();
    while (index < buffer.length() && (Character.isLetter(buffer.charAt(index)) || Character.isDigit(buffer.charAt(index)))) {
        idsb.append(buffer.charAt(index));
        index++;
    }
    return new Token(IDTOKEN, idsb.toString());
}

A similar approach is used for integers.

If I were to redesign this, I might incorporate a finite state machine to improve the efficiency and maintainability of the matching algorithm. In other words I would use a switch statement with an enum for START, INTEGER, IDENTIFIER, etc... This may make the program more scalable.

**Description of Completed Items**
All key features of the lexer were implemented:

It processes integers, identifiers, and basic operators.
The EOF token is correctly added at the end of the token list.
The lexer handles unknown characters gracefully by assigning them an UNKNOWN token type.
There were no major features left incomplete, and the extra credit was also completed by adding tokens for subtraction, division, and multiplication.

**Test Plan**

To ensure the lexer functions as expected, the following tests were performed:

Basic file parsing: The lexer was tested on a simple text file with a mix of integers, identifiers, and operators. The output matched the expected token types.

Whitespace handling: The lexer was tested on files with varying amounts of whitespace between tokens to ensure that whitespace is skipped correctly.

Invalid characters: Files with unsupported characters were tested to ensure they were classified as UNKNOWN.

Edge cases: Files with no content and files with a single token were tested to confirm proper EOF handling.

Test File (Basic operations): Input like x = 5 + 3 * 10 correctly produces tokens for identifiers, integers, and operators.

The lexer was also tested on the lexertest.java provided in the boilerplate repository. All tests were passed.

**Test Video**

A test video showcasing the lexer in action, parsing several sample files, can be found [[here](https://youtu.be/yvRSFmDeNas)]. The video explains the tests and demonstrates the application’s functionality.

**PART 2: PARSER RREADME**

**Overview**
This project is a Java-based Lexer and Parser system designed to tokenize and parse input data from a file. The Lexer identifies tokens, such as identifiers and keywords, and the Parser processes these tokens to create a structured representation of the input. The system also includes an IdTable class for tracking identifiers and their corresponding memory addresses.

**How To Run It**
Follow the steps above for running the lexer class. After that's done run in your command line:

javac Parser.java
javac IdTable.java

After it compiles you will want to compile the parsertest.java by running this command:

java ParserTest.java

**Development Process**
Initially I thought that the logic for this program was going to be much easier than designing the lexer, however figuring out how to keep track of line number as well as checking the order of the different tokens and the placement of the assignment operators and ID proved to be a challenge. However here is a brief description of the methods implemented:

The program starts with a list of tokens generated by the Lexer.
It enters the parseProgram() method, which loops through the tokens.
For each token, it checks if it’s an identifier, followed by an assignment, and then an expression.
It processes expressions, ensuring identifiers used on the right-hand side are already defined.
If any errors occur, the program outputs "Invalid Program" and terminates early.
If everything checks out, it prints "Valid Program."

**What I Completed**
All of the specs required on the rubric were completed for part 2 of this project. I also used generative AI to create more test.txt files to see if my program would catch errors that weren't provided in the original repository and it caught all of them as well. Please see video for further demonstration.

**Test Plan**
Testing my code was originally where I ran into most of the issues in terms of the order in which I should be running my different methods in order to properly catch all the errors or verify that the ID and assignment tokens were in the correct spots. After finishing the tests provided at a 100% success rate I added more tests (all of which had some sort of error) and it caught all of them as well.

**Test Video**
Here is the link to my video for part 2 [Youtube](https://youtu.be/9bbGKL2rQeE)

**PART 3: PARSER RREADME**

**Overview**
This project is a Java-based Lexer, Parser, and ByteCodeInterpreter system designed to tokenize, parse, and execute input data from a file. The Lexer identifies tokens such as identifiers, keywords, and operators. The Parser processes these tokens to create a structured representation of the input, which is then translated into SIMPLE Bytecode. The ByteCodeInterpreter executes this bytecode to perform the intended operations. Additionally, the system includes an IdTable class for tracking identifiers and their corresponding memory addresses, enabling the interpreter to reference and manipulate these variables during execution.

**How To Run It**
Go to your terminal or command line tool. Proceed to the directory in which the project is located. Once there enter the following commands:

javac Lexer.java
javac Parser.java
javac IdTable.java
javac Token.java
javac ByteCodeInterpreter.java
javac ParserTest.java

After compiling all of the required files run the following command:

java ParserTest.java

**Development Process**
The addition of the ByteCodeInterpreter significantly expanded the scope of the project, transforming it from just a tokenization and parsing tool into a full-fledged system capable of executing bytecode. Initially, the Lexer and Parser were responsible for identifying tokens and ensuring their correct sequence, such as validating assignments and expressions. However, integrating the ByteCodeInterpreter required an entirely new layer of functionality.

The development process involved updating the Parser to generate SIMPLE Bytecode after validating the token stream. This bytecode, representing operations and assignments, is passed to the ByteCodeInterpreter for execution. The interpreter then processes the bytecode, handling operations such as assignments, arithmetic expressions, and memory management using the IdTable for identifier lookups.

Challenges during this phase included ensuring that the bytecode generated by the Parser was accurate and correctly mapped to the original source code, and that the interpreter executed it in the proper sequence. Additionally, testing and debugging focused on error handling, ensuring that invalid token sequences would still trigger appropriate error messages like “Invalid Program.”

This summary captures the key points of the development process, focusing on the transition from parsing to bytecode execution. Let me know if you’d like any adjustments!

**What I Completed**
I completed all of the specs as described by the rubric however I did not get around to figuring out the extra credit for part 3.

**Test Plan**
Testing my code was originally where I ran into most of the issues in terms of the order in which I should be running my different methods in order to properly catch all the errors or verify that the ID and assignment tokens were in the correct spots. After finishing the tests provided at a 100% success rate I added more tests (all of which had some sort of error) and it caught all of them as well.

**Test Video**
Here is the link to my video for part 3 [Youtube](https://youtu.be/NS4GX9loID4)



