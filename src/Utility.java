/*
 * utility class, includes some misc. utility methods
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

public class Utility {
	
	/*
	 * update database with new data
	 */
	/*public void updateDB(HashMap<String, String> stockProp, DBIf dBIf, Connection conn) {
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
		        dBIf.writeDB(conn, bufferedReader, stockProp.get("ticker"));
		        //close Yahoo read
		        yahooIf.closeYahooConnection(bufferedReader);
	    		
	    	}
	    	buffReader.close();
	    } catch(IOException e) {
	    	e.printStackTrace();
	    }		
	}*/
	
	/*
	 * calculate n-long moving average 
	 */
	
}
