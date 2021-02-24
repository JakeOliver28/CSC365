import java.util.*;
import java.io.*;

/**
 * @author Cory Baxes, Jacob Oliver, Monica Chavez
 * @version 1/8/2018
 */
public class StudentSearch
{
    public static void findStudent(ArrayList<Student> students, String lname)
    {
        boolean studentExists = false;
        for (Student s : students)
        {
            if (s.lName.equals(lname))
            {
                studentExists = true;
                System.out.println(s.lName + " " + s.fName + " " + s.grade + " " + s.classroom + " " + s.TfName + " " + s.TlName);
            }
        }
        if (!studentExists)
            System.out.println();
    }
    
    public static void findStudentBus(ArrayList<Student> students, String lname)
    {
        boolean studentExists = false;
        for (Student s : students)
        {
            if (s.lName.equals(lname))
            {
                studentExists = true;
                System.out.println(s.lName + " " + s.fName + " " + s.bus);
            }
        }
        if (!studentExists)
            System.out.println();
    }

    public static void findStudentByBus(ArrayList<Student> students, int bus) {
        boolean studentExists = false;
        for (Student student : students) {
            if (student.bus == bus) {
                studentExists = true;
                System.out.println(student.fName + " " + student.lName + " " +
                    student.grade + " " + student.classroom);
            }
        }
        if (!studentExists)
            System.out.println();
    }

    public static void findStudentByGrade(ArrayList<Student> students, int grade) {
        boolean studentExists = false;
        for (Student student : students) {
            if (student.grade == grade) {
                studentExists = true;
                System.out.println(student.lName + " " + student.fName);
            }
        }
        if (!studentExists)
            System.out.println();
    }

    private static Student findStudentWithHighestGPA(ArrayList<Student> students) {
            Student highestGPA = students.get(0);
        for (Student s : students) {
            if (s.gpa > highestGPA.gpa) {
                highestGPA = s;
            }
        }
        return highestGPA;
    }

    private static Student findStudentWithLowestGPA(ArrayList<Student> students) {
        Student lowestGPA = students.get(0);
        for (Student s : students) {
            if (s.gpa < lowestGPA.gpa) {
                lowestGPA = s;
            }
        }
        return lowestGPA;
    }

    public static void findStudentByGradeAndGPA(ArrayList<Student> students, int grade, char order)
    {
        ArrayList<Student> studentsGivenGrade = new ArrayList<>();
        for (Student student : students) {
            if (student.grade == grade) {
                studentsGivenGrade.add(student);
            }
        }

        Student s;
        // if statement added to prevent IOBException if no students are in grade
        if (studentsGivenGrade.size() == 0){
            System.out.print("\n");
            return;
        }
        else if (order == 'H') {
            s = findStudentWithHighestGPA(studentsGivenGrade);
        } else { // order == 'L'
            s = findStudentWithLowestGPA(studentsGivenGrade);
        }
        System.out.println(s.lName + " " + s.fName + " " +
            s.gpa + " " + s.TlName + " " + s.TfName + " " + s.bus);
        //System.out.println();
    }

    public static void findStudentsGivenClassroom(int classroom, ArrayList<Student> students) {
      boolean classEmpty = true;

        for (Student s : students) {
            if (s.classroom == classroom) {
               classEmpty = false;
                System.out.println(s.lName + " " + s.fName + " " + s.grade + " " + s.classroom + " "
                    + s.bus + " " + s.gpa + " " + s.TlName + " " + s.TfName);
            }
        }
        if (classEmpty)
            System.out.println();
    }

    /*Given a classroom number, find the teacher (or teachers) teaching in it.*/
    public static void findTeacherGivenClassroom(int classroom, ArrayList<Teacher> teachers) {
        boolean classEmpty = true;

        for (Teacher t : teachers) {
            if (t.classroom == classroom) {
                classEmpty = false;
                System.out.println(t.lName + " " + t.fName + " " + t.classroom);
            }
        }
        if (classEmpty)
            System.out.println();
    }

    /* Given a grade, find all teachers who teach it.*/
    public static void findTeacherGivenGrade(int grade, ArrayList<Student> students, ArrayList<Teacher> teachers) {
        ArrayList<Integer> classrooms = new ArrayList<>();
        for (Student s : students) {
            if (s.grade == grade) {
                classrooms.add(s.classroom);
            }
        }

        boolean noTeachers = true;
        for (Teacher t : teachers) {
            if (classrooms.contains(t.classroom)) {
                noTeachers = false;
                System.out.println(t.lName + " " + t.fName + " " + t.classroom);
            }
        }
        if (noTeachers)
            System.out.println();
    }

    /**
     * Report the enrollments broken down by classroom (i.e., output a list of classrooms ordered
     * by classroom number, with a total number of students in each of the classrooms).
     * */
    public static void enrollment(ArrayList<Student> students) {
        List<Integer> classrooms = new ArrayList<>();
        for (Student s : students) {
            if (!classrooms.contains(s.classroom)) {
                classrooms.add(s.classroom);
            }
        }
        Collections.sort(classrooms);

        ArrayList<Integer> numberOfStudentsEnrolled =
            new ArrayList<>(Collections.nCopies(classrooms.size(), 0));
        for (Student s : students) {
            int oldValue = numberOfStudentsEnrolled.get(classrooms.indexOf(s.classroom));
            numberOfStudentsEnrolled.set(classrooms.indexOf(s.classroom), ++oldValue);
        }

        for (int i = 0; i < numberOfStudentsEnrolled.size(); i++) {
            System.out.println(classrooms.get(i) + ": " + numberOfStudentsEnrolled.get(i));
        }
    }
}
