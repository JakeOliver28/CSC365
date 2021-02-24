import java.util.*;
/**
 * Write a description of class Student here.
 *
 * @author Cory Baxes
 * @version 1/8/2018
 */
public class Student
{
    public String fName;
    public String lName;
    public int grade;
    public int bus;
    public int classroom;
    public Double gpa;
    public String TfName;
    public String TlName;

    public Student(String lName, String fName, int grade, int classroom, int bus, Double gpa, String TlName, String TfName)
    {
        this.lName = lName;
        this.fName = fName;
        this.grade = grade;
        this.bus = bus;
        this.classroom = classroom;
        this.gpa = gpa;
        this.TlName = TlName;
        this.TfName = TfName;
    }

}
