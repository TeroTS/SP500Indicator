import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import java.util.HashMap;
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
    public void writeDB(Connection conn, BufferedReader reader, String name) { //throws SQLException {
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
        					"'" + name + "'" + "," + dataFields[1] + "," + dataFields[2] + "," + dataFields[3] + "," + dataFields[4] + "," + 
        					dataFields[5] + "," + dataFields[6] + ")");    
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
	    stockProp.put("ticker", "intc");
	    stockProp.put("fromMonth", "0");
	    stockProp.put("fromDay", "1");
	    stockProp.put("fromYear", "2012");
	    stockProp.put("toMonth", "8");
	    stockProp.put("toDay", "1");
	    stockProp.put("toYear", "2012");
	    stockProp.put("freq", "d");
	    
	    
	    DBIf dBIf = new DBIf();
	    //YahooIf yahooIf = new YahooIf(stockProp);
	    
	    //BufferedReader bufferedReader = null;
	    
    	// create a database connection
        Connection connection = dBIf.openDBConnection("sp500.db");
        //create table
        dBIf.createTable(connection);
        
	    try {
	    	//read stock tickers from file,
	    	//one ticker per line
	    	File file = new File("SP500.txt");
	    	FileReader fileReader = new FileReader(file);
	    	BufferedReader buffReader = new BufferedReader(fileReader);
	        
	    	String line = null;
	    	while ((line = buffReader.readLine()) != null) {
	    		//set ticker
	    		stockProp.put("ticker", line);
	    		//create new Yahoo connection for every stock
	    		YahooIf yahooIf = new YahooIf(stockProp);
	    		//connect Yahoo and read data
	    		BufferedReader bufferedReader = yahooIf.openYahooConnection();
		        //write database
		        dBIf.writeDB(connection, bufferedReader, stockProp.get("ticker"));
		        //close connection
		        yahooIf.closeYahooConnection(bufferedReader);
	    		
	    	}
	    	buffReader.close();
	    } catch(IOException e) {
	    	e.printStackTrace();
	    }
	    	
	        //create table
	        //dBIf.createTable(connection);
	        //connect Yahoo
	        //bufferedReader = yahooIf.openYahooConnection();
	        //write database
	        //dBIf.writeDB(connection, bufferedReader);
	        
	        
	        //read database
	        dBIf.readDB(connection);
/*	    } catch(SQLException e) {
	    	System.err.println(e.getMessage());
		} finally {	       
			dBIf.closeDBConnection(connection);
		} */
	    
    }
}
	