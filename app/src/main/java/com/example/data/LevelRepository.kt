package com.example.data

import com.example.model.*

object LevelRepository {

    val levels: List<Level> = listOf(
        // LEVEL 1: Variables & Data Types
        Level(
            id = 1,
            tier = Tier.BEGINNER,
            title = "Level 1: Variables & Types",
            subtitle = "Memory Slots & Allocation",
            conceptTitle = "C Variables & Data Types",
            conceptExplanation = "In C, variables are designated named storage locations in memory. Every type has a fixed size in bytes: `int` (typically 4 bytes), `char` (1 byte), and `float` (4 bytes). Declaring a variable assigns an address in the Stack.",
            learningObjectives = listOf(
                "Understand primitive types: int, char, float",
                "Observe memory allocation on the Stack",
                "Format output using printf specifiers (%d, %c, %.2f)"
            ),
            sourceCode = listOf(
                CodeLine(1, "#include <stdio.h>", "Includes standard I/O library"),
                CodeLine(2, "int main() {", "Program entry point"),
                CodeLine(3, "    int age = 21;", "Allocates 4 bytes on Stack for integer 'age'"),
                CodeLine(4, "    char grade = 'A';", "Allocates 1 byte on Stack for character 'grade'"),
                CodeLine(5, "    float gpa = 3.85f;", "Allocates 4 bytes on Stack for floating-point 'gpa'"),
                CodeLine(6, "    printf(\"Age: %d, Grade: %c\\n\", age, grade);", "Prints formatted values to stdout"),
                CodeLine(7, "    return 0;", "Exits program with status 0"),
                CodeLine(8, "}", "End of main")
            ),
            initialMemory = listOf(
                MemoryCell("0x7ffd1000", "age", "int", "?? (uninit)", 4),
                MemoryCell("0x7ffd1004", "grade", "char", "?? (uninit)", 1),
                MemoryCell("0x7ffd1008", "gpa", "float", "?? (uninit)", 4)
            ),
            initialStack = listOf(
                StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("age", "grade", "gpa"))
            ),
            steps = listOf(
                SimStep(
                    lineIndex = 1,
                    activeStatement = "int main()",
                    statusText = "Initializing main() stack frame...",
                    stdout = "Initializing virtual C runtime...\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1000", "age", "int", "??", 4),
                        MemoryCell("0x7ffd1004", "grade", "char", "??", 1),
                        MemoryCell("0x7ffd1008", "gpa", "float", "??", 4)
                    ),
                    stackSnapshot = listOf(
                        StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("age", "grade", "gpa"))
                    )
                ),
                SimStep(
                    lineIndex = 2,
                    activeStatement = "int age = 21;",
                    statusText = "Allocating 4 bytes at 0x7ffd1000, value = 21",
                    stdout = null,
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1000", "age", "int", "21", 4, isHighlighted = true),
                        MemoryCell("0x7ffd1004", "grade", "char", "??", 1),
                        MemoryCell("0x7ffd1008", "gpa", "float", "??", 4)
                    ),
                    stackSnapshot = listOf(
                        StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("age=21", "grade", "gpa"))
                    )
                ),
                SimStep(
                    lineIndex = 3,
                    activeStatement = "char grade = 'A';",
                    statusText = "Allocating 1 byte at 0x7ffd1004, value = 'A' (ASCII 65)",
                    stdout = null,
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1000", "age", "int", "21", 4),
                        MemoryCell("0x7ffd1004", "grade", "char", "'A' (65)", 1, isHighlighted = true),
                        MemoryCell("0x7ffd1008", "gpa", "float", "??", 4)
                    ),
                    stackSnapshot = listOf(
                        StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("age=21", "grade='A'", "gpa"))
                    )
                ),
                SimStep(
                    lineIndex = 4,
                    activeStatement = "float gpa = 3.85f;",
                    statusText = "Allocating 4 bytes at 0x7ffd1008, value = 3.85",
                    stdout = null,
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1000", "age", "int", "21", 4),
                        MemoryCell("0x7ffd1004", "grade", "char", "'A'", 1),
                        MemoryCell("0x7ffd1008", "gpa", "float", "3.850", 4, isHighlighted = true)
                    ),
                    stackSnapshot = listOf(
                        StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("age=21", "grade='A'", "gpa=3.85"))
                    )
                ),
                SimStep(
                    lineIndex = 5,
                    activeStatement = "printf(\"Age: %d, Grade: %c\\n\", age, grade);",
                    statusText = "Sending formatted buffer to stdout",
                    stdout = "Age: 21, Grade: A\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1000", "age", "int", "21", 4),
                        MemoryCell("0x7ffd1004", "grade", "char", "'A'", 1),
                        MemoryCell("0x7ffd1008", "gpa", "float", "3.850", 4)
                    ),
                    stackSnapshot = listOf(
                        StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("age=21", "grade='A'", "gpa=3.85"))
                    )
                ),
                SimStep(
                    lineIndex = 6,
                    activeStatement = "return 0;",
                    statusText = "Process completed successfully. Exit code: 0",
                    stdout = "[Process finished with exit code 0]\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1000", "age", "int", "21", 4),
                        MemoryCell("0x7ffd1004", "grade", "char", "'A'", 1),
                        MemoryCell("0x7ffd1008", "gpa", "float", "3.850", 4)
                    ),
                    stackSnapshot = listOf(
                        StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("age=21", "grade='A'", "gpa=3.85"))
                    )
                )
            ),
            challengeQuestion = "Which printf format specifier must you use to print a standard signed integer in C?",
            challengeOptions = listOf("%s", "%f", "%d", "%c"),
            correctOptionIndex = 2,
            challengeHint = "Remember: %d stands for decimal integer, %f for float, %c for character, and %s for string.",
            cSnippetSnippet = "int age = 21;\nprintf(\"%d\", age);",
            targetPointer = "0x7ffd1000",
            defaultSize = "4 bytes"
        ),

        // LEVEL 2: Operators & Arithmetic
        Level(
            id = 2,
            tier = Tier.BEGINNER,
            title = "Level 2: Operators & Math",
            subtitle = "Registers & Arithmetic Logic",
            conceptTitle = "Operators & Modulo Logic",
            conceptExplanation = "C provides arithmetic operators (+, -, *, /, %). Division between integers discards decimals (5 / 2 = 2). The modulo operator `%` calculates the integer remainder (7 % 3 = 1), essential for wrap-arounds and cycle logic.",
            learningObjectives = listOf(
                "Master integer division vs floating-point division",
                "Use the modulo % operator for remainder calculation",
                "Understand pre and post-increment operators (score++)"
            ),
            sourceCode = listOf(
                CodeLine(1, "int score = 50;", "Initial score in memory"),
                CodeLine(2, "score += 25;", "Add 25 to current score"),
                CodeLine(3, "int bonus = score % 10;", "Calculate remainder: 75 % 10 = 5"),
                CodeLine(4, "score++;", "Increment score by 1 (75 -> 76)"),
                CodeLine(5, "printf(\"Score: %d, Bonus: %d\\n\", score, bonus);", "Print final values"),
                CodeLine(6, "return 0;", "Exit code 0")
            ),
            initialMemory = listOf(
                MemoryCell("0x7ffd1020", "score", "int", "0", 4),
                MemoryCell("0x7ffd1024", "bonus", "int", "0", 4)
            ),
            initialStack = listOf(
                StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("score", "bonus"))
            ),
            steps = listOf(
                SimStep(
                    lineIndex = 0,
                    activeStatement = "int score = 50;",
                    statusText = "Setting score = 50",
                    stdout = "Executing arithmetic simulation...\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1020", "score", "int", "50", 4, isHighlighted = true),
                        MemoryCell("0x7ffd1024", "bonus", "int", "0", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("score=50", "bonus=0")))
                ),
                SimStep(
                    lineIndex = 1,
                    activeStatement = "score += 25;",
                    statusText = "ALU operation: 50 + 25 = 75",
                    stdout = null,
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1020", "score", "int", "75", 4, isHighlighted = true),
                        MemoryCell("0x7ffd1024", "bonus", "int", "0", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("score=75", "bonus=0")))
                ),
                SimStep(
                    lineIndex = 2,
                    activeStatement = "int bonus = score % 10;",
                    statusText = "Modulo calculation: 75 % 10 = 5",
                    stdout = null,
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1020", "score", "int", "75", 4),
                        MemoryCell("0x7ffd1024", "bonus", "int", "5", 4, isHighlighted = true)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("score=75", "bonus=5")))
                ),
                SimStep(
                    lineIndex = 3,
                    activeStatement = "score++;",
                    statusText = "Increment operator: score is now 76",
                    stdout = null,
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1020", "score", "int", "76", 4, isHighlighted = true),
                        MemoryCell("0x7ffd1024", "bonus", "int", "5", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("score=76", "bonus=5")))
                ),
                SimStep(
                    lineIndex = 4,
                    activeStatement = "printf(\"Score: %d, Bonus: %d\\n\", score, bonus);",
                    statusText = "Printing output",
                    stdout = "Score: 76, Bonus: 5\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1020", "score", "int", "76", 4),
                        MemoryCell("0x7ffd1024", "bonus", "int", "5", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("score=76", "bonus=5")))
                )
            ),
            challengeQuestion = "In C, what is the exact result of the integer expression (14 / 4)?",
            challengeOptions = listOf("3.5", "3", "4", "2"),
            correctOptionIndex = 1,
            challengeHint = "Integer division in C truncates the fractional part towards zero; 14 divided by 4 is 3 with remainder 2.",
            cSnippetSnippet = "int result = 14 / 4; // evaluates to 3",
            targetPointer = "0x7ffd1020",
            defaultSize = "4 bytes"
        ),

        // LEVEL 3: Control Flow & Conditional Branching
        Level(
            id = 3,
            tier = Tier.BEGINNER,
            title = "Level 3: Control Flow",
            subtitle = "Conditional Branching & Logic",
            conceptTitle = "Branching with if/else",
            conceptExplanation = "In C, conditions evaluate to 0 (false) or any non-zero value (true). The CPU jumps instruction pointers based on condition flags. Logical operators include && (AND), || (OR), and ! (NOT).",
            learningObjectives = listOf(
                "Evaluate relational and logical conditions in C",
                "Trace branch jumps in CPU instruction pointer",
                "Handle multi-condition guard checks"
            ),
            sourceCode = listOf(
                CodeLine(1, "int temp = 104;", "Core temperature metric"),
                CodeLine(2, "int fanMode = 0;", "Fan level (0=OFF, 1=LOW, 2=TURBO)"),
                CodeLine(3, "if (temp > 100) {", "Check thermal threshold"),
                CodeLine(4, "    fanMode = 2; // TURBO", "Trigger cooling mechanism"),
                CodeLine(5, "} else {", "Normal state"),
                CodeLine(6, "    fanMode = 1; // NORMAL", "Normal cooling"),
                CodeLine(7, "}", "End if block"),
                CodeLine(8, "printf(\"Fan mode: %d\\n\", fanMode);", "Output active fan state")
            ),
            initialMemory = listOf(
                MemoryCell("0x7ffd1040", "temp", "int", "104", 4),
                MemoryCell("0x7ffd1044", "fanMode", "int", "0", 4)
            ),
            initialStack = listOf(
                StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("temp=104", "fanMode=0"))
            ),
            steps = listOf(
                SimStep(
                    lineIndex = 0,
                    activeStatement = "int temp = 104;",
                    statusText = "Checking system temperature: 104 C",
                    stdout = "Initiating thermal monitor simulation...\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1040", "temp", "int", "104", 4, isHighlighted = true),
                        MemoryCell("0x7ffd1044", "fanMode", "int", "0", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("temp=104", "fanMode=0")))
                ),
                SimStep(
                    lineIndex = 2,
                    activeStatement = "if (temp > 100)",
                    statusText = "Evaluating: 104 > 100 -> TRUE (1). Branching to TURBO block.",
                    stdout = null,
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1040", "temp", "int", "104", 4),
                        MemoryCell("0x7ffd1044", "fanMode", "int", "0", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("temp=104", "fanMode=0")))
                ),
                SimStep(
                    lineIndex = 3,
                    activeStatement = "fanMode = 2;",
                    statusText = "Assigning fanMode = 2 (TURBO)",
                    stdout = null,
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1040", "temp", "int", "104", 4),
                        MemoryCell("0x7ffd1044", "fanMode", "int", "2", 4, isHighlighted = true)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("temp=104", "fanMode=2"))),
                    loopCounterInfo = Pair("fanMode", 2)
                ),
                SimStep(
                    lineIndex = 7,
                    activeStatement = "printf(\"Fan mode: %d\\n\", fanMode);",
                    statusText = "Skipped else block, printing result",
                    stdout = "Fan mode: 2 (TURBO ACTIVE)\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1040", "temp", "int", "104", 4),
                        MemoryCell("0x7ffd1044", "fanMode", "int", "2", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("temp=104", "fanMode=2")))
                )
            ),
            challengeQuestion = "In C, what integer value does a boolean TRUE condition yield?",
            challengeOptions = listOf("0", "1", "-1", "null"),
            correctOptionIndex = 1,
            challengeHint = "In standard C, 0 is false, and 1 (or any non-zero integer) represents true.",
            cSnippetSnippet = "if (1) {\n    printf(\"Always executed\");\n}",
            targetPointer = "0x7ffd1044",
            defaultSize = "4 bytes"
        ),

        // LEVEL 4: Loops & Iterators
        Level(
            id = 4,
            tier = Tier.BEGINNER,
            title = "Level 4: Loops & Iterators",
            subtitle = "Animated Counters & Accumulation",
            conceptTitle = "For and While Loops",
            conceptExplanation = "Loops repeat a block of code as long as a condition holds true. A `for` loop has three clauses: initialization (`int i = 1`), condition (`i <= 3`), and update (`i++`). Watch the counter increment and memory update smoothly on every step.",
            learningObjectives = listOf(
                "Observe loop counter register incrementing",
                "Trace variable accumulation in memory",
                "Prevent infinite loop scenarios"
            ),
            sourceCode = listOf(
                CodeLine(1, "int sum = 0;", "Accumulator variable"),
                CodeLine(2, "for (int i = 1; i <= 3; i++) {", "Loop header: i from 1 to 3"),
                CodeLine(3, "    sum += i;", "Add counter to sum"),
                CodeLine(4, "    printf(\"i=%d, sum=%d\\n\", i, sum);", "Log loop state"),
                CodeLine(5, "}", "Loop boundary"),
                CodeLine(6, "printf(\"Total: %d\\n\", sum);", "Final result")
            ),
            initialMemory = listOf(
                MemoryCell("0x7ffd1060", "sum", "int", "0", 4),
                MemoryCell("0x7ffd1064", "i", "int", "0", 4)
            ),
            initialStack = listOf(
                StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("sum=0", "i=0"))
            ),
            steps = listOf(
                SimStep(
                    lineIndex = 0,
                    activeStatement = "int sum = 0;",
                    statusText = "Initializing accumulator sum = 0",
                    stdout = "Starting loop simulation...\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1060", "sum", "int", "0", 4, isHighlighted = true),
                        MemoryCell("0x7ffd1064", "i", "int", "0", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("sum=0", "i=0"))),
                    loopCounterInfo = Pair("i", 0)
                ),
                // Iteration 1
                SimStep(
                    lineIndex = 1,
                    activeStatement = "i = 1; (i <= 3)",
                    statusText = "Loop Iteration #1: i = 1, condition (1 <= 3) is TRUE",
                    stdout = null,
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1060", "sum", "int", "0", 4),
                        MemoryCell("0x7ffd1064", "i", "int", "1", 4, isHighlighted = true)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("sum=0", "i=1"))),
                    loopCounterInfo = Pair("i", 1)
                ),
                SimStep(
                    lineIndex = 2,
                    activeStatement = "sum += i; // 0 + 1 = 1",
                    statusText = "Accumulating: sum is now 1",
                    stdout = "i=1, sum=1\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1060", "sum", "int", "1", 4, isHighlighted = true),
                        MemoryCell("0x7ffd1064", "i", "int", "1", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("sum=1", "i=1"))),
                    loopCounterInfo = Pair("i", 1)
                ),
                // Iteration 2
                SimStep(
                    lineIndex = 1,
                    activeStatement = "i++; // i becomes 2 (2 <= 3 is TRUE)",
                    statusText = "Loop Iteration #2: incrementing i to 2",
                    stdout = null,
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1060", "sum", "int", "1", 4),
                        MemoryCell("0x7ffd1064", "i", "int", "2", 4, isHighlighted = true)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("sum=1", "i=2"))),
                    loopCounterInfo = Pair("i", 2)
                ),
                SimStep(
                    lineIndex = 2,
                    activeStatement = "sum += i; // 1 + 2 = 3",
                    statusText = "Accumulating: sum is now 3",
                    stdout = "i=2, sum=3\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1060", "sum", "int", "3", 4, isHighlighted = true),
                        MemoryCell("0x7ffd1064", "i", "int", "2", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("sum=3", "i=2"))),
                    loopCounterInfo = Pair("i", 2)
                ),
                // Iteration 3
                SimStep(
                    lineIndex = 1,
                    activeStatement = "i++; // i becomes 3 (3 <= 3 is TRUE)",
                    statusText = "Loop Iteration #3: incrementing i to 3",
                    stdout = null,
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1060", "sum", "int", "3", 4),
                        MemoryCell("0x7ffd1064", "i", "int", "3", 4, isHighlighted = true)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("sum=3", "i=3"))),
                    loopCounterInfo = Pair("i", 3)
                ),
                SimStep(
                    lineIndex = 2,
                    activeStatement = "sum += i; // 3 + 3 = 6",
                    statusText = "Accumulating: sum is now 6",
                    stdout = "i=3, sum=6\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1060", "sum", "int", "6", 4, isHighlighted = true),
                        MemoryCell("0x7ffd1064", "i", "int", "3", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("sum=6", "i=3"))),
                    loopCounterInfo = Pair("i", 3)
                ),
                // Termination
                SimStep(
                    lineIndex = 1,
                    activeStatement = "i++; // i becomes 4 (4 <= 3 is FALSE)",
                    statusText = "Loop terminates: (4 <= 3) is FALSE. Exiting loop body.",
                    stdout = null,
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1060", "sum", "int", "6", 4),
                        MemoryCell("0x7ffd1064", "i", "int", "4", 4, isHighlighted = true)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("sum=6", "i=4"))),
                    loopCounterInfo = Pair("i", 4)
                ),
                SimStep(
                    lineIndex = 5,
                    activeStatement = "printf(\"Total: %d\\n\", sum);",
                    statusText = "Final loop output",
                    stdout = "Total: 6\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd1060", "sum", "int", "6", 4),
                        MemoryCell("0x7ffd1064", "i", "int", "4", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("sum=6", "i=4")))
                )
            ),
            challengeQuestion = "If a for loop is written as 'for (int i=0; i<5; i++)', how many times does the body execute?",
            challengeOptions = listOf("4 times", "5 times", "6 times", "Infinite times"),
            correctOptionIndex = 1,
            challengeHint = "Count the indices: 0, 1, 2, 3, 4. That is exactly 5 iterations.",
            cSnippetSnippet = "for (int i = 0; i < 5; i++) {\n    // executes 5 times\n}",
            targetPointer = "0x7ffd1064",
            defaultSize = "4 bytes"
        ),

        // LEVEL 5: Functions & Call Stack
        Level(
            id = 5,
            tier = Tier.INTERMEDIATE,
            title = "Level 5: Functions & Stack",
            subtitle = "Call Frames, Push & Pop",
            conceptTitle = "Function Calls & The Call Stack",
            conceptExplanation = "Whenever a function is invoked in C, the CPU creates a new Stack Frame containing return address, arguments, and local variables. When the function returns, its frame is popped from the Stack.",
            learningObjectives = listOf(
                "Visualize stack frame push and pop transitions",
                "Understand pass-by-value argument semantics",
                "Trace return value propagation to caller"
            ),
            sourceCode = listOf(
                CodeLine(1, "int multiply(int a, int b) {", "Function header"),
                CodeLine(2, "    int res = a * b;", "Local variable calculation"),
                CodeLine(3, "    return res;", "Return value and destroy frame"),
                CodeLine(4, "}", "End function"),
                CodeLine(5, "int main() {", "Main caller"),
                CodeLine(6, "    int x = 6;", "Caller local variable"),
                CodeLine(7, "    int ans = multiply(x, 7);", "Pushes multiply() frame onto stack"),
                CodeLine(8, "    printf(\"Ans: %d\\n\", ans);", "Logs ans"),
                CodeLine(9, "    return 0;", "Exits main")
            ),
            initialMemory = listOf(
                MemoryCell("0x7ffd2000", "x", "int", "6", 4),
                MemoryCell("0x7ffd2004", "ans", "int", "0", 4)
            ),
            initialStack = listOf(
                StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("x=6", "ans=?"))
            ),
            steps = listOf(
                SimStep(
                    lineIndex = 5,
                    activeStatement = "int x = 6;",
                    statusText = "In main(): x = 6",
                    stdout = "Executing function call stack simulation...\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd2000", "x", "int", "6", 4, isHighlighted = true),
                        MemoryCell("0x7ffd2004", "ans", "int", "0", 4)
                    ),
                    stackSnapshot = listOf(
                        StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("x=6", "ans=?"))
                    )
                ),
                SimStep(
                    lineIndex = 6,
                    activeStatement = "int ans = multiply(x, 7);",
                    statusText = "CALL multiply(6, 7) -> PUSH new stack frame!",
                    stdout = "-> PUSH stack frame: multiply(a=6, b=7)\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd2000", "x", "int", "6", 4),
                        MemoryCell("0x7ffd2004", "ans", "int", "0", 4),
                        MemoryCell("0x7ffd1fc0", "a", "int", "6", 4, isHighlighted = true),
                        MemoryCell("0x7ffd1fc4", "b", "int", "7", 4, isHighlighted = true)
                    ),
                    stackSnapshot = listOf(
                        StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("x=6", "ans=?"), isActive = false),
                        StackFrame("f_mul", "multiply()", "0x00400084", listOf("a=6", "b=7"), listOf("res=?"), isActive = true)
                    )
                ),
                SimStep(
                    lineIndex = 1,
                    activeStatement = "int res = a * b;",
                    statusText = "Inside multiply(): res = 6 * 7 = 42",
                    stdout = null,
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd2000", "x", "int", "6", 4),
                        MemoryCell("0x7ffd2004", "ans", "int", "0", 4),
                        MemoryCell("0x7ffd1fc0", "a", "int", "6", 4),
                        MemoryCell("0x7ffd1fc4", "b", "int", "7", 4),
                        MemoryCell("0x7ffd1fc8", "res", "int", "42", 4, isHighlighted = true)
                    ),
                    stackSnapshot = listOf(
                        StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("x=6", "ans=?"), isActive = false),
                        StackFrame("f_mul", "multiply()", "0x00400084", listOf("a=6", "b=7"), listOf("res=42"), isActive = true)
                    )
                ),
                SimStep(
                    lineIndex = 2,
                    activeStatement = "return res;",
                    statusText = "RETURN 42 -> POP multiply() frame from stack!",
                    stdout = "<- POP stack frame: multiply() returned 42\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd2000", "x", "int", "6", 4),
                        MemoryCell("0x7ffd2004", "ans", "int", "42", 4, isHighlighted = true)
                    ),
                    stackSnapshot = listOf(
                        StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("x=6", "ans=42"), isActive = true)
                    )
                ),
                SimStep(
                    lineIndex = 7,
                    activeStatement = "printf(\"Ans: %d\\n\", ans);",
                    statusText = "Resumed main(): ans = 42",
                    stdout = "Ans: 42\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd2000", "x", "int", "6", 4),
                        MemoryCell("0x7ffd2004", "ans", "int", "42", 4)
                    ),
                    stackSnapshot = listOf(
                        StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("x=6", "ans=42"), isActive = true)
                    )
                )
            ),
            challengeQuestion = "What data structure governs function execution contexts in C?",
            challengeOptions = listOf("Queue (FIFO)", "Call Stack (LIFO)", "Linked List", "Binary Heap"),
            correctOptionIndex = 1,
            challengeHint = "Functions operate in Last-In First-Out (LIFO) order: the last function called is the first to return.",
            cSnippetSnippet = "void foo() { ... } // Frame pushed onto Call Stack",
            targetPointer = "0x7ffd2004",
            defaultSize = "4 bytes"
        ),

        // LEVEL 6: Pointers & Memory Addresses
        Level(
            id = 6,
            tier = Tier.INTERMEDIATE,
            title = "Level 6: Pointer Mastery",
            subtitle = "Addresses & Dereferencing",
            conceptTitle = "Pointers & The Dereference Operator",
            conceptExplanation = "A pointer in C is a variable whose value is the memory address of another variable. The address-of operator `&` retrieves an address. The dereference operator `*` accesses the value stored at that address.",
            learningObjectives = listOf(
                "Declare pointer variables with asterisks (int *ptr)",
                "Obtain memory addresses using &",
                "Mutate values remotely using dereferencing (*ptr = value)"
            ),
            sourceCode = listOf(
                CodeLine(1, "int secret = 42;", "Target integer in memory"),
                CodeLine(2, "int *ptr = &secret;", "Pointer holds address of secret"),
                CodeLine(3, "*ptr = 99;", "Dereference ptr and overwrite secret"),
                CodeLine(4, "printf(\"Secret: %d\\n\", secret);", "Prints updated secret value"),
                CodeLine(5, "printf(\"Address: %p\\n\", (void*)ptr);", "Prints pointer hex address")
            ),
            initialMemory = listOf(
                MemoryCell("0x7ffd3000", "secret", "int", "42", 4),
                MemoryCell("0x7ffd3008", "ptr", "int*", "0x00000000", 8, pointsToAddress = null)
            ),
            initialStack = listOf(
                StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("secret=42", "ptr=NULL"))
            ),
            steps = listOf(
                SimStep(
                    lineIndex = 0,
                    activeStatement = "int secret = 42;",
                    statusText = "secret allocated at 0x7ffd3000, value = 42",
                    stdout = "Initiating pointer simulation...\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd3000", "secret", "int", "42", 4, isHighlighted = true),
                        MemoryCell("0x7ffd3008", "ptr", "int*", "NULL", 8)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("secret=42", "ptr=NULL")))
                ),
                SimStep(
                    lineIndex = 1,
                    activeStatement = "int *ptr = &secret;",
                    statusText = "ptr stores address 0x7ffd3000 -> Pointer Wire Connected!",
                    stdout = "Pointer assigned: ptr -> &secret (0x7ffd3000)\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd3000", "secret", "int", "42", 4),
                        MemoryCell("0x7ffd3008", "ptr", "int*", "0x7ffd3000", 8, pointsToAddress = "0x7ffd3000", isHighlighted = true)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("secret=42", "ptr=0x7ffd3000"))),
                    activePointerSource = "0x7ffd3008",
                    activePointerTarget = "0x7ffd3000"
                ),
                SimStep(
                    lineIndex = 2,
                    activeStatement = "*ptr = 99;",
                    statusText = "Dereferencing *ptr: writing 99 into target 0x7ffd3000!",
                    stdout = "Dereference write: *ptr = 99 (secret updated!)\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd3000", "secret", "int", "99", 4, isHighlighted = true),
                        MemoryCell("0x7ffd3008", "ptr", "int*", "0x7ffd3000", 8, pointsToAddress = "0x7ffd3000")
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("secret=99", "ptr=0x7ffd3000"))),
                    activePointerSource = "0x7ffd3008",
                    activePointerTarget = "0x7ffd3000"
                ),
                SimStep(
                    lineIndex = 3,
                    activeStatement = "printf(\"Secret: %d\\n\", secret);",
                    statusText = "Direct read of secret confirms value changed to 99",
                    stdout = "Secret: 99\nAddress: 0x7ffd3000\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd3000", "secret", "int", "99", 4),
                        MemoryCell("0x7ffd3008", "ptr", "int*", "0x7ffd3000", 8, pointsToAddress = "0x7ffd3000")
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("secret=99", "ptr=0x7ffd3000")))
                )
            ),
            challengeQuestion = "Given 'int val = 10; int *p = &val;', which expression modifies val to 20?",
            challengeOptions = listOf("p = 20;", "*p = 20;", "&p = 20;", "val* = 20;"),
            correctOptionIndex = 1,
            challengeHint = "Use the dereference operator '*' on the pointer variable: *p = 20 changes the memory cell pointed to by p.",
            cSnippetSnippet = "int val = 10;\nint *p = &val;\n*p = 20;",
            targetPointer = "0x7ffd3000",
            defaultSize = "8 bytes (64-bit address)"
        ),

        // LEVEL 7: Arrays & Strings
        Level(
            id = 7,
            tier = Tier.INTERMEDIATE,
            title = "Level 7: Arrays & Strings",
            subtitle = "Contiguous Memory & '\\0'",
            conceptTitle = "Contiguous Memory & Null Terminators",
            conceptExplanation = "In C, an array is a contiguous block of memory elements. A string is simply an array of `char` terminated by a null byte `\\0` (ASCII 0x00). Array name decays to a pointer to its first element.",
            learningObjectives = listOf(
                "Inspect contiguous memory layouts of arrays",
                "Identify the role of the '\\0' null terminator",
                "Understand pointer arithmetic: *(arr + i)"
            ),
            sourceCode = listOf(
                CodeLine(1, "char msg[5] = \"CORE\";", "Allocates 5 bytes: 'C', 'O', 'R', 'E', '\\0'"),
                CodeLine(2, "int i = 0;", "Index iterator"),
                CodeLine(3, "while (msg[i] != '\\0') {", "Check for null terminator"),
                CodeLine(4, "    printf(\"%c \", *(msg + i));", "Pointer arithmetic access"),
                CodeLine(5, "    i++;", "Advance index"),
                CodeLine(6, "}", "Loop terminates at null byte")
            ),
            initialMemory = listOf(
                MemoryCell("0x7ffd4000", "msg[0]", "char", "'C' (0x43)", 1),
                MemoryCell("0x7ffd4001", "msg[1]", "char", "'O' (0x4F)", 1),
                MemoryCell("0x7ffd4002", "msg[2]", "char", "'R' (0x52)", 1),
                MemoryCell("0x7ffd4003", "msg[3]", "char", "'E' (0x45)", 1),
                MemoryCell("0x7ffd4004", "msg[4]", "char", "'\\0' (0x00)", 1),
                MemoryCell("0x7ffd4008", "i", "int", "0", 4)
            ),
            initialStack = listOf(
                StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("msg=\"CORE\"", "i=0"))
            ),
            steps = listOf(
                SimStep(
                    lineIndex = 0,
                    activeStatement = "char msg[5] = \"CORE\";",
                    statusText = "Allocating 5 contiguous bytes on Stack including '\\0'",
                    stdout = "String memory allocated...\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd4000", "msg[0]", "char", "'C'", 1, isHighlighted = true),
                        MemoryCell("0x7ffd4001", "msg[1]", "char", "'O'", 1, isHighlighted = true),
                        MemoryCell("0x7ffd4002", "msg[2]", "char", "'R'", 1, isHighlighted = true),
                        MemoryCell("0x7ffd4003", "msg[3]", "char", "'E'", 1, isHighlighted = true),
                        MemoryCell("0x7ffd4004", "msg[4]", "char", "'\\0'", 1, isHighlighted = true),
                        MemoryCell("0x7ffd4008", "i", "int", "0", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("msg=\"CORE\"", "i=0")))
                ),
                SimStep(
                    lineIndex = 2,
                    activeStatement = "while (msg[i] != '\\0') // i=0: 'C' != '\\0'",
                    statusText = "Index 0: 'C' (0x43) is not '\\0'. Reading *(msg + 0)",
                    stdout = "C ",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd4000", "msg[0]", "char", "'C'", 1, isHighlighted = true),
                        MemoryCell("0x7ffd4001", "msg[1]", "char", "'O'", 1),
                        MemoryCell("0x7ffd4002", "msg[2]", "char", "'R'", 1),
                        MemoryCell("0x7ffd4003", "msg[3]", "char", "'E'", 1),
                        MemoryCell("0x7ffd4004", "msg[4]", "char", "'\\0'", 1),
                        MemoryCell("0x7ffd4008", "i", "int", "0", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("msg=\"CORE\"", "i=0"))),
                    loopCounterInfo = Pair("i", 0)
                ),
                SimStep(
                    lineIndex = 2,
                    activeStatement = "while (msg[i] != '\\0') // i=3: 'E' != '\\0'",
                    statusText = "Traversing array elements towards null terminator...",
                    stdout = "O R E ",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd4000", "msg[0]", "char", "'C'", 1),
                        MemoryCell("0x7ffd4001", "msg[1]", "char", "'O'", 1),
                        MemoryCell("0x7ffd4002", "msg[2]", "char", "'R'", 1),
                        MemoryCell("0x7ffd4003", "msg[3]", "char", "'E'", 1, isHighlighted = true),
                        MemoryCell("0x7ffd4004", "msg[4]", "char", "'\\0'", 1),
                        MemoryCell("0x7ffd4008", "i", "int", "3", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("msg=\"CORE\"", "i=3"))),
                    loopCounterInfo = Pair("i", 3)
                ),
                SimStep(
                    lineIndex = 2,
                    activeStatement = "while (msg[i] != '\\0') // i=4: '\\0' == '\\0'",
                    statusText = "Reached '\\0' at index 4 (0x7ffd4004). Loop stops cleanly!",
                    stdout = "\n[Null byte encountered, string safely terminated]\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd4000", "msg[0]", "char", "'C'", 1),
                        MemoryCell("0x7ffd4001", "msg[1]", "char", "'O'", 1),
                        MemoryCell("0x7ffd4002", "msg[2]", "char", "'R'", 1),
                        MemoryCell("0x7ffd4003", "msg[3]", "char", "'E'", 1),
                        MemoryCell("0x7ffd4004", "msg[4]", "char", "'\\0'", 1, isHighlighted = true),
                        MemoryCell("0x7ffd4008", "i", "int", "4", 4)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("msg=\"CORE\"", "i=4"))),
                    loopCounterInfo = Pair("i", 4)
                )
            ),
            challengeQuestion = "How many bytes are required to store the string literal \"HELLO\" in C?",
            challengeOptions = listOf("5 bytes", "6 bytes", "10 bytes", "4 bytes"),
            correctOptionIndex = 1,
            challengeHint = "Don't forget the invisible null terminator '\\0'! 5 letters + 1 null byte = 6 bytes.",
            cSnippetSnippet = "char str[6] = \"HELLO\"; // Needs 6 bytes for '\\0'",
            targetPointer = "0x7ffd4004",
            defaultSize = "5 bytes"
        ),

        // LEVEL 8: Dynamic Memory (malloc & free)
        Level(
            id = 8,
            tier = Tier.ADVANCED,
            title = "Level 8: Heap & Dynamic Memory",
            subtitle = "malloc(), free() & Leaks",
            conceptTitle = "Heap Memory Management",
            conceptExplanation = "Dynamic memory is allocated at runtime using `malloc()` from the Heap. Unlike Stack variables, Heap allocations persist until explicitly released with `free()`. Forgetting to free leads to memory leaks!",
            learningObjectives = listOf(
                "Request heap blocks using malloc()",
                "Check for NULL allocation failures",
                "Prevent memory leaks by calling free() before pointer loss"
            ),
            sourceCode = listOf(
                CodeLine(1, "int *heapArr = (int*)malloc(2 * sizeof(int));", "Allocates 8 bytes on Heap"),
                CodeLine(2, "if (heapArr == NULL) return 1;", "Null pointer safety check"),
                CodeLine(3, "heapArr[0] = 100;", "Write to heap slot 0"),
                CodeLine(4, "heapArr[1] = 200;", "Write to heap slot 1"),
                CodeLine(5, "free(heapArr);", "Releases allocated heap memory"),
                CodeLine(6, "heapArr = NULL;", "Clear dangling pointer to NULL")
            ),
            initialMemory = listOf(
                MemoryCell("0x7ffd5000", "heapArr", "int*", "NULL", 8, memoryType = MemoryType.STACK)
            ),
            initialStack = listOf(
                StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("heapArr=NULL"))
            ),
            steps = listOf(
                SimStep(
                    lineIndex = 0,
                    activeStatement = "malloc(2 * sizeof(int))",
                    statusText = "Heap allocator reserves 8 bytes at Heap Address 0x00a01000",
                    stdout = "Heap: malloc(8 bytes) -> returned 0x00a01000\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd5000", "heapArr", "int*", "0x00a01000", 8, memoryType = MemoryType.STACK, pointsToAddress = "0x00a01000"),
                        MemoryCell("0x00a01000", "heapArr[0]", "int", "??", 4, memoryType = MemoryType.HEAP, isHighlighted = true),
                        MemoryCell("0x00a01004", "heapArr[1]", "int", "??", 4, memoryType = MemoryType.HEAP, isHighlighted = true)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("heapArr=0x00a01000"))),
                    activePointerSource = "0x7ffd5000",
                    activePointerTarget = "0x00a01000"
                ),
                SimStep(
                    lineIndex = 2,
                    activeStatement = "heapArr[0] = 100; heapArr[1] = 200;",
                    statusText = "Populating heap cells with values 100 and 200",
                    stdout = "Wrote 100 and 200 to Heap memory buffer.\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd5000", "heapArr", "int*", "0x00a01000", 8, memoryType = MemoryType.STACK, pointsToAddress = "0x00a01000"),
                        MemoryCell("0x00a01000", "heapArr[0]", "int", "100", 4, memoryType = MemoryType.HEAP, isHighlighted = true),
                        MemoryCell("0x00a01004", "heapArr[1]", "int", "200", 4, memoryType = MemoryType.HEAP, isHighlighted = true)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("heapArr=0x00a01000"))),
                    activePointerSource = "0x7ffd5000",
                    activePointerTarget = "0x00a01000"
                ),
                SimStep(
                    lineIndex = 4,
                    activeStatement = "free(heapArr);",
                    statusText = "Releasing Heap memory! Blocks returned to free list.",
                    stdout = "Heap: free(0x00a01000) called. Leak averted!\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd5000", "heapArr", "int*", "0x00a01000 (dangling!)", 8, memoryType = MemoryType.STACK, isCorrupted = true),
                        MemoryCell("0x00a01000", "heapArr[0]", "int", "[FREED]", 4, memoryType = MemoryType.HEAP, isFreed = true),
                        MemoryCell("0x00a01004", "heapArr[1]", "int", "[FREED]", 4, memoryType = MemoryType.HEAP, isFreed = true)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("heapArr=dangling"))),
                    isHazard = false
                ),
                SimStep(
                    lineIndex = 5,
                    activeStatement = "heapArr = NULL;",
                    statusText = "Dangling pointer set to NULL. Safe practice complete.",
                    stdout = "heapArr = NULL. Heap clean.\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd5000", "heapArr", "int*", "NULL", 8, memoryType = MemoryType.STACK, isHighlighted = true),
                        MemoryCell("0x00a01000", "heapArr[0]", "int", "[FREED]", 4, memoryType = MemoryType.HEAP, isFreed = true),
                        MemoryCell("0x00a01004", "heapArr[1]", "int", "[FREED]", 4, memoryType = MemoryType.HEAP, isFreed = true)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("heapArr=NULL")))
                )
            ),
            challengeQuestion = "What happens if you allocate memory with malloc() but never call free()?",
            challengeOptions = listOf("Compiler warning", "Automatic garbage collection", "Memory Leak", "Segmentation fault immediately"),
            correctOptionIndex = 2,
            challengeHint = "C does not have a garbage collector. Failing to free dynamic memory consumes RAM indefinitely (Memory Leak).",
            cSnippetSnippet = "int *p = malloc(100);\n// missing free(p) causes a leak!",
            targetPointer = "0x00a01000",
            defaultSize = "8 bytes"
        ),

        // LEVEL 9: Structs & Linked Nodes
        Level(
            id = 9,
            tier = Tier.ADVANCED,
            title = "Level 9: Structs & Nodes",
            subtitle = "The Arrow Operator (->)",
            conceptTitle = "Structures & Linked Node Chains",
            conceptExplanation = "A `struct` aggregates multiple variables into a single compound type. When accessing fields through a struct pointer, the arrow operator `ptr->field` is syntactic sugar for `(*ptr).field`.",
            learningObjectives = listOf(
                "Define custom C data structures with struct",
                "Link structs via self-referential pointers (struct Node*)",
                "Traverse linked elements using the arrow -> operator"
            ),
            sourceCode = listOf(
                CodeLine(1, "struct Node { int data; struct Node *next; };", "Self-referential struct"),
                CodeLine(2, "struct Node n2 = { 200, NULL };", "Tail node in chain"),
                CodeLine(3, "struct Node n1 = { 100, &n2 };", "Head node pointing to n2"),
                CodeLine(4, "struct Node *curr = &n1;", "Pointer to head"),
                CodeLine(5, "printf(\"Chain: %d -> %d\\n\", curr->data, curr->next->data);", "Traversing chain with ->")
            ),
            initialMemory = listOf(
                MemoryCell("0x7ffd6000", "n2.data", "int", "200", 4),
                MemoryCell("0x7ffd6008", "n2.next", "Node*", "NULL", 8),
                MemoryCell("0x7ffd6010", "n1.data", "int", "100", 4),
                MemoryCell("0x7ffd6018", "n1.next", "Node*", "0x7ffd6000", 8, pointsToAddress = "0x7ffd6000")
            ),
            initialStack = listOf(
                StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("n1", "n2", "curr"))
            ),
            steps = listOf(
                SimStep(
                    lineIndex = 1,
                    activeStatement = "struct Node n2 = { 200, NULL };",
                    statusText = "Allocating Node n2 at 0x7ffd6000 with next = NULL",
                    stdout = "Initializing linked nodes...\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd6000", "n2.data", "int", "200", 4, isHighlighted = true),
                        MemoryCell("0x7ffd6008", "n2.next", "Node*", "NULL", 8)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("n2={200,NULL}")))
                ),
                SimStep(
                    lineIndex = 2,
                    activeStatement = "struct Node n1 = { 100, &n2 };",
                    statusText = "Linking n1.next to &n2 (0x7ffd6000) -> Pointer wire established!",
                    stdout = "Node n1 linked to n2 via pointer address.\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd6000", "n2.data", "int", "200", 4),
                        MemoryCell("0x7ffd6008", "n2.next", "Node*", "NULL", 8),
                        MemoryCell("0x7ffd6010", "n1.data", "int", "100", 4, isHighlighted = true),
                        MemoryCell("0x7ffd6018", "n1.next", "Node*", "0x7ffd6000", 8, pointsToAddress = "0x7ffd6000", isHighlighted = true)
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("n2", "n1={100,&n2}"))),
                    activePointerSource = "0x7ffd6018",
                    activePointerTarget = "0x7ffd6000"
                ),
                SimStep(
                    lineIndex = 4,
                    activeStatement = "printf(\"Chain: %d -> %d\\n\", curr->data, curr->next->data);",
                    statusText = "Traversing linked nodes: curr->data (100) -> curr->next->data (200)",
                    stdout = "Chain: 100 -> 200\n",
                    memorySnapshot = listOf(
                        MemoryCell("0x7ffd6000", "n2.data", "int", "200", 4),
                        MemoryCell("0x7ffd6008", "n2.next", "Node*", "NULL", 8),
                        MemoryCell("0x7ffd6010", "n1.data", "int", "100", 4),
                        MemoryCell("0x7ffd6018", "n1.next", "Node*", "0x7ffd6000", 8, pointsToAddress = "0x7ffd6000")
                    ),
                    stackSnapshot = listOf(StackFrame("f_main", "main()", "0x00400000", listOf(), listOf("curr=&n1"))),
                    activePointerSource = "0x7ffd6018",
                    activePointerTarget = "0x7ffd6000"
                )
            ),
            challengeQuestion = "If 'ptr' is a pointer to a struct, which expression is equivalent to ptr->val?",
            challengeOptions = listOf("(*ptr).val", "*ptr.val", "ptr.val", "&ptr.val"),
            correctOptionIndex = 0,
            challengeHint = "The arrow operator ptr->val dereferences the pointer first then accesses the member: (*ptr).val.",
            cSnippetSnippet = "struct Point *p = &pt;\np->x = 10; // same as (*p).x = 10",
            targetPointer = "0x7ffd6018",
            defaultSize = "16 bytes"
        )
    )
}
