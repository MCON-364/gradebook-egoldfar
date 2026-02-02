# Homework: Gradebook CLI (Java 21)

## Overview

In this assignment, you will build a **command-line Gradebook application** using **Java 21**.  
The goal is to practice **modern Java language features**, **core collections**, and **clean control flow**, while writing code that is testable and maintainable.

This is **not** a GUI application. All interaction happens via standard input/output (the terminal).

You are provided with a **minimal Maven starter project** and a small set of **unit tests** that will be used for autograding.

---

## Learning Objectives

By completing this assignment, you should demonstrate the ability to:

- Use Java collections appropriately (`HashMap`, `ArrayList`, `Deque`, `LinkedList`)
- Apply **Java 21 language features** in real code
- Use `Optional` correctly instead of `null`
- Control program flow using **labeled loops**
- Write logic that is **testable without relying on console output**
- Work with Maven, JUnit 5, and GitHub Classroom autograding

---

## Functional Requirements

You will implement a **Gradebook manager** that supports the following commands.

Commands are entered one per line and are processed in a loop until the user exits.

### Required Commands

| Command | Description |
|------|------------|
| `ADD_STUDENT <name>` | Adds a new student with no grades |
| `ADD_GRADE <name> <0–100>` | Adds a grade for an existing student |
| `REMOVE_STUDENT <name>` | Removes a student and all of their grades |
| `REPORT <name>` | Prints grades, average, and letter grade |
| `CLASS_REPORT` | Prints the class average across all grades |
| `UNDO` | Undoes the last state-changing operation |
| `LOG` | Prints recent actions |
| `EXIT` | Exits the program |

Commands may be entered in any order. Invalid commands should **not crash** the program.

---

## Data Structures (Required)

Your implementation **must** use the following collections:

- `HashMap<String, List<Integer>>`
    - Maps student names to their list of grades
- `ArrayList<Integer>`
    - Stores grades for a single student
- `Deque<UndoAction>`
    - Used as an **undo stack**
- `LinkedList<String>`
    - Stores an activity log or command history

Using other collections is allowed, but these **must** appear in your implementation.

---

## Required Java Language Features

Your solution **must explicitly demonstrate** all of the following:

### 1. `Optional`
- All student lookups must return `Optional`
- Do **not** return `null` for missing students

Example expectation:
```java
Optional<List<Integer>> findStudentGrades(String name);
```

### 2. `var`
- Use `var` for local variable inference where the type is obvious
- Do **not** replace all types with `var`; use it selectively

### 3. Labeled Loop
You must use:
- a labeled loop (either `for` or `while`)
- at least one labeled `break` or labeled `continue`

This is required in the main command-processing loop (see `Main.java`).

**Note:** The starter code uses a labeled `while` loop. Both `for` and `while` loops can be labeled in Java.

### 4. Enhanced `for` Loop
- Use enhanced `for` loops when iterating over collections
- Required for computing averages

### 5. Switch Expression with `yield`
You must use a switch expression (not a statement) with `yield`.

This is required for letter-grade calculation, for example:

| Average | Letter |
|---------|--------|
| 90–100  | A      |
| 80–89   | B      |
| 70–79   | C      |
| 60–69   | D      |
| < 60    | F      |

---

## Undo Behavior

The following operations must be undoable:
- `ADD_GRADE`
- `REMOVE_STUDENT`

Undo should restore the previous state exactly.

**Hints:**
- Use the provided `UndoAction` functional interface
- Push undo operations onto a `Deque`
- `UNDO` should remove and execute the most recent action

---

## Logging

Every state-changing operation should add a human-readable entry to the log.

**Example log entries:**
```
Added student Alice
Added grade 90 for Alice
Removed student Bob
```

The `LOG` command should print recent entries from the `LinkedList`.

---

### What's Already Implemented

#### `Main.java` - Complete Reference Implementation
The `Main` class is **fully implemented** and demonstrates:
- **Labeled `while` loop** with `break mainLoop` and `continue mainLoop`
- **Switch expression** that delegates to handler methods
- **Command parsing** via `Commands.parse()`
- **Helper methods** for each command (e.g., `handleAddStudent()`, `handleAddGrade()`)
- **Usage of `var`** throughout for local variable inference
- **Enhanced `for` loop** in `handleLog()` method
- **`Optional` handling** for student lookups and averages
- **Input validation** and user-friendly error messages

**Key implementation details:**
- The main loop prints the command's ordinal value: `[0] ADD_STUDENT`
- Each command is delegated to a private helper method
- Helper methods return `boolean` to indicate success/failure
- The switch expression uses these return values

#### `Commands.java` - Fully Implemented Parser
The `Commands.parse()` method is **complete**:
- Extracts the first word from input
- Converts to uppercase for case-insensitive matching
- Uses a switch expression to return the correct `Command` enum
- Returns `Command.UNKNOWN` for invalid input

### What You Need to Implement

You must implement the following methods in **`Gradebook.java`** and add unit tests for them.

1. **`addStudent(String name)`**
   - Add a student to the `gradesByStudent` map
   - Initialize with an empty `ArrayList<Integer>`
   - Return `false` if student already exists
   - Add a log entry
   - Return `true` on success

2. **`addGrade(String name, int grade)`**
   - Find the student's grade list
   - Add the grade to their list
   - Push an undo action to `undoStack` (to remove this grade)
   - Add a log entry
   - Return `false` if student not found
   - Return `true` on success

3. **`removeStudent(String name)`**
   - Remove the student from `gradesByStudent`
   - Push an undo action to `undoStack` (to restore the student)
   - Add a log entry
   - Return `false` if student not found
   - Return `true` on success

4. **`averageFor(String name)`**
   - Use an **enhanced `for` loop** to sum grades
   - Calculate and return the average as `Optional<Double>`
   - Return `Optional.empty()` if no grades exist

5. **`letterGradeFor(String name)`**
   - Get the average for the student
   - Use a **switch expression with `yield`** to convert average to letter grade:
     - 90–100 → A
     - 80–89 → B
     - 70–79 → C
     - 60–69 → D
     - < 60 → F
   - Return `Optional<String>` with the letter grade
   - Return `Optional.empty()` if student has no grades

6. **`classAverage()`**
   - Calculate average across **all** grades for **all** students
   - Use **enhanced `for` loops** to iterate over students and their grades
   - Return `Optional<Double>`
   - Return `Optional.empty()` if no grades exist in the system

7. **`undo()`**
   - Pop the most recent `UndoAction` from `undoStack`
   - Execute it (call its `undo()` method)
   - Add a log entry
   - Return `false` if stack is empty
   - Return `true` on success

8. **`recentLog(int maxItems)`**
   - Return the most recent `maxItems` entries from `activityLog`
   - Use a `LinkedList` sublist or iterator
   - Return as `List<String>`

### Important Notes

- The `Main` class is **provided as a reference** - you don't need to modify it
- Focus your implementation efforts on `Gradebook.java`
- The `Commands.parse()` method is complete - no changes needed
- All tests will call `Gradebook` methods directly, not through `Main`


### GitHub Classroom

Autograding will:
1. Inject `GradebookTest.java`
2. Verify tests exist
3. Run `mvn test`

Your grade is based on test results only.

---

## Submission Rules

- Push your solution to GitHub Classroom
- Do **not** remove required language features
- Code must compile under Java 21
- Do **not** hard-code values to satisfy tests

---

## Tips

- Keep `Gradebook` logic independent of `System.in` / `System.out`
- Normalize student names consistently
- Prefer small helper methods over large methods
- If a method feels hard to test, it probably needs refactoring
- **Study the `Main.java` implementation** to understand how your `Gradebook` methods will be called
- The switch expression in `letterGradeFor()` is where you demonstrate the `yield` keyword

---

## Academic Integrity

This assignment is designed to test your understanding of Java, not your ability to generate code automatically.

**You may:**
- Use official Java documentation
- Review lecture examples

**You may not:**
- Copy solutions from classmates
- Submit AI-generated code
