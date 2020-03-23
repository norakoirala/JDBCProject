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
        String gName;
        String pName;
        String bTitle;
        String error;
        Boolean exit = false; 
        
        //runs til user exits
        do {
            //displays menu
            System.out.println("\nMenu:");
            System.out.println("1) List All Writing Groups");
            System.out.println("2) List Specific Writing Group Data");
            System.out.println("3) List All Publishers");
            System.out.println("4) List Specific Publisher Data");
            System.out.println("5) List All Books");
            System.out.println("6) List Specific Book Data");
            System.out.println("7) Add Book");
            System.out.println("8) Remove Book");
            System.out.println("9) Switch Publishers");
            System.out.println("10) Exit");
            System.out.print("\nSelection: ");
            
            //takes in input
            choice = in.nextInt();
            in.nextLine();
            
           switch(choice) {
                case 1: //List All Writing Groups
                    try { 
                        //executes SELECT statement
                        state = "SELECT gName FROM writingGroup";
                        st = conn.createStatement();
                        rs = st.executeQuery(state);
                        
                        //display and close connections
                        displayData(rs);
                        st.close();
                    } catch (SQLException se) {
                        System.out.println("Error with Option 1!");
                        se.printStackTrace();
                    }
                    break;
                case 2: //List Specific Writing Group Data
                    try {
                        //asks user for input
                        System.out.print("\nWriting Group Name: ");
                        gName = in.nextLine();
                        
                        //executes prepared statement
                        state = "Select headWriter,yearFounded,subject FROM writingGroup WHERE gName = ?";
                        pState = conn.prepareStatement(state);
                        pState.setString(1, gName);
                        rs = pState.executeQuery();
                        
                        //display and close connections
                        displayData(rs);
                        pState.close();
                    } catch (SQLException se) {
                        System.out.println("Error with Option 2!");
                        se.printStackTrace();
                    }
                    break;
                case 3: ////List All Publishers
                    try {
                        //executes SELECT statement
                        state = "SELECT pName from Publisher";
                        st = conn.createStatement();
                        rs = st.executeQuery(state);
                        
                        //display and close connections
                        displayData(rs);
                        st.close();
                    } catch (SQLException se) {
                        System.out.println("Error with Option 3!");
                        se.printStackTrace();
                    }
                    break;
                case 4: //List Specific Publisher Data
                    try {
                        //ask for user input 
                        System.out.print("\nPublisher Name: ");
                        pName = in.nextLine();
                        
                        //executes prepared statement
                        state = "SELECT pAddress, pPhone, pEmail FROM Publisher WHERE pName = ?";
                        pState = conn.prepareStatement(state);
                        pState.setString(1, pName);
                        rs = pState.executeQuery();
                        
                        //display and close connections
                        displayData(rs);
                        pState.close();
                    } catch (SQLException se) {
                        System.out.println("Error with Option 4!");
                        se.printStackTrace();
                    }
                    break;
                case 5: ////List All Books
                    try {
                        //executes SELECT statement
                        state = "SELECT bTitle from Book";
                        st = conn.createStatement();
                        rs = st.executeQuery(state);
                        
                        //display and close connections
                        displayData(rs);
                        st.close();
                    } catch (SQLException se) {
                        System.out.println("Error with Option 5!");
                        se.printStackTrace();
                    }
                    break;
                case 6: //List Specific Book Data
                    try {
                        //asks user for input 
                        System.out.print("\nBook Title: ");
                        bTitle = in.nextLine();
                        System.out.print("Group Name: ");
                        gName = in.nextLine();
                        
                        //executes prepared statement 
                        state = "SELECT pName, yearPublished, numberPages FROM Book WHERE bTitle = ? AND gName = ?";
                        pState = conn.prepareStatement(state);
                        pState.setString(1, bTitle);
                        pState.setString(2, gName);
                        rs = pState.executeQuery();
                        
                        //display and close connections
                        displayData(rs);
                        pState.close();
                    } catch (SQLException se) {
                        System.out.println("Error with Option 6!");
                        se.printStackTrace();
                    }
                    break;
                case 7: //Add Book
                    try {
                        //asks user for input
                        System.out.print("\nEnter Title: ");
                        bTitle = in.nextLine();
                        System.out.print("Enter Group Name: ");
                        gName = in.nextLine();
                        System.out.print("Enter Publisher Name: ");
                        pName  = in.nextLine(); 
                        System.out.print("Enter Year Published: ");
                        int year = in.nextInt();
                        System.out.print("Enter Number Of Pages: ");
                        int pages = in.nextInt();
                        
                        ////executes prepared statement 
                        state = "INSERT INTO Book (gName, bTitle, pName, yearPublished, numberPages) VALUES (?, ?, ?, ?, ?)";
                        pState = conn.prepareStatement(state);
                        pState.setString(1, gName);
                        pState.setString(2, bTitle);
                        pState.setString(3, pName);
                        pState.setInt(4, year);
                        pState.setInt(5, pages);
                        
                        //if lines are changed then user is informed 
                        if (pState.executeUpdate() != 0) {
                           System.out.println("Book inserted!");
                        } else {
                           System.out.println("Book cannot be inserted!");
                        }
                        
                        //close connection
                        pState.close();
                    } catch (SQLException se) { 
                        error = se.getMessage(); //gets error message
                        if (error.contains("'BOOK_FK'")) { //if book_fk is invalid
                            System.out.println("Invalid book name!");
                        } else if (error.contains("'BOOK_FK_2'")) { //if book_fk_2 is invalid
                            System.out.println("Invalid publisher name!");
                        } else if (error.contains("duplicate")) { //if there is duplicates
                            System.out.println("Cannot have duplicate book! ");
                        } else {
                            System.out.println("Error with Option 7!");
                            se.printStackTrace();
                        }
                    } 
                    break;
                case 8: //Remove Book
                    try {
                        //ask user for input
                        System.out.print("\nEnter Book Title: ");
                        bTitle = in.nextLine();
                        System.out.print("Enter Group Name: ");
                        gName = in.nextLine();
                        
                        //executes prepared statement
                        state = "DELETE FROM Book WHERE bTitle = ? AND gname = ?";
                        pState = conn.prepareStatement(state);
                        pState.setString(1, bTitle);
                        pState.setString(2, gName);
                        
                        //if lines are changed then user is informed 
                        if (pState.executeUpdate() != 0) {
                           System.out.println("Book removed!");
                        } else {
                            System.out.println("Book doesn't exist!");
                        }
                        
                        //close connection
                        pState.close();
                    } catch (SQLException se) {
                        System.out.println("Error with Option 8!");
                        se.printStackTrace();
                    } 
                    break;
                case 9: //Switch Publishers
                    try {
                        //ask user for input
                        System.out.print("\nEnter New Publisher Name: ");
                        pName = in.nextLine();
                        System.out.print("Enter Publisher Address: ");
                        String pAddress = in.nextLine();
                        System.out.print("Enter Publisher Phone Number: ");
                        String pPhone = in.nextLine();
                        System.out.print("Enter Publisher Email: ");
                        String pEmail = in.nextLine();
                        
                        ////executes prepared statement
                        state = "INSERT INTO Publisher (pName, pAddress, pPhone, pEmail) VALUES (?, ?, ?, ?)";
                        pState = conn.prepareStatement(state);
                        pState.setString(1, pName);
                        pState.setString(2, pAddress);
                        pState.setString(3, pPhone);
                        pState.setString(4, pEmail);
                        
                        if (pState.executeUpdate() != 0) {
                            //if lines are changed then publisher created
                            System.out.println("Publisher created!");
                            
                            //ask user for input 
                            System.out.print("\nEnter Old Publisher: ");
                            String oldPName = in.nextLine();
                            state = "UPDATE Book SET pName  = ? WHERE pName = ?";
                            
                            //executes prepared statement
                            pState = conn.prepareStatement(state);
                            pState.setString(1, pName);
                            pState.setString(2, oldPName);
                            if (pState.executeUpdate() != 0) { 
                                //if lines are changed then switched 
                                System.out.println("Switch sucessful!");
                            } else { 
                                //else new publisher is deleted
                                System.out.println("Invalid Publisher! Cannot switch publishers.");
                                
                                //executes prepared statement
                                state = "DELETE FROM Publisher WHERE pName = ?";
                                pState = conn.prepareStatement(state);
                                pState.setString(1, pName);
                                pState.executeUpdate();
                            }
                        }
                        
                        //close connection
                        pState.close();
                    } catch (SQLException se) {
                        error = se.getMessage(); //gets error message 
                        if (error.contains("duplicate")) { //if duplicate
                            System.out.println("Cannot have duplicate publishers!");
                        } else {
                            System.out.println("Error with option 9!");
                        }
                    }
                    break;
                case 10: //User exits
                    exit = true; 
                    break;
                default: //Invalid
                    System.out.println("Invalid Selection!");
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
        
        //exit message
        System.out.println("Goodbye!");
    }
    
    public static void main(String[] args) {
        userConnectPrompt();
        establishConnection();
        Menu();
    }
}
