/**
 *
 *
 * @author Jacob Oliver
 * @version 1/8/18
 */

import java.util.ArrayList;

public class Info{

   public static void info(ArrayList<Student> students){
       
      int[] numberOfStudents = new int[7];
      for (Student thisStudent : students){
         (numberOfStudents[thisStudent.grade])++;
      }

      for (int i = 0; i < 7; i++){
         System.out.println((i) + ": " + numberOfStudents[i]);
      }

   }

}
