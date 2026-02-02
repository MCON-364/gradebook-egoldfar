package edu.course.gradebook;

import java.util.Scanner;

/**
 * Main entry point for the Gradebook CLI application.
 *
 * This class demonstrates:
 * - var for local variable inference
 * - labeled while loop (changed from for loop per requirements)
 * - enhanced switch expression for command processing
 * - Integration with Gradebook methods
 */
public class Main {
    private final Gradebook gradebook;

    public Main() {
        this.gradebook = new Gradebook();
    }

    public static void main(String[] args) {
        var app = new Main();
        app.run();
    }

    public void run() {
        var scanner = new Scanner(System.in);

        mainLoop:
        while (true) {
            System.out.print("> ");
            var line = scanner.nextLine();
            var cmd = Commands.parse(line);

            // Print numeric ordinal value of the command
            System.out.println("[" + cmd.ordinal() + "] " + cmd);

            if (cmd == Command.EXIT) {
                break mainLoop;
            }

            if (cmd == Command.UNKNOWN) {
                System.out.println("Unknown command. Try: ADD_STUDENT, ADD_GRADE, REMOVE_STUDENT, REPORT, CLASS_REPORT, UNDO, LOG, EXIT");
                continue mainLoop;
            }

            // Enhanced switch expression to invoke correct Gradebook method
            var result = switch (cmd) {
                case ADD_STUDENT -> handleAddStudent(line);
                case ADD_GRADE -> handleAddGrade(line);
                case REMOVE_STUDENT -> handleRemoveStudent(line);
                case REPORT -> handleReport(line);
                case CLASS_REPORT -> handleClassReport();
                case UNDO -> handleUndo();
                case LOG -> handleLog();
                default -> {
                    System.out.println("Command not handled: " + cmd);
                    yield false;
                }
            };
        }

        scanner.close();
        System.out.println("Goodbye!");
    }

    private boolean handleAddStudent(String line) {
        var parts = line.trim().split("\\s+", 2);
        if (parts.length < 2) {
            System.out.println("Usage: ADD_STUDENT <name>");
            return false;
        }
        var success = gradebook.addStudent(parts[1]);
        if (success) {
            System.out.println("Added student: " + parts[1]);
        } else {
            System.out.println("Student already exists: " + parts[1]);
        }
        return success;
    }

    private boolean handleAddGrade(String line) {
        var parts = line.trim().split("\\s+", 3);
        if (parts.length < 3) {
            System.out.println("Usage: ADD_GRADE <name> <grade>");
            return false;
        }
        try {
            var grade = Integer.parseInt(parts[2]);
            if (grade < 0 || grade > 100) {
                System.out.println("Grade must be between 0 and 100");
                return false;
            }
            var success = gradebook.addGrade(parts[1], grade);
            if (success) {
                System.out.println("Added grade " + grade + " for " + parts[1]);
            } else {
                System.out.println("Student not found: " + parts[1]);
            }
            return success;
        } catch (NumberFormatException e) {
            System.out.println("Invalid grade: " + parts[2]);
            return false;
        }
    }

    private boolean handleRemoveStudent(String line) {
        var parts = line.trim().split("\\s+", 2);
        if (parts.length < 2) {
            System.out.println("Usage: REMOVE_STUDENT <name>");
            return false;
        }
        var success = gradebook.removeStudent(parts[1]);
        if (success) {
            System.out.println("Removed student: " + parts[1]);
        } else {
            System.out.println("Student not found: " + parts[1]);
        }
        return success;
    }

    private boolean handleReport(String line) {
        var parts = line.trim().split("\\s+", 2);
        if (parts.length < 2) {
            System.out.println("Usage: REPORT <name>");
            return false;
        }
        var studentName = parts[1];
        var gradesOpt = gradebook.findStudentGrades(studentName);
        if (gradesOpt.isEmpty()) {
            System.out.println("Student not found: " + studentName);
            return false;
        }
        var grades = gradesOpt.get();
        System.out.println("Report for " + studentName + ":");
        System.out.println("  Grades: " + grades);

        var avgOpt = gradebook.averageFor(studentName);
        var letterOpt = gradebook.letterGradeFor(studentName);

        if (avgOpt.isPresent()) {
            System.out.printf("  Average: %.2f%n", avgOpt.get());
        } else {
            System.out.println("  Average: N/A (no grades)");
        }

        if (letterOpt.isPresent()) {
            System.out.println("  Letter Grade: " + letterOpt.get());
        } else {
            System.out.println("  Letter Grade: N/A");
        }
        return true;
    }

    private boolean handleClassReport() {
        var avgOpt = gradebook.classAverage();
        if (avgOpt.isPresent()) {
            System.out.printf("Class Average: %.2f%n", avgOpt.get());
        } else {
            System.out.println("Class Average: N/A (no grades in system)");
        }
        return avgOpt.isPresent();
    }

    private boolean handleUndo() {
        var success = gradebook.undo();
        if (success) {
            System.out.println("Undo successful");
        } else {
            System.out.println("Nothing to undo");
        }
        return success;
    }

    private boolean handleLog() {
        var logs = gradebook.recentLog(10);
        if (logs.isEmpty()) {
            System.out.println("No activity logged yet");
        } else {
            System.out.println("Recent activity:");
            for (var entry : logs) {
                System.out.println("  - " + entry);
            }
        }
        return !logs.isEmpty();
    }
}
