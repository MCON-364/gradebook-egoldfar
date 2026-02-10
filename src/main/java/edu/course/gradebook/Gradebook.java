package edu.course.gradebook;

import java.lang.reflect.Array;
import java.util.*;

public class Gradebook {

    private final Map<String, List<Integer>> gradesByStudent = new HashMap<>();
    private final Deque<UndoAction> undoStack = new ArrayDeque<>();
    private final LinkedList<String> activityLog = new LinkedList<>();

    public Optional<List<Integer>> findStudentGrades(String name) {
        return Optional.ofNullable(gradesByStudent.get(name));
    }

    public boolean addStudent(String name) {
        var result = gradesByStudent.putIfAbsent(name, new ArrayList<Integer>());
        if (result == null) {
            activityLog.addFirst("Added student " + name);
            return true;
        }
        return false;
    }

    public boolean addStudent(String name, ArrayList<Integer> grades) {
        var result = gradesByStudent.putIfAbsent(name, grades);
        if (result == null) {
            activityLog.addFirst("Added student " + name + " with grades " + grades);
            return true;
        }
        return false;
    }

    public boolean addGrade(String name, int grade) {
        var grades = gradesByStudent.get(name);
        if (grades == null) {
            return false;
        }
        grades.add(grade);
        activityLog.addFirst("Added grade " + grade + " for student " + name);
        undoStack.push(g -> g.removeLastGrade(name));
        return true;
    }

    public int removeLastGrade(String name) {
        try {
            int grade = gradesByStudent.get(name).removeLast();
            activityLog.addFirst("Removed grade " + grade + " for student " + name);
            return grade;
        } catch (NoSuchElementException e) {
            activityLog.addFirst("No grade for student " + name);
            return -1;
        } catch (NullPointerException e) {
            activityLog.addFirst(name + " is not in the student list");
            return -1;
        }
    }

    public boolean removeStudent(String name) {
        ArrayList<Integer> grades =  (ArrayList<Integer>) gradesByStudent.get(name);
        if (grades == null) {
            return false;
        }
        gradesByStudent.remove(name);
        if (grades.isEmpty()) {
            activityLog.addFirst("Removed student " + name + " there were no grades currently recorded");
            undoStack.push(g -> g.addStudent(name));
        } else {
            activityLog.addFirst("Removed student " + name + " with grades " + grades);
            undoStack.push(g -> g.addStudent(name, grades));
        }
        return true;
    }

    public Optional<Double> averageFor(String name) {
        var grades = gradesByStudent.get(name);
        if (grades == null) {
            return Optional.empty();
        } else if (grades.isEmpty()) {
            return Optional.empty();
        } else {
            var average = 0.0;
            for (int grade : grades) {
                average += grade;
            }
            average /= grades.size();
            return Optional.of(average);
        }
    }

    public Optional<String> letterGradeFor(String name) {
        var averageObject = averageFor(name);
        if (!averageObject.isPresent()) {
            return Optional.empty();
        }
        var average =  averageObject.get();
        return switch ((int) average.doubleValue()/10) {
                case 9,10 -> { activityLog.addFirst(name + "'s letter grade is an A");
                    yield Optional.of("A"); }
                case 8 -> { activityLog.addFirst(name + "'s letter grade is a B");
                    yield Optional.of("B"); }
                case 7 -> { activityLog.addFirst(name + "'s letter grade is a C");
                    yield Optional.of("C"); }
                case 6 -> { activityLog.addFirst(name + "'s letter grade is a D");
                    yield Optional.of("D"); }
                default -> { activityLog.addFirst(name + " has failed");
                     yield Optional.of("F"); }
            };
    }

    public Optional<Double> classAverage() {
        if (gradesByStudent.isEmpty()) {
            return Optional.empty();
        }
        double classAverage = 0.0;
        var students = gradesByStudent.keySet();
        var numStudents = 0;
        for (var student : students) {
            classAverage += averageFor(student).isPresent()?  averageFor(student).get() : 0.0;
            if (averageFor(student).isPresent()) {
                numStudents++;
            }
        }
        if (classAverage == 0.0) {
            return Optional.empty();
        }
        return Optional.of(classAverage/numStudents);
    }

    public boolean undo() {
        try {
            undoStack.pop().undo(this);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public List<String> recentLog(int maxItems) {
        List<String> log = new ArrayList<>();
        for (int i = 0; i < maxItems; i++) {
            try {
                log.add(activityLog.get(i));
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        return log;
    }
}
