/*
 * open connection to Yahoo finance and download the stock data
 */

import java.io.*;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
//import java.util.Properties;

public class YahooIf {
	
	//Yahoo finance data download URL
	static final String YAHOO_URL = "http://ichart.finance.yahoo.com/table.csv?";
	
	//stock download date properties (from date and to date)
	private HashMap<String, String> stockProp;
	
	//constructor
	public YahooIf() {
		stockProp = new HashMap<String, String>();
	}
	
	/*
	 * set the date properties
	 */
	public void setDateProp(String prevDate, String currentDate) {
		stockProp.put("fromMonth", prevDate.split("-")[1]);
	    stockProp.put("fromDay", prevDate.split("-")[2]);
	    stockProp.put("fromYear", prevDate.split("-")[0]);
	    stockProp.put("toMonth", currentDate.split("-")[1]);
	    stockProp.put("toDay", currentDate.split("-")[2]);
	    stockProp.put("toYear", currentDate.split("-")[0]);
	    stockProp.put("freq", "d");		
	}
	
	/*
	 * open connection to Yahoo, returns read buffer
	 */
	public BufferedReader openYahooConnection(String ticker) {
		  BufferedReader reader = null;
		  
	      try {
	    	  URL yahoo = new URL(YAHOO_URL + 
	    			  			  "s=" + stockProp.get(ticker) + 
	    			  			  "&d=" + stockProp.get("toMonth") + 
	    			  	          "&e=" + stockProp.get("toDay") + 
	    			  		      "&f=" + stockProp.get("toYear") + 	    			  			  
	    			  			  "&g=" + stockProp.get("freq") +
	    			  			  "&a=" + stockProp.get("fromMonth") + 
	    			  			  "&b=" + stockProp.get("fromDay") + 
	    			  			  "&c=" + stockProp.get("fromYear")); 
	    	  
	          URLConnection yahooConn = yahoo.openConnection();             
	          reader = new BufferedReader(new InputStreamReader(yahooConn.getInputStream())); 
	      } catch(Exception e) {
	    	  e.printStackTrace();
	      }
	      return reader;
	}
	
	/*
	 * close yahoo data reader
	 */
	public void closeYahooConnection(BufferedReader reader) {
		try {
			if (reader != null)
				reader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
