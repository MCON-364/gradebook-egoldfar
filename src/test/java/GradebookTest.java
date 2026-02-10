import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import edu.course.gradebook.Gradebook;

public class GradebookTest {

    private Gradebook gradebook;

    @BeforeEach
    public void setup() {
        gradebook = new Gradebook();
    }

    @Test
    public void testAddStudent() {
        assertTrue(gradebook.addStudent("Alice"));
        assertFalse(gradebook.addStudent("Alice"));
    }

    @Test
    public void testAddStudentWithGrades() {
        var grades = new ArrayList<Integer>();
        grades.add(80);
        grades.add(90);
        assertTrue(gradebook.addStudent("Bob", grades));
        assertTrue(gradebook.findStudentGrades("Bob").isPresent());
        assertEquals(2, gradebook.findStudentGrades("Bob").get().size());
        assertFalse(gradebook.addStudent("Bob", grades));
    }

    @Test
    public void testAddGrade() {
        assertFalse(gradebook.addGrade("Bob", 90));
        gradebook.addStudent("Bob");
        assertTrue(gradebook.addGrade("Bob", 90));
    }

    @Test
    public void testRemoveLastGrade() {
        assertEquals(-1, gradebook.removeLastGrade("Charlie"));
        gradebook.addStudent("Charlie");
        assertEquals(-1, gradebook.removeLastGrade("Charlie"));
        gradebook.addGrade("Charlie", 85);
        assertEquals(85, gradebook.removeLastGrade("Charlie"));
    }

    @Test
    public void testRemoveStudent() {
        assertFalse(gradebook.removeStudent("David"));
        gradebook.addStudent("David");
        assertTrue(gradebook.removeStudent("David"));
    }

    @Test
    public void testFindStudentGrades() {
        assertTrue(gradebook.findStudentGrades("Eve").isEmpty());
        gradebook.addStudent("Eve");
        assertTrue(gradebook.findStudentGrades("Eve").get().isEmpty());
        gradebook.addGrade("Eve", 95);
        assertTrue(gradebook.findStudentGrades("Eve").isPresent());
        assertEquals(1, gradebook.findStudentGrades("Eve").get().size());
        assertEquals(95, gradebook.findStudentGrades("Eve").get().get(0));
    }

    @Test
    public void testRecentLog() {
        gradebook.addStudent("Frank");
        gradebook.addGrade("Frank", 88);
        gradebook.removeLastGrade("Frank");
        gradebook.removeStudent("Frank");

        var log = gradebook.recentLog(3);
        assertEquals(3, log.size());
        assertEquals("Removed student Frank there were no grades currently recorded", log.get(0));
        assertEquals("Removed grade 88 for student Frank", log.get(1));
        assertEquals("Added grade 88 for student Frank", log.get(2));
    }

    @Test
    public void testRecentLogWithFewerEntries() {
        gradebook.addStudent("Grace");

        var log = gradebook.recentLog(5);
        assertEquals(1, log.size());
        assertEquals("Added student Grace", log.get(0));
    }

    @Test
    public void testAverageFor() {
        assertTrue(gradebook.averageFor("Grace").isEmpty());
        gradebook.addStudent("Grace");
        assertTrue(gradebook.averageFor("Grace").isEmpty());
        gradebook.addGrade("Grace", 80);
        gradebook.addGrade("Grace", 90);
        assertTrue(gradebook.averageFor("Grace").isPresent());
        assertEquals(85.0, gradebook.averageFor("Grace").get());
    }

    @Test
    public void testLetterGradeFor() {
        assertTrue(gradebook.letterGradeFor("Hank").isEmpty());
        gradebook.addStudent("Hank");
        assertTrue(gradebook.letterGradeFor("Hank").isEmpty());
        gradebook.addGrade("Hank", 95);
        assertTrue(gradebook.letterGradeFor("Hank").isPresent());
        assertEquals("A", gradebook.letterGradeFor("Hank").get());
    }

    @Test
    public void testUndoAddGrade() {
        gradebook.addStudent("Ivy");
        gradebook.addGrade("Ivy", 85);
        assertTrue(gradebook.undo());
        assertTrue(gradebook.findStudentGrades("Ivy").get().isEmpty());
    }

    @Test
    public void testUndoRemoveStudentWithGrades() {
        gradebook.addStudent("Jack");
        gradebook.addGrade("Jack", 75);
        gradebook.removeStudent("Jack");
        assertTrue(gradebook.undo());
        assertTrue(gradebook.findStudentGrades("Jack").isPresent());
        assertEquals(1, gradebook.findStudentGrades("Jack").get().size());
        assertEquals(75, gradebook.findStudentGrades("Jack").get().get(0));
    }

    @Test
    public void testUndoRemoveStudentWithoutGrades() {
        gradebook.addStudent("Karen");
        gradebook.removeStudent("Karen");
        assertTrue(gradebook.undo());
        assertTrue(gradebook.findStudentGrades("Karen").isPresent());
        assertTrue(gradebook.findStudentGrades("Karen").get().isEmpty());
    }

    @Test
    public void testUndoNoActions() {
        assertFalse(gradebook.undo());
        // No exception should be thrown
    }

    @Test
    public void testMultipleUndos() {
        gradebook.addStudent("Leo");
        gradebook.addGrade("Leo", 70);
        gradebook.addGrade("Leo", 80);
        gradebook.removeStudent("Leo");

        assertTrue(gradebook.undo()); // Undo remove student
        assertTrue(gradebook.findStudentGrades("Leo").isPresent());
        assertEquals(2, gradebook.findStudentGrades("Leo").get().size());
        assertEquals(70, gradebook.findStudentGrades("Leo").get().get(0));

        assertTrue(gradebook.undo()); // Undo add second grade
        assertTrue(gradebook.findStudentGrades("Leo").isPresent());
        assertEquals(1, gradebook.findStudentGrades("Leo").get().size());

        assertTrue(gradebook.undo()); // Undo add first grade
        assertTrue(gradebook.findStudentGrades("Leo").isPresent());
        assertTrue(gradebook.findStudentGrades("Leo").get().isEmpty());

        assertFalse(gradebook.undo()); // No more actions to undo
    }

    @Test
    public void testClassAverage() {
        gradebook.addStudent("Mia");
        gradebook.addGrade("Mia", 90);
        gradebook.addStudent("Nina");
        gradebook.addGrade("Nina", 80);
        gradebook.addGrade("Nina", 85);
        assertTrue(gradebook.classAverage().isPresent());
        assertEquals(86.25, gradebook.classAverage().get());
    }
}   
