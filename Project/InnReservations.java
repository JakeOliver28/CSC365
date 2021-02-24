/*
 * InnReservations JDBC
 *
 * @author Jacob Oliver
 * @version 3/12/18
 */

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.util.Map;
import java.util.Scanner;
import java.util.LinkedHashMap;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

import java.io.IOException;

public class InnReservations {


   public static void main(String[] args) {

      try{
         InnReservations ir = new InnReservations();
         Connection conn = ir.establishConnection();
         runProgram(conn);
      }
      catch (SQLException e){
         System.err.println("SQLException: " + e.getMessage()); 
      }
   }

   public Connection establishConnection() throws SQLException{

      Connection conn = DriverManager.getConnection(
         System.getenv("HP_JDBC_URL"),
         System.getenv("HP_JDBC_USER"),
         System.getenv("HP_JDBC_PW"));

      return conn; 
  }
         

   // runs main functionality of program
   public static void runProgram(Connection conn) throws SQLException{

      //ddlStatements(conn);

      Scanner sc = new Scanner(System.in);
      int choice = 0;   

      // while user does not choose to exit
      while(choice != 5){

         System.out.println("Choose Rooms and Rates (1), Reservations (2)" +
            ", Detailed Reservation Information (3), Revenue (4)" +
            ", or Quit (5):");

         try{
            choice = sc.nextInt();
            sc.nextLine();
         }
         catch(Exception e){
            System.out.println("Input must be integer 1-5");
            System.exit(-1);
         }

         // run respective method based on user input
         if (choice == 1)
            roomsAndRates(conn);
         else if (choice == 2)
            reservations(conn);
         else if (choice == 3)
            reservInfo(conn);
         else if (choice == 4)
            revenue(conn);
         else if (choice != 5)
            System.out.println("Please enter an integer value between 1 and 5.");
      }

   }

   // initial DDL statements to define the data table
   public static void ddlStatements(Connection conn) throws SQLException{

      String ddlRooms = "CREATE TABLE IF NOT EXISTS lab6_rooms (" +
         "RoomCode char(5) PRIMARY KEY," +
         "RoomName varchar(30) NOT NULL," +
         "Beds int(11) NOT NULL," +
         "bedType varchar(8) NOT NULL," +
         "maxOcc int(11) NOT NULL," +
         "basePrice DECIMAL(6,2) NOT NULL," +
         "decor varchar(20) NOT NULL," +
         "UNIQUE (RoomName))";

      String ddlReservations = "CREATE TABLE IF NOT EXISTS lab6_reservations (" +
         "CODE int(11) PRIMARY KEY, " + 
         "Room char(5) NOT NULL, " +
         "CheckIn date NOT NULL, " +
         "Checkout date NOT NULL, " +
         "Rate DECIMAL(6,2) NOT NULL, " +
         "LastName varchar(15) NOT NULL, " +
         "FirstName varchar(15) NOT NULL, " +
         "Adults int(11) NOT NULL, " +
         "Kids int(11) NOT NULL, " +
         "UNIQUE (Room, CheckIn), " +
         "UNIQUE (Room, Checkout), " +
         "FOREIGN KEY (Room) REFERENCES lab6_rooms (RoomCode))";
   
      String insertRooms = "INSERT INTO lab6_rooms SELECT * FROM INN.rooms";

      String insertReservations = "INSERT INTO lab6_reservations SELECT CODE, Room, " +
         "DATE_ADD(CheckIn, INTERVAL 8 YEAR), " +
         "DATE_ADD(Checkout, INTERVAL 8 YEAR), " +
         "Rate, LastName, FirstName, Adults, Kids FROM INN.reservations";

      try (Statement stmt = conn.createStatement()){

         stmt.execute(ddlRooms);
         stmt.execute(ddlReservations);
         stmt.execute(insertRooms);
         stmt.execute(insertReservations);
         
      }

   }

   // shows indivitual rooms and their information
   public static void roomsAndRates(Connection conn) throws SQLException{

      String sqlStatement = "SELECT ROUND(SUM(DATEDIFF(reservations." +
         "CheckOut, reservations.CheckIn))/180, 2) AS RoomPopularityScore, CURDATE()," +
         " rooms.RoomCode, rooms.RoomName, rooms.Beds, rooms.bedType," +
         " rooms.maxOcc, rooms.basePrice, rooms.decor FROM lab6_rooms AS" +
         " rooms, lab6_reservations AS reservations WHERE rooms.RoomCode " +
         "= reservations.Room AND reservations.CheckIn <= CURDATE() AND " +
         "DATEDIFF(reservations.CheckOut, CURDATE()) < 180 GROUP BY rooms." +
         "RoomCode ORDER BY RoomPopularityScore DESC";
      
      try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sqlStatement)) {
         Random rand = new Random();
         System.out.format("%10s %30s %23s %10s %30s %10s %10s %10s %10s %15s\n", 
            "Popularity", "Next Available Check In", "Latest Stay Len", 
            "Room Code", "Room Name", "Beds", "Bed Type", 
            "Max Occ", "Base Price", "Decor");

         // print out each room in resultset
         while (rs.next()) {
            Double roomPop = rs.getDouble("RoomPopularityScore");
            String nextCheckIn = rs.getString("CURDATE()");
            Integer lenStay = rand.nextInt(6) + 2;
            String roomCode = rs.getString("RoomCode");
            String roomName = rs.getString("RoomName");
            Integer beds = rs.getInt("Beds");
            String bedType = rs.getString("bedType");
            Integer maxOcc = rs.getInt("maxOcc");
            Double basePrice = rs.getDouble("basePrice");
            String decor = rs.getString("decor");
            System.out.format("%10.2f %30s %23d %10s %30s %10d %10s %10d %10.2f %15s\n", 
               roomPop, nextCheckIn, lenStay,  
               roomCode, roomName, beds, bedType, maxOcc, basePrice,
               decor);
         }
         System.out.println();
      }
       

   }

   // allows user to search reservations based on their input
   public static void reservInfo(Connection conn) throws SQLException{
      
      Scanner sc = new Scanner(System.in);
    
      String fName = "N/A";
      String lName = "N/A";
      String startDate = "N/A";
      String endDate = "N/A";
      String roomCode = "N/A";
      String reservCode = "N/A";

      System.out.println("What specifications do you want to make?\n"
         + "None (0), First Name (1), Last Name (2), Room Code (3), "
         + "or Reservation Code (4).");

      int choice = sc.nextInt();
      sc.nextLine();

      while(choice != 0){

         if (choice == 1){
            System.out.println("First Name (Enter 'N/A' to Skip):");
            fName = sc.next();
         }
         else if (choice == 2){
            System.out.println("Last Name (Enter 'N/A' to Skip):");
            lName = sc.next();
         }
         else if (choice == 3){
            System.out.println("Room Code (Enter 'N/A' to Skip):");
            roomCode = sc.next();
         }
         else if (choice == 4){
            System.out.println("Reservation Code (Enter 'N/A' to Skip):");
            reservCode = sc.next();
         }
         else
            System.out.println("Please enter an integer value between 0 and 4.");

         System.out.println("What additional specifications do you want to make?\n"
            + "None (0), First Name (1), Last Name (2), Room Code (3), "
            + "or Reservation Code (4).");

         choice = sc.nextInt();

      }

      StringBuilder sqlStatement = new StringBuilder(
         "SELECT * FROM lab6_rooms AS rooms, lab6_reservations AS reservations " +
         "WHERE rooms.RoomCode = reservations.Room");
      if (!fName.equals("N/A"))
         sqlStatement.append(" AND reservations.FirstName LIKE '" + fName + "'");
      if (!lName.equals("N/A"))
         sqlStatement.append(" AND reservations.LastName LIKE '" + lName + "'");
      if (!roomCode.equals("N/A"))
         sqlStatement.append(" AND reservations.Room LIKE '" + roomCode + "'");
      if (!reservCode.equals("N/A"))
         sqlStatement.append(" AND reservations.CODE LIKE '" + reservCode + "'");


      try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sqlStatement.toString())) {

         System.out.println();
         int sizeOfRS = 0;
         while (rs.next()) {
            if (sizeOfRS == 0){
               System.out.format("%11s %5s %25s %11s %11s %7s %15s %15s %6s %6s\n", "Code", "Room",
               "Room Name", "Check In", "Check Out", "Rate", "Last Name",
               "First Name", "Adults", "Kids");
               sizeOfRS++;
            }
            String code = rs.getString("CODE");
            String room = rs.getString("Room");
            String roomName = rs.getString("RoomName");
            String checkIn = rs.getString("CheckIn");
            String checkOut = rs.getString("Checkout");
            String rate = rs.getString("Rate");
            String lastName = rs.getString("LastName");
            String firstName = rs.getString("FirstName");
            String adults = rs.getString("Adults");
            String kids = rs.getString("Kids");
            System.out.format("%11s %5s %25s %11s %11s %7s %15s %15s %6s %6s\n", code, room, 
               roomName, checkIn, checkOut, rate, lastName, 
               firstName, adults, kids);
         }
         if (sizeOfRS == 0)
            System.out.println("No results found.");
         System.out.println();
      
      }  

   }

   // calculate different revenue statistics
   public static void revenue(Connection conn) throws SQLException{

      HashMap<String, Double> revenueList = new 
         HashMap<String, Double>();
      ArrayList<String> roomList = new ArrayList<String>();
      double[] totalsForMonths = new double[13];

      String sqlStatement = "SELECT rooms.RoomCode, MONTH(reservations." 
         + "CheckOut) AS Month, SUM(DATEDIFF(reservations.CheckOut,"
         + " reservations.CheckIn)*reservations.Rate) AS MonthlyRevenue"
         + " FROM lab6_rooms AS rooms, lab6_reservations AS reservations"
         + " WHERE rooms.RoomCode = reservations.Room AND YEAR(reservations."
         + "Checkout) = YEAR(CURDATE())"
         + " GROUP BY rooms.RoomCode, MONTH(reservations.CheckOut)";

      try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sqlStatement.toString())) {

         while (rs.next()){
            String roomCode = rs.getString("RoomCode");
            int month = rs.getInt("Month");
            Double monthlyRevenue = rs.getDouble("MonthlyRevenue");
            
            // register room code if not yet found
            if (roomList.size() == 0)
               roomList.add(roomCode);
            else if (!roomList.get(roomList.size() - 1).equals(roomCode))
               roomList.add(roomCode);
            // add revenue to respective column and row totals
            revenueList.put(roomCode + Integer.toString(month), monthlyRevenue);
            revenueList.put(roomCode + "13", monthlyRevenue + 
               revenueList.getOrDefault(roomCode + "13", 0.0));
            totalsForMonths[month-1] += monthlyRevenue;
            totalsForMonths[12] += monthlyRevenue;
         }

      }

      printRevenueResults(revenueList, roomList, totalsForMonths);

   }

   // prints results of revenue calculations
   public static void printRevenueResults(HashMap<String, Double> 
      revenueList, ArrayList<String> roomList, double[] totalsForMonths){

      System.out.format("%7s %9s %9s %9s %9s %9s %9s %9s %9s %9s %9s %9s %9s %9s\n",
         "Room", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
         "Sep", "Oct", "Nov", "Dec", "Total");

      for (int i = 0; i < roomList.size(); i++){
         System.out.format("%7s", roomList.get(i));
         for (int j = 1; j <= 13; j++){
            System.out.format(" %9s", revenueList.get(roomList.get(i) + Integer.toString(j)));
         }
         System.out.println();
      }
      System.out.format("%7s", "Totals");
      for (int k = 0; k <= 12; k++)
         System.out.format(" %9s", totalsForMonths[k]);
      System.out.println("\n");

   }




   // run if user wants to try and make a reservation
   public static void reservations(Connection conn) throws SQLException {
      HashMap<String, String> thisReserv = requestReservation();

      String checkMaxOcc = "SELECT MAX(rooms.maxOcc) as MaxOcc " +
         "FROM lab6_rooms AS rooms GROUP BY rooms.RoomCode";

      try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(checkMaxOcc)){

         while (rs.next()){

            int maxOcc = rs.getInt("MaxOcc");
            if (maxOcc < (Integer.parseInt(thisReserv.get("Num Adults")) +
               Integer.parseInt(thisReserv.get("Num Children")))){

               System.out.println("Number of Adults and Children exceeds" +
                  " max occupancy for all rooms.");
               return;
            }

         }

      }

      String sqlStatement = "SELECT * FROM lab6_rooms AS rooms " +
         "WHERE rooms.maxOcc >= (? + ?) AND rooms.bedType LIKE ? " +
         "AND rooms.RoomCode LIKE ? AND rooms.RoomCode NOT IN (" +
         "SELECT rooms.RoomCode FROM lab6_rooms AS rooms, lab6_reservations " +
         "AS reservations WHERE rooms.RoomCode = reservations.Room AND " +
         "reservations.CheckIn <= ? AND reservations.CheckOut > ?)";


      try (PreparedStatement pstmt = conn.prepareStatement(sqlStatement)){

         pstmt.setInt(1, Integer.parseInt(thisReserv.get("Num Adults")));
         pstmt.setInt(2, Integer.parseInt(thisReserv.get("Num Children")));
         pstmt.setString(3, thisReserv.get("Bed Type"));
         pstmt.setString(4, thisReserv.get("Room Code"));
         pstmt.setString(5, thisReserv.get("Begin Date"));
         pstmt.setString(6, thisReserv.get("End Date"));

         ResultSet thisRS = pstmt.executeQuery();

         ArrayList<reservObj> reserves = new ArrayList<reservObj>();
         int count = 0;
         while (thisRS.next()){
            if (count == 0)
               System.out.println("These rooms are a match for your specifications.");
            count++;

            String roomCode = thisRS.getString("RoomCode");
            String roomName = thisRS.getString("RoomName");
            int numBeds = thisRS.getInt("Beds");
            String bedType = thisRS.getString("bedType");
            double basePrice = thisRS.getDouble("basePrice");
            String decor = thisRS.getString("decor");

            reservObj thisRO = new reservObj(roomCode, roomName, numBeds,
            bedType, basePrice, decor, thisReserv.get("Begin Date"),
            thisReserv.get("End Date"), thisReserv.get("Last Name"),
            thisReserv.get("First Name"),
            Integer.parseInt(thisReserv.get("Num Adults")),
            Integer.parseInt(thisReserv.get("Num Children")));
            System.out.print("(" + count + ") ");
            System.out.print(thisRO.toString() + "\n");

            reserves.add(thisRO);

         }

         if (count == 0)
            reserves = noMatches(conn, thisReserv);

         System.out.println("Enter which number reservation you wish" +
            " to make, or enter 'Cancel' to cancel.");

         Scanner sc = new Scanner(System.in);
         String finalChoice = sc.next();
         if (finalChoice.equals("Cancel"))
            return;
         else{
            makeReservation(conn, reserves.get(Integer.parseInt(finalChoice)));
         }

      }

   }

   // search for similar possibilities if no match for reservation request is found
   public static ArrayList<reservObj> noMatches(Connection conn, HashMap<String, String> thisReserv)
      throws SQLException{

      System.out.println("No exact matches were found. Here are 5 other similar options.");

      int count = 0;

      String sqlStatement = "SELECT * FROM lab6_rooms AS rooms " +
         "WHERE rooms.maxOcc >= (? + ?) AND rooms.bedType LIKE ? " +
         "AND rooms.RoomCode LIKE ? AND rooms.RoomCode NOT IN (" +
         "SELECT rooms.RoomCode FROM lab6_rooms AS rooms, lab6_reservations " +
         "AS reservations WHERE rooms.RoomCode = reservations.Room AND " +
         "reservations.CheckIn <= ? AND reservations.CheckOut > ?)";



      try (PreparedStatement pstmt = conn.prepareStatement(sqlStatement)){

         pstmt.setInt(1, Integer.parseInt(thisReserv.get("Num Adults")));
         pstmt.setInt(2, Integer.parseInt(thisReserv.get("Num Children")));
         pstmt.setString(3, "%");
         pstmt.setString(4, "%");
         pstmt.setString(5, "DATE_ADD(" + thisReserv.get("Begin Date")
            + ", INTERVAL 2 DAY)");
         pstmt.setString(6, "DATE_ADD(" + thisReserv.get("End Date")
            + ", INTERVAL 2 DAY)");

         ResultSet thisRS = pstmt.executeQuery();

         ArrayList<reservObj> reserves = new ArrayList<reservObj>();

         while (thisRS.next()){
            count++;

            String roomCode = thisRS.getString("RoomCode");
            String roomName = thisRS.getString("RoomName");
            int numBeds = thisRS.getInt("Beds");
            String bedType = thisRS.getString("bedType");
            double basePrice = thisRS.getDouble("basePrice");
            String decor = thisRS.getString("decor");

            reservObj thisRO = new reservObj(roomCode, roomName, numBeds,
            bedType, basePrice, decor, thisReserv.get("Begin Date"),
            thisReserv.get("End Date"), thisReserv.get("Last Name"),
            thisReserv.get("First Name"),
            Integer.parseInt(thisReserv.get("Num Adults")),
            Integer.parseInt(thisReserv.get("Num Children")));
            System.out.print("(" + count + ") ");
            System.out.print(thisRO.toString() + "\n");

            reserves.add(thisRO);

            if (count >= 5)
               return reserves;

         }

         pstmt.clearParameters();
         pstmt.setInt(1, Integer.parseInt(thisReserv.get("Num Adults")));
         pstmt.setInt(2, Integer.parseInt(thisReserv.get("Num Children")));
         pstmt.setString(3, "%");
         pstmt.setString(4, "%");
         pstmt.setString(5, "DATE_ADD(" + thisReserv.get("Begin Date")
            + ", INTERVAL 2 DAY)");
         pstmt.setString(6, "DATE_ADD(" + thisReserv.get("End Date")
            + ", INTERVAL 2 DAY)");

         while (thisRS.next()){
            count++;

            String roomCode = thisRS.getString("RoomCode");
            String roomName = thisRS.getString("RoomName");
            int numBeds = thisRS.getInt("Beds");
            String bedType = thisRS.getString("bedType");
            double basePrice = thisRS.getDouble("basePrice");
            String decor = thisRS.getString("decor");

            reservObj thisRO = new reservObj(roomCode, roomName, numBeds,
            bedType, basePrice, decor, thisReserv.get("Begin Date"),
            thisReserv.get("End Date"), thisReserv.get("Last Name"),
            thisReserv.get("First Name"),
            Integer.parseInt(thisReserv.get("Num Adults")),
            Integer.parseInt(thisReserv.get("Num Children")));
            System.out.print("(" + count + ") ");
            System.out.print(thisRO.toString() + "\n");

            reserves.add(thisRO);

            if (count >= 5)
               return reserves;

         }

         return reserves;

      }

   }

   
   // prepare and execute SQL insert statement to enter reservation
   public static void makeReservation(Connection conn,
      reservObj reservation) throws SQLException{

      Random rand = new Random();
      Integer thisCodeInt = rand.nextInt(9999) + 1;
      String thisCode = String.format("%05d", thisCodeInt);

      System.out.println("CODE: " + thisCode);

      String sqlInsert = "INSERT INTO lab6_reservations (CODE, Room, " +
         "CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids)" +
         " VALUES (" + thisCode + ", ?, ?, ?, ?, ?, ?, ?, ?)";

      try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)){

         pstmt.setString(1, reservation.roomCode);
         pstmt.setString(2, reservation.startDate);
         pstmt.setString(3, reservation.endDate);

         double basePrice = reservation.basePrice * 1.18;

         pstmt.setDouble(4, basePrice);
         pstmt.setString(5, (reservation.lastName).toUpperCase());
         pstmt.setString(6, (reservation.firstName).toUpperCase());
         pstmt.setInt(7, reservation.adults);
         pstmt.setInt(8, reservation.kids);

         pstmt.execute();

      }

      System.out.println("Reservation Made!");

   }




   // returns reservation hashmap after prompting user for info
   public static HashMap<String, String> requestReservation(){
     
      Scanner sc = new Scanner(System.in);
      HashMap<String, String> thisReserv = new HashMap<String, String>();   
      String nextString;

      System.out.println("First Name:");      
      thisReserv.put("First Name", sc.next());
      System.out.println("Last Name:");
      thisReserv.put("Last Name", sc.next());
      System.out.println("Room Code (or 'Any'):");
      if ((nextString = sc.next()).equals("Any"))
         thisReserv.put("Room Code", "%");
      else
         thisReserv.put("Room Code", nextString);
      System.out.println("Desired Bed Type (or 'Any'):");
      if ((nextString = sc.next()).equals("Any"))
         thisReserv.put("Bed Type", "%");
      else
         thisReserv.put("Bed Type", nextString);
      System.out.println("Begin Date (YYYY-MM-DD):");
      thisReserv.put("Begin Date", sc.next());
      System.out.println("End Date (YYYY-MM-DD):");
      thisReserv.put("End Date", sc.next());
      System.out.println("Number of Children:");
      thisReserv.put("Num Children", sc.next());
      System.out.println("Number of Adults:");
      thisReserv.put("Num Adults", sc.next());

      return thisReserv;

   }


   // reservObj nested class 

   private static class reservObj {

      private String roomCode;
      private String roomName;
      private Integer beds;
      private String bedType;
      private Double basePrice;
      private String decor;
      private String startDate;
      private String endDate;
      private String lastName;
      private String firstName;
      private int adults;
      private int kids;

      public reservObj(String roomCode, String roomName, int beds,
         String bedType, double basePrice, String decor,
         String startDate, String endDate, String lastName,
         String firstName, int adults, int kids){

         this.roomCode = roomCode;
         this.roomName = roomName;
         this.beds = beds;
         this.bedType = bedType;
         this.basePrice = basePrice;
         this.decor = decor;
         this.startDate = startDate;
         this.endDate = endDate;
         this.lastName = lastName;
         this.firstName = firstName;
         this.adults = adults;
         this.kids = kids;


      }

      public String toString(){

         String strRep = roomCode + ", " + roomName + ", " +
            beds.toString() + " " + "beds, " + bedType + ", " +
            "Base Price: " + basePrice + ", " + decor + ", " +
            startDate + " to " + endDate;

         return strRep;

      }

   }

}
