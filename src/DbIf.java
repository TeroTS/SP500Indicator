import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import java.net.URL; 
import java.net.URLConnection; 

public class DbIf {
	
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

