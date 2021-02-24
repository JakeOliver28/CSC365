/**
 * Reservation Methods
 *
 *
 * @author Jacob Oliver
 */


public class ReservationMethods {

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
/*
         pstmt.setInt(1, Integer.parseInt(thisReserv.get("Num Adults")));
         pstmt.setInt(2, Integer.parseInt(thisReserv.get("Num Children")));
         pstmt.setString(3, "%");
         pstmt.setString(4, "%");
         pstmt.setString(5, thisReserv.get("Begin Date"));
         pstmt.setString(6, thisReserv.get("End Date"));

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
*/         
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

}
