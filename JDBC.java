package JDBCProject;

import java.sql.*;
import java.util.Scanner;

public class JDBC {
    //  Database credentials
    static String USER = "root";
    static String PASS = "pass";
    static String DBNAME = "JDBC";
    
    //connection var 
    static Connection conn = null; //initialize the connection
    
    
    static final String displayFormat="%-5s%-15s%-15s%-15s\n";
    
// JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
    

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
        Scanner in = new Scanner(System.in);
        System.out.print("Name of the database (not the user account): ");
        DBNAME = in.nextLine();
        System.out.print("Database user name: ");
        USER = in.nextLine();
        System.out.print("Database password: ");
        PASS = in.nextLine();
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
    
    public static void listAllWritingGroups(){
       
        try{
            
            Statement stmt = conn.createStatement();
            
            String sql = "SELECT gName FROM writingGroup";
            
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
    
    public static void listDataOfGroup(String groupName){
        
        String sql = "Select headWriter,yearFounded,subject FROM writingGroup WHERE gName  = "+"\'"+groupName+"\'";
                    
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
    
    
    public static void main(String[] args) {
        
        establishConnection();
        
        //testfunctions
        //listAllWritingGroups();
        listDataOfGroup("Algorithms Group");
        
        try {
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } 
        
        
        System.out.println("Goodbye!");
    }
}
