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
    
    public static void userConnectPrompt(){
        
        System.out.print("Name of the database (not the user account): ");
        DBNAME = in.nextLine();
        System.out.print("Database user name: ");
        USER = in.nextLine();
        System.out.print("Database password: ");
        PASS = in.nextLine();
    }
    
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
    
    public static void printData(ResultSet rs, int columnCount) throws SQLException {
        while(rs.next()){
                System.out.print("Row: "+rs.getRow());
                
                for(int i =1; i<=columnCount; i++){
                    System.out.print(" | " +rs.getString(i));
                }
                System.out.print("|\n");
            }
    }
    
    public static void Menu(){
        
        
        System.out.println("Please Select From Menu:");
        System.out.println("1) List All Writing Groups");
        System.out.println("2) List Data Of Group");
        System.out.println("3) List All Publishers");
        
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
                
            }
            
            exeQuery(sql);
        }
        catch(Exception se){
            System.out.println("Enter an integer!");
        }
        
    }
    
    
    public static String listDataOfTable(String tableName){
        
        in.nextLine();
        String name;
        
        System.out.print("Enter "+ tableName+" Name: ");
        name=in.nextLine();
        
        
        return "Select headWriter,yearFounded,subject FROM "+ tableName +" WHERE gName  = "+"\'"+name+"\'";
        
    }
    
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
            
           System.out.println(sql);
           
        }catch(Exception e){
            System.out.println("Enter a number for year or pages!");
        }
        
        exeUpdate(sql);
        
    }
    
    
    
}
    
 