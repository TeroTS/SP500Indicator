import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import java.util.Calendar;
import java.util.HashMap;
//import java.net.URL; 
//import java.net.URLConnection; 


public class DBIf {
	
	static final String NAME_DB = "jdbc:sqlite:sp500.db"; 
	//static final String TICKER_FILE = "SP500.txt";
	
	//private YahooIf YahooIf;
	
	/*
	 * open connection to database
	 */
	public Connection openDBConnection(String name) {
	    Connection conn= null;
	    try
	    {
	    	// create a database connection
	        conn = DriverManager.getConnection(name);
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
    public void writeDB(Connection conn, BufferedReader reader, String name) { //throws SQLException {
    	//Connection conn = null;
    	Statement statement = null;
    	//prepared statement to speed up the write
    	PreparedStatement preparedStatement = null;
	    try
	    {
	        statement = conn.createStatement();
	        //read buffer from download site
	        String line = null;
        	int i = 0;
        	while ((line = reader.readLine()) != null) {
        		i++;
        		//dont read the first line
        		if (i != 1) {
        			//split data fields
        			String[] dataFields = line.split(",");
        			preparedStatement = conn.prepareStatement("insert into stock values(?,?,?,?,?,?,?,?)");
        			//date
        			preparedStatement.setString(1, dataFields[0]);
        			//ticker
        			preparedStatement.setString(2, name);
        			//open
        			preparedStatement.setString(3, dataFields[1]);
        			//high
        			preparedStatement.setString(4, dataFields[2]);
        			//low
        			preparedStatement.setString(5, dataFields[3]);
        			//close
        			preparedStatement.setString(6, dataFields[4]);
        			//volume
        			preparedStatement.setString(7, dataFields[5]);
        			//adjusted close
        			preparedStatement.setString(8, dataFields[6]);
        			//execute
        			preparedStatement.executeUpdate();

        			//statement.executeUpdate("insert into stock values(" + "'" + dataFields[0] + "'" + "," +
        			//		"'" + name + "'" + "," + dataFields[1] + "," + dataFields[2] + "," + dataFields[3] + "," + dataFields[4] + "," + 
        			//		dataFields[5] + "," + dataFields[6] + ")");    
        		}
        	} 
	    } catch(Exception ex) {
	    	  ex.printStackTrace();
 
    	} finally {
	    	try {
	    		statement.close();
	    	} catch(SQLException e) {
	    		System.err.println(e.getMessage());
	    	}
	    }
    }
    
    /*
     * read database, one row at a time
     */
    public void readDB(Connection conn) { //throws SQLException {
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
    	} finally {
	    	try {
	    		statement.close();
	    	} catch(SQLException e) {
	    		System.err.println(e.getMessage());
	    	}
	    }
    }
    

    
    /*
     * create table
     */
	public void createTable(Connection conn) { //throws SQLException {
		//if (!(new File(DBname).exists())) {	
		Statement statement = null;
		String tableString = "create table stock (date text, ticker string, " +
							 "open real, high real, low real, close real, " +
							 "volume integer, adjclose real)";
		
    	try {		
    		statement = conn.createStatement();
    		statement.executeUpdate("drop table if exists stock");
    		statement.executeUpdate(tableString);
	    } catch(SQLException e) {
	    	System.err.println(e.getMessage());
	    } finally {
	    	try {
	    		statement.close();
	    	} catch(SQLException e) {
	    		System.err.println(e.getMessage());
	    	}
	    }
	}
	
	
	public static void main(String[] args) throws ClassNotFoundException {
		
	    // load the sqlite-JDBC driver
	    Class.forName("org.sqlite.JDBC");
	   
	    //load S&P500 stocks (from file) using properties !!!!
	    
	    //test stock properties
	    HashMap<String, String> stockProp = new HashMap<String, String>();
	    //stockProp.put("ticker", "intc");
	    stockProp.put("fromMonth", "0");
	    stockProp.put("fromDay", "1");
	    stockProp.put("fromYear", "2012");
	    stockProp.put("toMonth", "8");
	    stockProp.put("toDay", "1");
	    stockProp.put("toYear", "2012");
	    stockProp.put("freq", "d");
	    
	    
	    DBIf dBIf = new DBIf();
    	// create a database connection
        Connection connection = dBIf.openDBConnection(NAME_DB);
        //create table
        dBIf.createTable(connection);
        //update database
        .updateDB(stockProp, connection);
	    //read database
	    dBIf.readDB(connection);
	    
    }
}
	