/**
 * Analytics
 *
 * @author Jacob Oliver, Monica Chavez, Cory Baxes
 * @version 1/17/18
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Analytics{

   public static void main(String[] args){
   }

   // calls helper functions to show all performance data calculated
   public static void showAnalytics(ArrayList<Student> students, ArrayList<Teacher> teachers){
      gradeLevel(students);
      teacher(teachers, students);
      busRoute(students);
   }



   // calculates and prints performance data related to students' grade level
   public static void gradeLevel(ArrayList<Student> students){
      for (int i = 0; i <= 6; i++){
         System.out.println("\nGRADE " + i);
         showGradeStats(students, i);
      }
   }

   public static void teacher(ArrayList<Teacher> teachers, ArrayList<Student> students){
      for (Teacher thisTeacher : teachers){
         System.out.println("\nTEACHER: " + thisTeacher.fName + " " + thisTeacher.lName);
         showTeacherStats(students, thisTeacher);
      }
   }

   public static void busRoute(ArrayList<Student> students){
      HashMap<Integer, Integer> buses = new HashMap<Integer, Integer>();
      for (Student thisStudent : students){
         if (!buses.containsKey(thisStudent.bus))
            buses.put(thisStudent.bus, 1);
      }
      for (Integer thisBus : buses.keySet()){
         System.out.println("\nBUS ROUTE " + thisBus);
         showBusStats(students, thisBus);
      }
   }



   public static void showGradeStats(ArrayList<Student> students, int grade){
      
      double totalGPA = 0;
      ArrayList<Double> gpaList = new ArrayList<Double>();
      int[] gpaTiers = new int[7];

      for (Student thisStudent : students){
         if (thisStudent.grade == grade){
            gpaList.add(thisStudent.gpa);
            totalGPA += thisStudent.gpa;
            gpaTiers[findTier(thisStudent.gpa)] += 1;
         }
      }

      Collections.sort(gpaList);

      printDistribution(gpaTiers);
      printMedianGPA(gpaList);
      printMeanGPA(gpaList.size(), totalGPA);
      printStandardDeviation(gpaList, totalGPA);

   }

   public static void showTeacherStats(ArrayList<Student> students, Teacher thisTeacher){
      double totalGPA = 0;
      ArrayList<Double> gpaList = new ArrayList<Double>();
      int[] gpaTiers = new int[7];

      for (Student thisStudent : students){
         if ((thisStudent.TlName).equals(thisTeacher.lName) &&
            (thisStudent.TfName).equals(thisTeacher.fName)){

            gpaList.add(thisStudent.gpa);
            totalGPA += thisStudent.gpa;
            gpaTiers[findTier(thisStudent.gpa)] += 1;
         }
      }

      Collections.sort(gpaList);

      printDistribution(gpaTiers);
      printMedianGPA(gpaList);
      printMeanGPA(gpaList.size(), totalGPA);
      printStandardDeviation(gpaList, totalGPA);

   }

   public static void showBusStats(ArrayList<Student> students, int thisBus){
      double totalGPA = 0;
      ArrayList<Double> gpaList = new ArrayList<Double>();
      int[] gpaTiers = new int[7];

      for (Student thisStudent : students){
         if (thisStudent.bus == thisBus){
            gpaList.add(thisStudent.gpa);
            totalGPA += thisStudent.gpa;
            gpaTiers[findTier(thisStudent.gpa)] += 1;
         }
      }

      Collections.sort(gpaList);

      printDistribution(gpaTiers);
      printMedianGPA(gpaList);
      printMeanGPA(gpaList.size(), totalGPA);
      printStandardDeviation(gpaList, totalGPA);

   }


   public static int findTier(double gpa){
      if (gpa > 3.75)
         return 6;
      else if (gpa > 3.5)
         return 5;
      else if (gpa > 3.25)
         return 4;
      else if (gpa > 3.0)
         return 3;
      else if (gpa > 2.75)
         return 2;
      else if (gpa > 2.5)
         return 1;
      else
         return 0;
   }

   public static void printDistribution(int[] gpaTiers){

      System.out.println("Distribution of GPAs");
      System.out.println("2.50 and below | 2.50 - 2.75 | 2.75 - 3.00 | 3.00 - 3.25 | 3.25 - 3.50 | 3.50 - 3.75 | 3.75 - 4.00");
      System.out.println("--------------------------------------------------------------------------------------------------");
      System.out.format("%14s | %11s | %11s | %11s | %11s | %11s | %11s\n", 
         Integer.toString(gpaTiers[0]), Integer.toString(gpaTiers[1]), 
         Integer.toString(gpaTiers[2]), Integer.toString(gpaTiers[3]), 
         Integer.toString(gpaTiers[4]), Integer.toString(gpaTiers[5]), 
         Integer.toString(gpaTiers[6]));
      System.out.println();
   }

   public static void printMeanGPA(int numStudents, double totalGPA){

      if (numStudents == 0)
         System.out.format("Mean GPA: N/A\n");
      else
         System.out.format("Mean GPA: %.2f\n", totalGPA/numStudents);

   }
   
   public static void printMedianGPA(ArrayList<Double> gpaList){

      if (gpaList.size() == 0)
         System.out.format("Median GPA: N/A\n");

      else if (gpaList.size() % 2 != 0)
         System.out.format("Median GPA: %.2f\n", gpaList.get((gpaList.size()/2)));
      else{
         double average = (gpaList.get((gpaList.size()/2) - 1) + gpaList.get((gpaList.size()/2))) / 2;
         System.out.format("Median GPA: %.2f\n", average);
      }
   }

   public static void printStandardDeviation(ArrayList<Double> gpaList, double totalGPA){
      
      if (gpaList.size() == 0){
         System.out.format("Standard Deviation: N/A\n\n");
         return;
      }
      double mean = totalGPA/(gpaList.size());
      double variance = 0;
      for (Double thisGPA : gpaList){
         variance += Math.pow((thisGPA - mean), 2);
      }

      System.out.format("Standard Deviation: %.2f\n\n", Math.sqrt(variance));

   }

}
