import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;

public class DBIf {

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
        			preparedStatement = conn.prepareStatement("insert or replace into stock values(?,?,?,?,?,?,?,?)");
        			//date
        			preparedStatement.setString(1, dataFields[0]);
        			//ticker
        			preparedStatement.setString(2, name);
        			//open
        			preparedStatement.setDouble(3, Double.parseDouble(dataFields[1]));
        			//high
        			preparedStatement.setDouble(4, Double.parseDouble(dataFields[2]));
        			//low
        			preparedStatement.setDouble(5, Double.parseDouble(dataFields[3]));
        			//close
        			preparedStatement.setDouble(6, Double.parseDouble(dataFields[4]));
        			//volume
        			preparedStatement.setInt(7, Integer.parseInt(dataFields[5]));
        			//adjusted close
        			preparedStatement.setDouble(8, Double.parseDouble(dataFields[6]));
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
    public void readDB(Connection conn, String ticker) { //throws SQLException {
    	Statement statement = null;
    	try {
    		statement = conn.createStatement();
    		ResultSet rs = statement.executeQuery("select * from stock where ticker = '" + ticker + "' order by date");
    		while(rs.next()) {
    			// read the result set
    			//System.out.println(rs.getDouble("adjclose") + " ");  
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
    		//create unique index
    		statement.executeUpdate("CREATE UNIQUE INDEX stock_idx ON stock(date, ticker)");
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
	
}
	