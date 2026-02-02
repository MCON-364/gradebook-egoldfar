package edu.course.gradebook;

/**
 * Represents the different commands supported by the Gradebook CLI.
 *
 * Each command corresponds to an operation the user can perform.
 * See README.md for the full list of commands and their expected behavior.
 *
 * UNKNOWN should be returned when a command cannot be recognized.
 */
public enum Command {
    ADD_STUDENT,      // Add a new student with no grades
    ADD_GRADE,        // Add a grade to an existing student
    REMOVE_STUDENT,   // Remove a student and all their grades
    REPORT,           // Display a student's grades, average, and letter grade
    CLASS_REPORT,     // Display the class average across all students
    UNDO,             // Undo the last state-changing operation
    LOG,              // Display the activity log
    EXIT,             // Exit the program
    UNKNOWN           // Represents an invalid or unrecognized command
}
