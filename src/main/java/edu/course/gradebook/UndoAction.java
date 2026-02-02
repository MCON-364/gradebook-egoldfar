package edu.course.gradebook;

@FunctionalInterface
public interface UndoAction {
    void undo(Gradebook gradebook);
}
