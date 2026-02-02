package edu.course.gradebook;

import java.util.*;

public class Gradebook {

    private final Map<String, List<Integer>> gradesByStudent = new HashMap<>();
    private final Deque<UndoAction> undoStack = new ArrayDeque<>();
    private final LinkedList<String> activityLog = new LinkedList<>();

    public Optional<List<Integer>> findStudentGrades(String name) {
        return Optional.ofNullable(gradesByStudent.get(name));
    }

    public boolean addStudent(String name) {
        throw new UnsupportedOperationException();
    }

    public boolean addGrade(String name, int grade) {
        throw new UnsupportedOperationException();
    }

    public boolean removeStudent(String name) {
        throw new UnsupportedOperationException();
    }

    public Optional<Double> averageFor(String name) {
        throw new UnsupportedOperationException();
    }

    public Optional<String> letterGradeFor(String name) {
        throw new UnsupportedOperationException();
    }

    public Optional<Double> classAverage() {
        throw new UnsupportedOperationException();
    }

    public boolean undo() {
        throw new UnsupportedOperationException();
    }

    public List<String> recentLog(int maxItems) {
        throw new UnsupportedOperationException();
    }
}
