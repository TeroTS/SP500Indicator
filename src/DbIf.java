import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
//import java.net.URL; 
//import java.net.URLConnection; 

public class DBIf {
	
	static final String NAME_DB = "sp500.db"; 
	
	//private YahooIf YahooIf;
	
	/*
	 * open connection to database
	 */
	public Connection openDBConnection(String name) {
	    Connection conn= null;
	    try
	    {
	    	// create a database connection
	        conn = DriverManager.getConnection("jdbc:sqlite:" + name);
	        //Statement statement = connection.createStatement();
	    } catch(SQLException e) {
		      System.err.println(e.getMessage());
		} 
		return conn;
	}
	
	/*
	 * close connection to database
	 */
	public void closeDBConnection(Connection conn) {
		try {
			if(conn != null)
				conn.close();
		}
		catch(SQLException e) {
			//connection close failed.
			System.err.println(e.getMessage());
		}
	}
	
	/*
	 * load stock data and write it to database
	 */
    public void writeDB(Connection conn, BufferedReader reader) throws SQLException {
    	//Connection conn = null;
    	Statement statement = null;
	    // create a database connection
	    //Connection conn = openDBConnection(DBname);
	    try
	    {
	        statement = conn.createStatement();
	        //read buffer from download site
	        String line = null;
        	int i = 0;
        	while ((line = reader.readLine()) != null) {
        		i++;
        		if (i != 1) {
        			//split data fields
        			String[] dataFields = line.split(",");
        			statement.executeUpdate("insert into stock values(" + "'" + dataFields[0] + "'" + "," +
        					"'YHOO'" + "," + dataFields[1] + "," + dataFields[2] + "," + dataFields[3] + "," + dataFields[4] + "," + 
        					dataFields[5] + "," + dataFields[6] + ")");    
        		}
        	} 
	    } catch(Exception ex) {
	    	  ex.printStackTrace();
    	//} finally {
    	//	if (statement != null)
    	}
	    statement.close();
	    //close database connection
	    //closeDBConnection(conn);
    }
    
    /*
     * read database, one row at a time
     */
    public void readDB(Connection conn) throws SQLException {
    	Statement statement = null;
    	try {
    		statement = conn.createStatement();
    		ResultSet rs = statement.executeQuery("select * from stock order by date");
    		while(rs.next()) {
    			// read the result set
    			System.out.print(rs.getString("date") + " ");  
    			System.out.print(rs.getString("ticker") + " ");
    			System.out.print(rs.getDouble("adjclose") + "\n");
    		}
    	}  catch(SQLException e) {
	    	//connection close failed.
	    	System.err.println(e.getMessage());    		
    	}
    	statement.close();
    	//return;   	
    }
    
    /*
     * create table
     */
	public void createTable(Connection conn) throws SQLException {
		//if (!(new File(DBname).exists())) {	
		Statement statement = null;
		String tableString = "create table stock (date text, ticker string, " +
							 "open real, high real, low real, close real, " +
							 "volume integer, adjclose real)";
		
    	try {		
    		//connection = DriverManager.getConnection("jdbc:sqlite:" + nameDb);
    		statement = conn.createStatement();
    		//create table: date, ticker, open, high, low, close and adjusted close
    		statement.executeUpdate("drop table if exists stock");
    		statement.executeUpdate(tableString);
	    } catch(SQLException e) {
	    	System.err.println(e.getMessage());
	    }	
    	statement.close();
	}
	
	

	public static void main(String[] args) throws ClassNotFoundException {
		
	    // load the sqlite-JDBC driver
	    Class.forName("org.sqlite.JDBC");
	    
	    DBIf dBIf = new DBIf();
	    YahooIf yahooIf = new YahooIf();
	    
	    BufferedReader bufferedReader = null;
	    
	    //create database
	    //Connection connection = null;
    	// create a database connection
        Connection connection = dBIf.openDBConnection("sp500.db");
        
	    try
	    {
	        //create table
	        dBIf.createTable(connection);
	        //connect Yahoo
	        bufferedReader = yahooIf.openYahooConnection();
	        //write database
	        dBIf.writeDB(connection, bufferedReader);
	        //read database
	        dBIf.readDB(connection);
	    } catch(SQLException e) {
	    	System.err.println(e.getMessage());
		} finally {	       
			dBIf.closeDBConnection(connection);
		}
	    
    }
}
	