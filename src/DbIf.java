import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import java.net.URL; 
import java.net.URLConnection; 

public class DbIf {
	
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
			System.err.println(e);
		}
	}
	
	/*
	 * load stock data and write it to database
	 */
    public void writeDB(String DBname, BufferedReader reader) {
    	//Connection conn = null;
	    // create a database connection
	    Connection conn = openDBConnection(DBname);
	    try
	    {
	        Statement statement = conn.createStatement();
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
    	}
	    //close database connection
	    closeDBConnection(conn);
    }
    
    /*
     * read database, return single row
     */
    public Table readDB(Connection conn, Table table) {
    	try {
    		Statement statement = conn.createStatement();
    		ResultSet rs = statement.executeQuery("select * from stock order by date");
    		while(rs.next()) {
    			// read the result set
    			System.out.print(rs.getString("date") + " ");  
    			System.out.print(rs.getString("ticker") + " ");
    			System.out.print(rs.getDouble("adjclose") + "\n");
    		}
    	}  catch(SQLException e) {
	    	//connection close failed.
	    	System.err.println(e);    		
    	}
    	return;   	
    }
	
	/*
	* initialize database: Create table, load data and populate tables.
	* Done once when the program starts if the database not created
	*/
    public void initDb(String nameDb, Connection connection) {
    	//Connection connection = null;
    	//check if database doesn't exist
	    if (!(new File(nameDb).exists())) {
	    	// create a database connection
	    	try {		
	    		//connection = DriverManager.getConnection("jdbc:sqlite:" + nameDb);
	    		//Statement statement = connection.createStatement();
	    		//create table: date, ticker, open, high, low, close and adjusted close
	    		statement.executeUpdate("drop table if exists stock");
	    		statement.executeUpdate("create table stock (date text, ticker string, open real, high real, low real, close real, volume integer, adjclose real)");
    	    } catch(SQLException e) {
    	    	System.err.println(e.getMessage());
    	    } finally {
    		    try {
    		    	if(connection != null)
    		    		connection.close();
    		    } catch(SQLException e) {
    		    	//connection close failed.
    		    	System.err.println(e);
    		    }
    	    }
        }
	    //load stock data from Yahoo and write it to database
	    
    }
    
    
    
    
    
    
    
	
	public static void main(String[] args) throws ClassNotFoundException {
		
	    // load the sqlite-JDBC driver
	    Class.forName("org.sqlite.JDBC");
	    

	
	    
	    
	    Connection connection = null;
	    try
	    {
	    	// create a database connection
	        connection = DriverManager.getConnection("jdbc:sqlite:sp500.db");
	        Statement statement = connection.createStatement();
	      
	        statement.executeUpdate("drop table if exists stock");
	        statement.executeUpdate("create table stock (date text, ticker string, open real, high real, low real, close real, volume integer, adjclose real)");
	      try {
	    	  URL yahoo = new URL("http://ichart.finance.yahoo.com/table.csv?s=YHOO&a=00&b=1&c=2011&d=00&e=1&f=2012&g=d&ignore=.csv"); 
	          URLConnection yahooConn = yahoo.openConnection();             
	          BufferedReader reader = new BufferedReader(new InputStreamReader(yahooConn.getInputStream()));    	  
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
	      }
	      
	      ResultSet rs = statement.executeQuery("select * from stock order by date");
	      //rs.last();
	      while(rs.next()) {
	    	  // read the result set
	          System.out.print(rs.getString("date") + " ");  
	          System.out.print(rs.getString("ticker") + " ");
	          System.out.print(rs.getFloat("adjclose") + "\n");
	      }
	    }
	    catch(SQLException e) {
	      System.err.println(e.getMessage());
	    }
	    finally
	    {
	      try {
	        if(connection != null)
	          connection.close();
	      }
	      catch(SQLException e) {
	        //connection close failed.
	        System.err.println(e);
	      }
	    }
	  }
}

