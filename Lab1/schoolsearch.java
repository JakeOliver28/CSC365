/**
 * Write a description of class Driver here.
 *
 * @author Cory Baxes, Monica Chavez, Jacob Oliver
 * @version 1/8/2018
 */

import java.util.*;
import java.io.*;

public class schoolsearch
{
    private static String studentDBFile = "list.txt";
    private static String teacherDBFile = "teachers.txt";

    public static void main(String [] args) throws FileNotFoundException, IOException
    {
        String input = "";
        Scanner s = new Scanner( System.in );
        BufferedReader br;

        br = new BufferedReader(new FileReader(teacherDBFile));
        String line = null;
        ArrayList<Teacher> teachers = new ArrayList<Teacher>();

        while ((line = br.readLine()) != null) {
            String[] vals = line.split(",");
            Integer classroom;
            if (vals.length != 3) {
                System.out.format("Incorrectly formatted line in " + teacherDBFile + ".\n");
                return;
            }
            try {
                classroom = Integer.parseInt(vals[2].trim());
                Teacher newTeacher = new Teacher(vals[0].trim(), vals[1].trim(), classroom);
                teachers.add(newTeacher);
            } catch (NumberFormatException nfe) {
                System.out.println(nfe.getMessage());
                System.exit(1);
            }
        }

        br = new BufferedReader(new FileReader(studentDBFile));
        line = null;
        ArrayList<Student> students = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            String[] vals = line.split(",");
            Integer grade, classroom, bus;
            Double gpa;
            if (vals.length != 6) {
                System.out.format("Incorrectly formatted line in " + studentDBFile + ".\n");
                return;
            }
            try {
                grade = Integer.parseInt(vals[2].trim());
                classroom = Integer.parseInt(vals[3].trim());
                bus = Integer.parseInt(vals[4].trim());
                gpa = Double.parseDouble(vals[5].trim());
                // Find Teacher in this classroom
                Teacher t = findTeacherIn(classroom, teachers);
                Student newStudent = new Student(vals[0].trim(), vals[1].trim(), grade, classroom, bus, gpa, t.lName, t.fName);
                students.add(newStudent);
            } catch (NumberFormatException nfe) {
                System.out.println(nfe.getMessage());
                System.exit(1);
            }
        }

        System.out.println("Enter a command: ");
        input = s.nextLine();


        while (!input.equals("Q") && !input.equals("Quit")) {
            args = input.split(" ");

            // Student command
            if (args[0].equals("S:") || args[0].equals("Student:")){
                if (args.length == 2) {
                    StudentSearch.findStudent(students, args[1]);
                } else if (args.length == 3) {
                    if (args[2].equals("B") || args[2].equals("Bus")) {
                        StudentSearch.findStudentBus(students, args[1]);
                    }
                } else {
                    invalidCommand();
                }
            }
            // Teacher command
            else if (args[0].equals("T:") || args[0].equals("Teacher:")){
                if (args.length == 2) {
                    TeacherSearch.findTeacher(students, args[1]);
                } else {
                    invalidCommand();
                }
            }
            // Bus command
            else if (args[0].equals("B:") || args[0].equals("Bus:")){
                if (args.length == 2) {
                    try {
                        Integer bus = Integer.parseInt(args[1]);
                        StudentSearch.findStudentByBus(students, bus);
                    }
                    catch (NumberFormatException nfe) {
                        invalidCommand();
                    }
                } else {
                    invalidCommand();
                }
            }
            // Grade command
            else if (args[0].equals("G:") || args[0].equals("Grade:")){
                if (args.length == 2) {
                    try {
                        Integer grade = Integer.parseInt(args[1]);
                        StudentSearch.findStudentByGrade(students, grade);
                    }
                    catch (NumberFormatException nfe) {
                        invalidCommand();
                    }
                } else if (args.length == 3) {
                    try {
                        Integer grade = Integer.parseInt(args[1]);
                        if (args[2].equals("H") || args[2].equals("High")) {
                            StudentSearch.findStudentByGradeAndGPA(students, grade, 'H');
                        } else if (args[2].equals("L") || args[2].equals("Low")) {
                            StudentSearch.findStudentByGradeAndGPA(students, grade, 'L');
                        } else {
                            invalidCommand();
                        }
                    }
                    catch (NumberFormatException nfe) {
                        invalidCommand();
                    }
                } else {
                    invalidCommand();
                }
            }
            // Average command
            else if (args[0].equals("A:") || args[0].equals("Average:")){
                if (args.length == 2) {
                    try {
                        Integer grade = Integer.parseInt(args[1]);
                        Average.average(students, grade);
                    }
                    catch (NumberFormatException nfe) {
                        invalidCommand();
                    }
                } else {
                    invalidCommand();
                }
            }
            // Info command
            else if (args[0].equals("I") || args[0].equals("Info")){
                if (args.length == 1) {
                    Info.info(students);
                } else {
                    invalidCommand();
                }
            }
            else if (args[0].equals("E") || args[0].equals("Enrollment")) {
                if (args.length == 1) {
                    StudentSearch.enrollment(students);
                } else {
                    invalidCommand();
                }
            }
            else if (args[0].equals("SGC:") || args[0].equals("StudentGivenClassroom:")) {
                if (args.length == 2) {
                    try {
                        Integer classroom = Integer.parseInt(args[1]);
                        StudentSearch.findStudentsGivenClassroom(classroom, students);
                    } catch (NumberFormatException nfe) {
                        System.out.println(nfe.getMessage());
                        System.exit(1);
                    }
                }
            }
            else if (args[0].equals("TGC:") || args[0].equals("TeachersGivenClassroom:")) {
                if (args.length == 2) {
                    try {
                        Integer classroom = Integer.parseInt(args[1]);
                        StudentSearch.findTeacherGivenClassroom(classroom, teachers);
                    } catch (NumberFormatException nfe) {
                        System.out.println(nfe.getMessage());
                        System.exit(1);
                    }
                }
            }
            else if (args[0].equals("TGG:") || args[0].equals("TeachersGivenGrade:")) {
                if (args.length == 2) {
                    try {
                        Integer grade = Integer.parseInt(args[1]);
                        StudentSearch.findTeacherGivenGrade(grade, students, teachers);
                    } catch (NumberFormatException nfe) {
                        System.out.println(nfe.getMessage());
                        System.exit(1);
                    }
                }
            }
            // Run analytics
            else if (args[0].equals("An:") || args[0].equals("Analytics:")) {
               if (args.length == 2){
                  if (args[1].equals("G") || args[1].equals("Grade")){
                     Analytics.gradeLevel(students);
                  } else if (args[1].equals("T") || args[1].equals("Teacher")){
                     Analytics.teacher(teachers, students);
                  } else if (args[1].equals("B") || args[1].equals("Bus")){
                     Analytics.busRoute(students);
                  } else{
                     invalidCommand();
                  }
               } else {
                  invalidCommand();
               }
            }
            else {
                invalidCommand();
            }

            System.out.println("Enter a command: ");
            input = s.nextLine();
        }
    }

    private static Teacher findTeacherIn(int classroom, ArrayList<Teacher> teachers)
    {
        for (Teacher t : teachers)
        {
            if (t.classroom == classroom)
            {
                return t;
            }
        }
        System.out.println("No teacher found in classroom " + classroom + ".");
        return null;
    }

    private static void invalidCommand()
    {
        System.out.println("Invalid command.");
    }
}
