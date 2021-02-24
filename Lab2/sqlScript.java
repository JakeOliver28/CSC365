/**
 * Read in csv files
 *
 * @author Jacob Oliver
 * @version 1/31/18
 */

import java.io.*;
import java.util.*;

public class sqlScript {

   public static void main(String[] args) throws IOException, FileNotFoundException {

      int numFiles = 4;

      String outFileName = args[0];
      FileWriter fileWriter = new FileWriter(outFileName);
      BufferedWriter buffWriter = new BufferedWriter(fileWriter);

      String[] nameTable = new String[numFiles];
      for (int notMom = 0; notMom < numFiles; notMom++){
         nameTable[notMom] = args[notMom+1];
      }

      for (int i=numFiles+1; i < args.length; i++){
         String filename = args[i];
         FileReader filereader = new FileReader(filename);
         BufferedReader bufferedReader = new BufferedReader(filereader);


         String line = bufferedReader.readLine();
         String[] dLine = line.split(",");
         String[] attributes = new String[dLine.length];
         for (int k = 0; k < attributes.length; k++)
            attributes[k] = dLine[k];
         while((line = bufferedReader.readLine()) != null){
            dLine = line.split(",");
            for (int j=0; j < dLine.length; j++)
               System.out.println(dLine[j]);
            if (dLine.length < attributes.length)
               break;
            buffWriter.write("INSERT INTO ");
            buffWriter.write(nameTable[i-numFiles-1]);
            buffWriter.write(" (");
            System.out.println("Attributes:" + attributes.length);
            for (int index = 0; index < attributes.length-1; index++){
               buffWriter.write(attributes[index].trim() + ", ");
            }
            buffWriter.write(attributes[attributes.length-1].trim() + ") ");
            buffWriter.write("VALUES (");
            for (int newInd = 0; newInd < dLine.length-1; newInd++){
               if (newInd == 2 || newInd == 3){
                  buffWriter.write("20");
                  buffWriter.write(dLine[newInd][7] + dLine[newInd][8]);
                  buffWriter.write("-");
                  buffWriter.write(getMonth(dLine[newInd]{)
               }
               buffWriter.write(dLine[newInd]+ ", ");
            }
            buffWriter.write(dLine[dLine.length-1] + ");\n");
         }
         bufferedReader.close();
      }


   }

   public static String getMonth(String monthAbbr){
   
      if (monthAbbr == "JAN")
         return "01";
      else if (monthAbbr == "FEB")
         return "02";
      else if (monthAbbr == "MAR")
         return "03";
      else if (monthAbbr == "APR")
         return "04";
      else if (monthAbbr == "MAY")
         return "05";
      else if (monthAbbr == "JUN")
         return "06";
      else if (monthAbbr == "JUL")
         return "07";
      else if (monthAbbr == "AUG")
         return "08";
      else if (monthAbbr == "SEP")
         return "09";
      else if (monthAbbr == "OCT")
         return "10";
      else if (monthAbbr == "NOV")
         return "11";
      else if (monthAbbr == "DEC")
         return "12";
      else
         return "01";

   }

}
