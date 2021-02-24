import java.util.*;
/**
 * Write a description of class TeacherSearch here.
 *
 * @author Cory Baxes
 * @version 1/8/2018
 */
public class TeacherSearch
{
    public static void findTeacher(ArrayList<Student> students, String lname)
    {
        boolean teacherExists = false;
        for (Student s : students)
        {
            if (s.TlName.equals(lname))
            {
                teacherExists = true;
                System.out.println(s.lName + " " + s.fName);
            }
        }
        if (!teacherExists)
            System.out.println();
    }
}
