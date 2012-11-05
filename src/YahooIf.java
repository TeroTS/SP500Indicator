/*
 * open connection to Yahoo finance and download the stock data
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class YahooIf {
	
	/*
	 * open connection to Yahoo, returns read buffer
	 */
	public BufferedReader openYahooConnection() {
		  BufferedReader reader = null;
	      try {
	    	  URL yahoo = new URL("http://ichart.finance.yahoo.com/table.csv?s=YHOO&a=00&b=1&c=2011&d=00&e=1&f=2012&g=d&ignore=.csv"); 
	          URLConnection yahooConn = yahoo.openConnection();             
	          reader = new BufferedReader(new InputStreamReader(yahooConn.getInputStream())); 
	      } catch(Exception ex) {
	    	  ex.printStackTrace();
	      }
	      return reader;
	}

}
