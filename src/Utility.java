/*
 * utility class, includes some misc. utility methods
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.HashMap;

public class Utility {
	
	static final String TICKER_FILE = "SP500.txt";
	
	DBIf dBIf = new DBIf();
	
	private String toDay;
	private String toMonth;
	private String toYear;
    private String fromDay;         
    private String fromMonth;         
    private String fromYear;
    
	/*
	 * add n months to date
	 */
	private void addDate(int n) {
		Calendar now = Calendar.getInstance();
	    //get current day, month and year
	    toDay = Integer.toString(now.get(Calendar.DATE));        
	    toMonth = Integer.toString(now.get(Calendar.MONTH) + 1);         
	    toYear = Integer.toString(now.get(Calendar.YEAR));
	    //add n months to date
	    now.add(Calendar.DATE, n);
	    fromDay = Integer.toString(now.get(Calendar.DATE));         
	    fromMonth = Integer.toString(now.get(Calendar.MONTH) + 1);         
	    fromYear = Integer.toString(now.get(Calendar.YEAR));		
	}

	/*
	 * update database with new data
	 */
	public void updateDB(Connection conn, String DBname) {
		
		//stock data length properties (fromDate -> toDate)
		HashMap<String, String> stockProp = new HashMap<String, String>();	
	
	    try {
	    	//read stock tickers from file,
	    	//one ticker per line
	    	File file = new File(TICKER_FILE);
	    	FileReader fileReader = new FileReader(file);
	    	BufferedReader buffReader = new BufferedReader(fileReader);
	    	//get the current date
	    	
	    	//if the database doesn't exist, create table and fill the database 
	    	//with initial data (the last six months). If database available, update the data to current date
	        if (!(new File(DBname).exists())) {
	        	dBIf.createTable(conn);
	        	addDate(-6);
	    	    stockProp.put("fromMonth", fromMonth);
	    	    stockProp.put("fromDay", fromDay);
	    	    stockProp.put("fromYear", fromYear);
	    	    stockProp.put("toMonth", toMonth);
	    	    stockProp.put("toDay", toDay);
	    	    stockProp.put("toYear", toYear);
	    	    stockProp.put("freq", "d");
	        } else {
	        	
	        }
	    	String line = null;
	    	while ((line = buffReader.readLine()) != null) {
	    		//set ticker
	    		stockProp.put("ticker", line);
	    		//create new Yahoo connection for every stock
	    		YahooIf yahooIf = new YahooIf(stockProp);
	    		//connect Yahoo and read data
	    		BufferedReader bufferedReader = yahooIf.openYahooConnection();
		        //write database
		        dBIf.writeDB(conn, bufferedReader, stockProp.get("ticker"));
		        //close Yahoo read stream
		        yahooIf.closeYahooConnection(bufferedReader);
	    		
	    	}
	    	buffReader.close();
	    } catch(IOException e) {
	    	e.printStackTrace();
	    }		
	}
	
	/*
	 * calculate n-long moving average 
	 */
	
}
