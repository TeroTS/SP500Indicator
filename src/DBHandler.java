import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBHandler {
	
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
    public void writeDB(Connection conn, ArrayList<StockItem> stockList, String tableName) { //throws SQLException {
    	//Connection conn = null;
    	Statement statement = null;
    	//prepared statement to speed up the write
    	PreparedStatement preparedStatement = null;
	    try
	    {
	        statement = conn.createStatement();
	        //read buffer from download site
	        //String line = null;
        	//int i = 0;
        	for (StockItem index : stockList) {
        		preparedStatement = conn.prepareStatement("insert or replace into " + tableName + " values(?,?,?,?,?,?,?,?,?,?)");
    			//date
    			preparedStatement.setString(1, index.getDate());
    			//ticker
    			preparedStatement.setString(2, index.getTicker());
    			//open
    			preparedStatement.setDouble(3, index.getOpen());   
    			//high
    			preparedStatement.setDouble(4, index.getHigh());   
    			//low
    			preparedStatement.setDouble(5, index.getLow());   
    			//close
    			preparedStatement.setDouble(6, index.getClose());   
    			//volume
    			preparedStatement.setInt(7, index.getVolume());   
    			//adjusted close
    			preparedStatement.setDouble(8, index.getAdjClose());
    			//moving average (ma)
    			preparedStatement.setDouble(9, index.getMa());
    			//close under ma
    			preparedStatement.setInt(10, index.getUnderMa());    			
    			//execute
    			preparedStatement.executeUpdate();
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
    public ArrayList<StockItem> readDB(Connection conn, String command) { //throws SQLException {
    	ResultSet rs = null; //new ResultSet(); //null;
    	Statement statement = null;
    	ArrayList<StockItem> stockList = new ArrayList<StockItem>();
    	
    	try {
    		statement = conn.createStatement();
    		//read the last n rows of the database
    		rs = statement.executeQuery(command);
    		//System.out.println(rs.next());
    		while(rs.next()) {
    			StockItem stockItem = new StockItem();
    			stockItem.setDate(rs.getString("date"));
    			stockItem.setTicker(rs.getString("ticker"));
    			stockItem.setOpen(rs.getDouble("open"));
    			stockItem.setHigh(rs.getDouble("high"));
    			stockItem.setLow(rs.getDouble("low"));
    			stockItem.setClose(rs.getDouble("close"));
    			stockItem.setVolume(rs.getInt("volume"));
    			stockItem.setAdjClose(rs.getDouble("adjClose")); 
    			stockItem.setMa(rs.getDouble("ma"));
    			stockItem.setUnderMa(rs.getInt("underMa"));
    			stockList.add(0, stockItem);
  
    			//System.out.print(rs.getString("date") + " ");  
    			//System.out.print(rs.getString("ticker") + " ");
    			//System.out.print(rs.getDouble("adjClose") + "\n");
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
    	return stockList;
    }
    
    /*
     * create table
     */
	public void createTable(Connection conn, String tableName) { //throws SQLException {
		//if (!(new File(DBname).exists())) {	
		Statement statement = null;
		String tableString = "create table " + tableName + " (date text not null, ticker string not null, " +
							 "open real, high real, low real, close real, " +
							 "volume integer, adjClose real, ma real, underMa integer, primary key (date, ticker))";
		
    	try {		
    		statement = conn.createStatement();
    		statement.executeUpdate("drop table if exists " + tableName);
    		statement.executeUpdate(tableString);
    		//create unique index
    		//statement.executeUpdate("CREATE UNIQUE INDEX stock_idx ON stock(date, ticker)");
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
	