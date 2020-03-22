package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBC {
    //  Database credentials
    static String USER = "root";
    static String PASS = "pass";
    static String DBNAME = "JDBC";
    
    //user input 
    static Scanner in = new Scanner(System.in);
    
    //connection var 
    static Connection conn = null; //initialize the connection
    
    
    static final String displayFormat="%-5s%-15s%-15s%-15s\n";
    
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";

    
    public static void main(String[] args) {
        userConnectPrompt();
        establishConnection();
        Menu();
    }
    
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
        }
        
        catch(SQLException se) {
           se.printStackTrace();
        }
        catch(Exception e){
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
     * @param sql - takes in SQL Select statement 
     * @return list - used to double check duplicates / validation 
     */
    public static ArrayList displayData(String sql) {
        ArrayList<String> str = new ArrayList<String>();
        
        try{
            //executing statement 
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(sql);
            
            //formatting output
            ResultSetMetaData rmd = rs.getMetaData();
            int columnCount = rmd.getColumnCount();
            String s = String.format("\n%-5s%-15s", " ", rmd.getColumnName(1));
            System.out.println(s + "\n----------------------------------------------------");
            while(rs.next()){
                for(int i =1; i<=columnCount; i++){
                    System.out.printf("%-5s%-15s\n", Integer.toString(rs.getRow()), rs.getString(i));
                    str.add(rs.getString(i)); 
                }
            }
            //closing connections
            state.close();
            rs.close();
        } catch(SQLException se){
            System.out.println("Invalid Table! Table does not exist");
        }
        
        return str;
    }
    
    /**
     * Takes in result set to format info 
     * @param rs 
     */
    public static void displayInfo(ResultSet rs) {
        try {
            ResultSetMetaData rmd = rs.getMetaData();
            int columnCount = rmd.getColumnCount();
            String s = String.format(displayFormat, " ", rmd.getColumnName(1), rmd.getColumnName(2), rmd.getColumnName(3));
            System.out.println(s + "----------------------------------------------------");
            while(rs.next()){
                String rowStr = "";
                for(int i=1; i<=columnCount; i++){
                    rowStr = rowStr +  rs.getString(i) + "!";
                }
                String[] rowStrArr = rowStr.split("!");
                System.out.printf(displayFormat, dispNull(Integer.toString(rs.getRow())), dispNull(rowStrArr[0]), dispNull(rowStrArr[1]), dispNull(rowStrArr[2]));
            }
        } catch (SQLException ex) {
            System.out.println("Error with displaying information!");
        }
    }
    
    
    /**
     * Menu for the user
     */
    public static void Menu() {
        Boolean exit = false;
        //loops til user exits
        do {
            //displays choices user has and takes input
            System.out.println("\nMenu:");
            System.out.print("1)Writing Groups \n2)Publishers \n3)Books \n4)Exit \n\nSelection: ");
            int choice = in.nextInt();
            in.nextLine();
            
            //initializing variables
            String sql;
            int subchoice;
            ArrayList<String> list;

            switch(choice){
                //Writing Groups is selected 
                case 1: 
                    //displays all Writing Groups
                    sql = "SELECT gName FROM writingGroup"; 
                    list = displayData(sql);
                    
                    //submenu is outputted and takes input
                    System.out.print("\n1) View Writing Group Info \n2) Exit Writing Group Menu\n\nSelection: ");
                    subchoice = in.nextInt();
                    in.nextLine();
                    
                    //Viewing specific info is selected
                    if(subchoice == 1) {
                        System.out.print("\nWriting Group Name: ");
                        String gName = in.nextLine();
                        System.out.println();
                        
                        //check if writing group exists and outputs information
                        if(!list.contains(gName)) {
                            System.out.println("Invalid Writing Group! Writing Group does not exist.");
                        } else {
                            try {
                                //prepared statement executed
                                String state = "Select headWriter,yearFounded,subject FROM writingGroup WHERE gName = ?";
                                PreparedStatement pState = conn.prepareStatement(state);
                                pState.setString(1, gName);
                                ResultSet rs = pState.executeQuery();
                                conn.commit();
                                
                                //formatting
                                displayInfo(rs);
                                
                                //closing connections
                                pState.close();
                                rs.close();
                            } catch (SQLException ex) {
                                System.out.println("Invalid Writing Group! Writing Group does not exist.");
                            }
                        }   
                    //if user does not view or exit, selection is invalid
                    } else if (subchoice != 2) {
                       System.out.println("Invalid Selection!");
                    }
                    
                    break;
                    
                //Publisher is selected
                case 2: 
                    //displays all publishers
                    sql = "SELECT pName FROM Publisher ";
                    list = displayData(sql);
                    
                    //submenu is outputted and takes input
                    System.out.println("\n1) View Publisher Info \n2) Move Publishers \n3) Exit Publisher Menu\n\nSelection: ");
                    subchoice = in.nextInt();
                    in.nextLine();
                    
                    //view specific info is selected 
                    if(subchoice == 1) {
                        System.out.print("\nPublisher Name: ");
                        String pName = in.nextLine();
                        System.out.println();
                        
                        //checks if publisher exists and outputs information
                        if(!list.contains(pName)) {
                            System.out.println("Invalid Publisher! Publisher does not exist.");
                        } else {
                            try {
                                //prepared statement executed 
                                String state = "Select pAddress, pPhone, pEmail FROM Publisher WHERE pName = ?";
                                PreparedStatement pState = conn.prepareStatement(state);
                                pState.setString(1, pName);
                                ResultSet rs = pState.executeQuery();
                                conn.commit();
                                
                                //formatting
                                displayInfo(rs);
                                
                                //closing connections
                                pState.close();
                                rs.close();
                            } catch (SQLException ex) {
                                System.out.println("Invalid Publisher! Publisher does not exist.");
                            }
                        }
                    //switching publishers is selected
                    } else if (subchoice == 2){
                        //verifies if publisher is duplicate or not 
                        System.out.print("Enter New Publisher Name: ");
                        String pName = in.nextLine();
                        if(list.contains(pName)) {
                            System.out.println("Invalid Publsiher Name! Publisher cannot be created.");
                        } else {
                            //creates new publisher with given info
                            System.out.print("Enter Publisher Address: ");
                            String pAddress = in.nextLine();
                            System.out.print("Enter Publisher Phone Number: ");
                            String pPhone = in.nextLine();
                            System.out.print("Enter Publisher Email: ");
                            String pEmail = in.nextLine();
                            try {
                                //executes preparedstatement 
                                String state = "INSERT INTO Publisher (pName, pAddress, pPhone, pEmail) VALUES (?, ?, ?, ?)";
                                PreparedStatement pState = conn.prepareStatement(state);
                                pState.setString(1, pName);
                                pState.setString(2, pAddress);
                                pState.setString(3, pPhone);
                                pState.setString(4, pEmail);
                                pState.executeUpdate();
                                conn.commit();
                            } catch (SQLException se) {
                                System.out.println("Invalid entry! Publisher cannot be created.");
                            }
                        }
                        
                        //gets information about old publisher to make the switch
                        System.out.print("Enter Old Publisher: ");
                        String oldPName = in.nextLine();
                        try {
                            String state = "UPDATE Book SET pName  = ? WHERE pName = ?";
                            PreparedStatement pState = conn.prepareStatement(state);
                            pState.setString(1, pName);
                            pState.setString(2, oldPName);
                            pState.executeUpdate();
                            conn.commit();
                        } catch (SQLException se) {
                            System.out.println("Invalid entry! Publisher cannot be switched.");
                        }
                        
                        //Validation message
                        System.out.println("Switch sucessful!");
                    //if user does not view or switch or exit, selection is invalid
                    } else if (subchoice != 3) {
                        System.out.println("Invalid Selection!");
                    }
                    
                    break;
                case 3:
                    //TO DO: STILL NEEDS WORK
                    sql = "SELECT bTitle FROM Book"; 
                    list = displayData(sql);
                    System.out.println("\n1) View Book Info \n2) Add Book \n3) Remove Book \n4) Exit Books Menu\n\nSelection: ");
                    subchoice = in.nextInt();
                    in.nextLine();
                    System.out.println();
                    if (subchoice == 1) {
                        System.out.print("\nBook Name: ");
                        String bName = in.nextLine();
                        System.out.print("Group Name: ");
                        String gName = in.nextLine();
                        if (!list.contains(bName)) {
                            System.out.println("Invalid Book! Book does not exist.");
                        } else {
                            try {
                                String state = "Select pName, yearPublished, numberPages FROM Publisher WHERE bName = ? AND gName = ?";
                                PreparedStatement pState = conn.prepareStatement(state);
                                pState.setString(1, bName);
                                pState.setString(2, gName);
                                ResultSet rs = pState.executeQuery();
                                conn.commit();
                                displayInfo(rs);
                                pState.close();
                                rs.close();
                            } catch (SQLException ex) {
                                System.out.println("Invalid Publisher! Publisher does not exist.");
                            }
                        }
                    } else if (subchoice == 2) {
                        System.out.print("Enter Group Name: ");
                        String gName = in.nextLine();
                        System.out.print("Enter Title: ");
                        String bTitle = in.nextLine();
                        System.out.print("Enter Publisher Name: ");
                        String pName  = in.nextLine(); 
                        System.out.print("Enter Year Published: ");
                        int year = in.nextInt();
                        System.out.print("Enter Number Of Pages: ");
                        int pages = in.nextInt();
                        try {
                            String state = "INSERT INTO Book (gName, bTitle, pName, yearPublished, numberPages) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement pState = conn.prepareStatement(state);
                            pState.setString(1, gName);
                            pState.setString(2, bTitle);
                            pState.setString(3, pName);
                            pState.setInt(4, year);
                            pState.setInt(5, pages);
                            pState.executeUpdate();
                            conn.commit();
                        } catch (SQLException se) {
                            System.out.println("Invalid entry! Book cannot be inserted.");
                        }
                    } else if (subchoice == 3) {
                        System.out.print("Enter Book title: ");
                        String bTitle = in.nextLine();
                        System.out.print("Enter Group Name: ");
                        String gName = in.nextLine();
                        try {
                          String state = "DELETE FROM Book WHERE bTitle = ? AND gname = ?";
                          PreparedStatement pState = conn.prepareStatement(state);
                          pState.setString(1, bTitle);
                          pState.setString(2, gName);
                          pState.executeUpdate();
                          conn.commit();
                        } catch (SQLException se) {
                            System.out.println("Invalid entry! Book cannot be removed!");
                        }
                    } else if (subchoice != 4) {
                        System.out.println("Invalid Selection!");
                    }
                    break;
                case 4: 
                    exit = true; 
                    break;
            }
            
            //if user has not exited from sub-menu, give final option to exit
            if(!exit) {
                System.out.print("\n1) Return to Main Menu \n2) Exit\n\nSelection: ");
                choice = in.nextInt();
                if (choice == 2) {
                    exit = true;
                } 
            } 

        }while(!exit);
        
        try {
            conn.close();
        } catch (SQLException se) {
        } 
       
        System.out.println("\nGoodbye!");
    }
    
}
    
