/**
 * Average 
 *
 * @author Jacob Oliver
 * @version 1/8/2018
 */

import java.util.ArrayList;

public class Average{

   public static void average(ArrayList<Student> students, int grade){

      double totalGPA = 0;
      int numStudents = 0;

      // iterate through all students in data set
      for (Student thisStudent : students){
         // count number of students of desired grade
         // and add their GPA to total GPA
         if (thisStudent.grade == grade){
            numStudents++;
            totalGPA += thisStudent.gpa;
         }
      }

      // if no students were found
      if (numStudents == 0){
         System.out.println();
         return;
      }

      // calculate average GPA and print output
      double avgGPA = totalGPA/numStudents;
      System.out.format("%.2f\n", avgGPA);
   }

}
