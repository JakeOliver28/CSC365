/**
 * Write a description of class Driver here.
 *
 * @author Cory Baxes
 * @version 1/8/2018
 */

import java.util.*;
import java.io.*;

public class Driver
{
    private static String dbFile = "students.txt";
    
    public static void main(String [] args) throws FileNotFoundException, IOException
    {
        String input = "";
        Scanner s = new Scanner( System.in );
        BufferedReader br;

        br = new BufferedReader(new FileReader(dbFile));
        String line = null;
        ArrayList<Student> students = new ArrayList<Student>();
        
        while ((line = br.readLine()) != null) {
            String[] vals = line.split(",");
            Integer grade, classroom, bus;
            Double gpa;
            if (vals.length != 8) {
                System.out.format("Incorrectly formatted line in " + dbFile + ".\n");
                return;
            }
            try {    
                grade = Integer.parseInt(vals[2]);
                classroom = Integer.parseInt(vals[3]);
                bus = Integer.parseInt(vals[4]);
                gpa = Double.parseDouble(vals[5]);
                Student newStudent = new Student(vals[0], vals[1], grade, classroom, bus, gpa, vals[6], vals[7]);
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
            else {
                invalidCommand();
            }
            
            System.out.println("Enter a command: ");
            input = s.nextLine();
        }
    }
    
    private static void invalidCommand()
    {
        System.out.println("Invalid command.");
    }
}
