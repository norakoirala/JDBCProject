package jdbc;

import java.sql.*;
import java.util.Scanner;


public class JDBC {
    //  Database credentials
    static String USER = "root";
    static String PASS = "pass";
    static String DBNAME = "JDBC";
    
    //user input 
    static Scanner in = new Scanner(System.in);
    
    //connection var 
    static Connection conn = null; //initialize the connection
    
    
    static final String displayFormat = "%-5s%-15s%-15s%-15s\n";
    
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
        
    /**
     * Formats string if attribute is null
     * @param input - Takes in string to format 
     * @return - outputs formatted string 
     */
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
    
    /**
     * Establishes a connection with the database 
     */
    public static void establishConnection(){
        //Constructing the database URL connection string
        DB_URL = DB_URL + DBNAME + ";user="+ USER + ";password=" + PASS;
        
        try{
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(DB_URL);
        } catch(SQLException se) {
            System.out.println("Error establishing connection!");
            se.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    /**
     * Let's the user specify the connection variables
     */
    public static void userConnectPrompt(){
        System.out.print("Name of the database (not the user account): ");
        DBNAME = in.nextLine();
        System.out.print("Database user name: ");
        USER = in.nextLine();
        System.out.print("Database password: ");
        PASS = in.nextLine();
    }
    
    /**
     * Displays data for tables
     * @param rs - takes in result set
     */
    public static void displayData(ResultSet rs) {
        String output = "\n";
        try{
            //executing statement
            ResultSetMetaData rmd = rs.getMetaData();
            int columnCount = rmd.getColumnCount();
            
            if(!rs.next()) {
                //checking if table is empty
                output = "Invalid entry! " + rmd.getTableName(columnCount) + " does not exist.";
            } else {
                //formatting table header 
                for(int i =1; i<=columnCount; i++){
                    output += String.format("%-5s%-15s", " ", dispNull(rmd.getColumnName(i)));
                }
                output += "\n--------------------------------------------------------\n";
                //formatting rows
                do {
                    output += String.format("%-5s", Integer.toString(rs.getRow()));
                    for(int i =1; i<=columnCount; i++){
                        output += String.format("%-15s", dispNull(rs.getString(i)));
                    }
                    output += "\n";
                } while (rs.next());
            }

            //closing connections
            rs.close();
        } catch(SQLException se){
            System.out.println("Error displaying table!");
            se.printStackTrace();
        } 
        
        //prints table
        System.out.println(output);
    }

    /**
    * Menu for the user
    */
    public static void Menu() {
        //init variables  
        int choice;
        String state; 
        Statement st;
        PreparedStatement pState;
        ResultSet rs;
        String output;
        String gName;
        String pName;
        String bTitle;
        Boolean exit = false; 
        
        //runs til user exits
        do {
            //displays menu
            System.out.println("\nMenu:");
            System.out.println("1) List All Writing Groups");
            System.out.println("2) List Data Of Specific Writing Group");
            System.out.println("3) List All Publishers");
            System.out.println("4) List Data Of Specific Publisher");
            System.out.println("5) List All Books");
            System.out.println("6) List Data Of Specific Book");
            System.out.println("7) Add Book");
            System.out.println("8) Remove Book");
            System.out.println("9) Switch Publishers");
            System.out.println("10) Exit");
            System.out.print("\nSelection: ");
            
            //takes in input
            choice = in.nextInt();
            in.nextLine();
            
           switch(choice) {
                case 1: 
                    try {
                        state = "SELECT gName FROM writingGroup";
                        st = conn.createStatement();
                        rs = st.executeQuery(state);
                        displayData(rs);
                        st.close();
                    } catch (SQLException se) {
                        System.out.println("Error with Option 1!");
                        se.printStackTrace();
                    }
                    break;
                case 2: 
                    try {
                        System.out.print("\nWriting Group Name: ");
                        gName = in.nextLine();
                        state = "Select headWriter,yearFounded,subject FROM writingGroup WHERE gName = ?";
                        pState = conn.prepareStatement(state);
                        pState.setString(1, gName);
                        rs = pState.executeQuery();
                        displayData(rs);
                        pState.close();
                    } catch (SQLException se) {
                        System.out.println("Error with Option 2!");
                        se.printStackTrace();
                    }
                    break;
                case 3: 
                    try {
                        state = "SELECT pName from Publisher";
                        st = conn.createStatement();
                        rs = st.executeQuery(state);
                        displayData(rs);
                        st.close();
                    } catch (SQLException se) {
                        System.out.println("Error with Option 3!");
                        se.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        System.out.print("\nPublisher Name: ");
                        pName = in.nextLine();
                        state = "SELECT pAddress, pPhone, pEmail FROM Publisher WHERE pName = ?";
                        pState = conn.prepareStatement(state);
                        pState.setString(1, pName);
                        rs = pState.executeQuery();
                        displayData(rs);
                        pState.close();
                    } catch (SQLException se) {
                        System.out.println("Error with Option 4!");
                        se.printStackTrace();
                    }
                    break;
                case 5: 
                    try {
                        state = "SELECT bTitle from Book";
                        st = conn.createStatement();
                        rs = st.executeQuery(state);
                        displayData(rs);
                        st.close();
                    } catch (SQLException se) {
                        System.out.println("Error with Option 5!");
                        se.printStackTrace();
                    }
                    break;
                case 6: 
                    /*** 
                    try {
                        System.out.print("\nBook Name: ");
                        bTitle = in.nextLine();
                        System.out.print("Group Name: ");
                        gName = in.nextLine();
                        state = "SELECT pName, yearPublished, numberPages FROM Book WHERE bTitle = ? AND gName = ?";
                        pState = conn.prepareStatement(state);
                        pState.setString(1, bTitle);
                        pState.setString(2, gName);
                        rs = pState.executeQuery();
                        output = displayData(rs);
                        pState.close();
                    } catch (SQLIntegrityConstraintViolationException se) {
                        if (se.getMessage().contains("book_fk")) { //error on foreign key 
                            System.out.println("Invalid Book Name!");
                        } else if (se.getMessage().contains("book_fk_2")) {
                            System.out.println("Invalid Publisher Name!");
                        } else {
                            System.out.println("Error getting book information!");
                        }
                    } ***/
                    break;
                case 7: 
                    /***
                    try {
                        System.out.print("Enter Group Name: ");
                        gName = in.nextLine();
                        System.out.print("Enter Title: ");
                        bTitle = in.nextLine();
                        System.out.print("Enter Publisher Name: ");
                        pName  = in.nextLine(); 
                        System.out.print("Enter Year Published: ");
                        int year = in.nextInt();
                        System.out.print("Enter Number Of Pages: ");
                        int pages = in.nextInt();
                        state = "INSERT INTO Book (gName, bTitle, pName, yearPublished, numberPages) VALUES (?, ?, ?, ?, ?)";
                        pState = conn.prepareStatement(state);
                        pState.setString(1, gName);
                        pState.setString(2, bTitle);
                        pState.setString(3, pName);
                        pState.setInt(4, year);
                        pState.setInt(5, pages);
                        pState.executeUpdate();
                        pState.close();
                        System.out.println("Book inserted!");
                    } catch (SQLIntegrityConstraintViolationException se) {
                        if (se.getMessage().contains("book_fk")) { //error on foreign key 
                            System.out.println("Invalid Book Name!");
                        } else if (se.getMessage().contains("book_fk_2")) {
                            System.out.println("Invalid Publisher Name!");
                        } else if (se.getMessage().contains("book_uk1")) {
                            System.out.println("Cannot have duplicate book!");
                        } else {
                            System.out.println("Error inserting book!");
                        }
                    }
                     **/
                    break;
                case 8: 
                    /***
                    try {
                    *   System.out.print("Enter Book title: ");
                        bTitle = in.nextLine();
                        System.out.print("Enter Group Name: ");
                        gName = in.nextLine();
                        state = "DELETE FROM Book WHERE bTitle = ? AND gname = ?";
                        pState = conn.prepareStatement(state);
                        pState.setString(1, bTitle);
                        pState.setString(2, gName);
                        pState.executeUpdate();
                        pState.close();
                        System.out.println("Book removed!");
                    } catch (SQLIntegrityConstraintViolationException se) {
                        if (se.getMessage().contains("book_fk")) { //error on foreign key 
                            System.out.println("Invalid Book Name!");
                        } else if (se.getMessage().contains("book_fk_2")) {
                            System.out.println("Invalid Publisher Name!");
                        } else {
                            System.out.println("Error removing book!");
                        }
                    } **/
                    break;
                case 9: 
                    //NO CHECK FOR UNIQUE TABLE
                    System.out.print("Enter New Publisher Name: ");
                    pName = in.nextLine();
                    try {
                        System.out.print("Enter Publisher Address: ");
                        String pAddress = in.nextLine();
                        System.out.print("Enter Publisher Phone Number: ");
                        String pPhone = in.nextLine();
                        System.out.print("Enter Publisher Email: ");
                        String pEmail = in.nextLine();
                        state = "INSERT INTO Publisher (pName, pAddress, pPhone, pEmail) VALUES (?, ?, ?, ?)";
                        pState = conn.prepareStatement(state);
                        pState.setString(1, pName);
                        pState.setString(2, pAddress);
                        pState.setString(3, pPhone);
                        pState.setString(4, pEmail);
                        pState.executeUpdate();
                        pState.close();
                    } catch (SQLException se) {
                        System.out.println("INSERT Error: " + se.getErrorCode());
                    }
                    
                    //gets information about old publisher to make the switch
                    try {
                        System.out.print("Enter Old Publisher: ");
                        String oldPName = in.nextLine();
                        state = "UPDATE Book SET pName  = ? WHERE pName = ?";
                        pState = conn.prepareStatement(state);
                        pState.setString(1, pName);
                        pState.setString(2, oldPName);
                        pState.executeUpdate();
                        System.out.println("Switch sucessful!");
                    } catch (SQLException se) {
                        System.out.println("UPDATE Error: " + se.getErrorCode());
                    }
                    break;
                case 10: 
                    exit = true; 
                    break;
            }

             //if the user hasnt exited out, give option to exit
            if(!exit) {
                System.out.print("\n1) Return to Main Menu \n2) Exit\n\nSelection: ");
                choice = in.nextInt();
                switch (choice) {
                    case 1:
                        exit = false;
                        break;
                    case 2:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid Selection!");
                        break;
                }
            } 
        } while (!exit);
        
        System.out.println("Goodbye!");
    }
    
    public static void main(String[] args) {
        userConnectPrompt();
        establishConnection();
        Menu();
    }
}
