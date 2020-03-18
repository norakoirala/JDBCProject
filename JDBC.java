package JDBCProject;

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
    
    
    static final String displayFormat="%-5s%-15s%-15s%-15s\n";
    
// JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";

    
    public static void main(String[] args) {
        
        userConnectPrompt();
        establishConnection();
        
        Menu();
        
        try {
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } 
        
        
        System.out.println("Goodbye!");
    }
    
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
    
    //Sets up connection to database with given values set
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
    
    //Lets the user specify the connection variables
    public static void userConnectPrompt(){
        
        System.out.print("Name of the database (not the user account): ");
        DBNAME = in.nextLine();
        System.out.print("Database user name: ");
        USER = in.nextLine();
        System.out.print("Database password: ");
        PASS = in.nextLine();
    }
    
    //Executes sql that returns a table
    public static void exeQuery(String sql){
        try{
            Statement stmt = conn.createStatement();
            
            ResultSet rs = stmt.executeQuery(sql);
            
            ResultSetMetaData rmd = rs.getMetaData();
            int columnCount = rmd.getColumnCount();
            
            printData(rs,columnCount);
            
            rs.close();
            stmt.close();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    //Executes sql that modifies a table
    public static void exeUpdate(String sql){
        try{
            Statement stmt = conn.createStatement();
            
            stmt.executeUpdate(sql);
            
            stmt.close();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    //Print a table given results
    public static void printData(ResultSet rs, int columnCount) throws SQLException {
        while(rs.next()){
                System.out.print("Row: "+rs.getRow());
                
                for(int i =1; i<=columnCount; i++){
                    System.out.print(" | " +rs.getString(i));
                }
                System.out.print("|\n");
            }
    }
    
    //Menu prompt for the user 
    public static void Menu(){
        
        
        System.out.println("Please Select From Menu:");
        System.out.println("1) List All Writing Groups");
        System.out.println("2) List Data Of Group");
        System.out.println("3) List All Publishers");
        System.out.println("4) List Data Of Publishers");
        System.out.println("5) List All Books");
        System.out.println("6) List Data Of Books");
        System.out.println("7) Add New Book");
        System.out.println("8) Switch Publishers");
        System.out.println("9) Remove Book");
        
        String sql = null;
        
        try{
            switch(in.nextInt()){
                case 1: sql = "SELECT gName FROM writingGroup"; break;
                case 2: sql = listDataOfTable("writingGroup"); break;
                case 3: sql = "SELECT pName FROM Publisher "; break;
                case 4: sql = listDataOfTable("Publisher"); break;
                case 5: sql = "SELECT gName FROM Book"; break;
                case 6: sql = listDataOfTable("Book"); break;
                case 7: insertBook(); break;
                case 8: createSwitchPublisher(); break;
                case 9: removeBook(); break;
            }
            
            //dont exe if sql is not set
            if(sql != null)
                exeQuery(sql);
        }
        catch(Exception se){
            System.out.println("Enter an integer!");
        }
        
    }
    
    //List data of a table
    public static String listDataOfTable(String tableName){
        
        in.nextLine();
        String name;
        
        System.out.print("Enter "+ tableName+" Name: ");
        name=in.nextLine();
        
        if(tableName.equals("Book")){
            System.out.print("Enter Group Name: ");
            String gName = in.nextLine();
            return "Select headWriter,yearFounded,subject FROM " + name +"\'"+ "AND gname = \'"+gName+"\'";
        }
        
        return "Select headWriter,yearFounded,subject FROM "+ tableName +" WHERE gName  = "+"\'"+name+"\'";
        
    }
    
    //Prompts the user to insert a book
    public static void insertBook(){
        in.nextLine();
        
        String sql="";
        
        System.out.print("Enter Group Name: ");
           String groupName = in.nextLine();
        
        System.out.print("Enter Title: ");
            String title = in.nextLine();
            
        System.out.print("Enter Publisher Name: ");
            String publisherName  = in.nextLine();
        
        try{    
            System.out.print("Enter Year Published: ");
                int year = in.nextInt();
                
            System.out.print("Enter Number Of Pages: ");
                int pages = in.nextInt();
                
            sql = "INSERT INTO Book (gName, bTitle, pName, yearPublished, numberPages) VALUES (\'"+groupName+"\', \'"
                    + title + "\', \'"
                    + publisherName + "\', "
                    + year +", "
                    + pages+" )";
           
        }catch(Exception e){
            System.out.println("Enter a number for year or pages!");
        }
        
        System.out.println(sql);
        exeUpdate(sql);
        
    }
    
    //Prompts the user to enter a new publisher
    public static void newPublisher(String pName){
        String sql="";
        
        System.out.print("Enter Publisher Address: ");
            String pAddress = in.nextLine();
            
        System.out.print("Enter Publisher Phone Number: ");
            String pPhone = in.nextLine();
        
        System.out.print("Enter Publisher Email: ");
            String pEmail = in.nextLine();
           
        sql = "INSERT INTO Publisher (pName, pAddress, pPhone, pEmail) VALUES (\'"
                    +pName+"\', \'"
                    + pAddress + "\', \'"
                    + pPhone + "\', \'"
                    + pEmail +"\')";
        
        //debug
        System.out.println(sql);
        
        exeUpdate(sql);
    }
    
    //Creates a publisher and switches all books from another given publisher
    public static void createSwitchPublisher(){
        in.nextLine();
        
        String sql="";
        
        System.out.print("Enter New Publisher Name: ");
           String pName = in.nextLine();
           
        newPublisher(pName);
        
        System.out.print("Enter Old Publisher: ");
            String old_pName = in.nextLine();
        
        sql = "UPDATE Book SET pName  = \'"+pName+ "\' WHERE pName = \'"+ old_pName + "\' ";
        System.out.println(sql);
        exeUpdate(sql);
    }
    
    //Removes a book given primary key
    public static void removeBook(){
        String sql="";
        
        in.nextLine();
        
        System.out.print("Enter Book title: ");
            String bTitle = in.nextLine();
            
        System.out.print("Enter Group Name: ");
            String gName = in.nextLine();
            
        sql = "DELETE FROM Book WHERE bTitle = \'"+ bTitle+"\'"+ "AND gname = \'"+gName+"\'";
        
        exeUpdate(sql);
        
    }
    
}
    
 