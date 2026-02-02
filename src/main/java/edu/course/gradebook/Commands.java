package edu.course.gradebook;

/**
 * Utility class for parsing user input into Command enum values.
 *
 * This class is responsible for converting raw command-line strings
 * into structured Command enum values that can be processed by the application.
 */
public final class Commands {
    private Commands() {}

    /**
     * Parses a command line string and returns the corresponding Command enum value.
     *
     * Instructions:
     * - Extracts the command word from the input string (the first word)
     * - Matches it against the known command names (ADD_STUDENT, ADD_GRADE, etc.)
     * - Returns the appropriate Command enum value
     * - Returns Command.UNKNOWN for any unrecognized input
     *
     * Examples of expected input:
     * - "ADD_STUDENT Alice" returns Command.ADD_STUDENT
     * - "EXIT" returns Command.EXIT
     * - "INVALID" returns Command.UNKNOWN
     * - "" (blank) returns Command.UNKNOWN
     *
     * Note: This method only identifies WHICH command was entered.
     * Parsing the arguments (student names, grades, etc.) is handled elsewhere.
     *
     * @param line the raw input line from the user
     * @return the Command enum value corresponding to the input, or UNKNOWN if not recognized
     */
    public static Command parse(String line) {
        if (line == null || line.isBlank()) return Command.UNKNOWN;

        var firstWord = line.trim().split("\\s+")[0].toUpperCase();

        return switch (firstWord) {
            case "ADD_STUDENT" -> Command.ADD_STUDENT;
            case "ADD_GRADE" -> Command.ADD_GRADE;
            case "REMOVE_STUDENT" -> Command.REMOVE_STUDENT;
            case "REPORT" -> Command.REPORT;
            case "CLASS_REPORT" -> Command.CLASS_REPORT;
            case "UNDO" -> Command.UNDO;
            case "LOG" -> Command.LOG;
            case "EXIT" -> Command.EXIT;
            default -> Command.UNKNOWN;
        };
    }
}
