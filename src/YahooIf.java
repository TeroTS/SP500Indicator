/*
 * open connection to Yahoo finance and download the stock data
 */

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
 

public class YahooIf {
	
	//Yahoo finance data download URL
	private static final String YAHOO_URL = "http://ichart.finance.yahoo.com/table.csv?";
	//date format
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	//stock download date properties (from date and to date)
	private HashMap<String, String> stockProp;
	
	//constructor
	public YahooIf() {
		stockProp = new HashMap<String, String>();
	}
	
	/*
	 * set download dates 
	 */
	public void setDateProp(String prevDate, String currentDate) {
		//download start day is always previous day + 1
		Calendar cal = new GregorianCalendar();
		try {
			Date date = new SimpleDateFormat(DATE_FORMAT).parse(prevDate);
			cal.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//add one day
		cal.add(Calendar.DAY_OF_WEEK, 1);
        String Day = Integer.toString(cal.get(Calendar.DATE));        
        String Month = Integer.toString(cal.get(Calendar.MONTH)+1); //first month 0         
        String Year = Integer.toString(cal.get(Calendar.YEAR)); 
    	String pDate = Year + "-" + Month + "-" + Day;

		//set download dates
		stockProp.put("fromMonth", pDate.split("-")[1]);
	    stockProp.put("fromDay", pDate.split("-")[2]);
	    stockProp.put("fromYear", pDate.split("-")[0]);
	    stockProp.put("toMonth", currentDate.split("-")[1]);
	    stockProp.put("toDay", currentDate.split("-")[2]);
	    stockProp.put("toYear", currentDate.split("-")[0]);
	    stockProp.put("freq", "d");		
	}
	
	/*
	 * open connection to Yahoo, returns arraylist
	 */
	public ArrayList<StockItem> openConnection(String ticker) {
		  BufferedReader reader = null;
		  ArrayList<StockItem> list = new ArrayList<StockItem>();
		  //StockItem stock = new StockItem();
		  
	      try {
	    	  URL yahoo = new URL(YAHOO_URL + 
	    			  			  "s=" + ticker + 
	    			  			  "&d=" + stockProp.get("toMonth") + 
	    			  	          "&e=" + stockProp.get("toDay") + 
	    			  		      "&f=" + stockProp.get("toYear") + 	    			  			  
	    			  			  "&g=" + stockProp.get("freq") +
	    			  			  "&a=" + stockProp.get("fromMonth") + 
	    			  			  "&b=" + stockProp.get("fromDay") + 
	    			  			  "&c=" + stockProp.get("fromYear")); 
	    	  
	          URLConnection yahooConn = yahoo.openConnection();             
	          reader = new BufferedReader(new InputStreamReader(yahooConn.getInputStream()));
	          //write data into arraylist
		      String line = null;
	          int i = 0;
	          while ((line = reader.readLine()) != null) {
	        	  i++;
	        	  //don't read the header line
	        	  if (i != 1) {
	        		  StockItem stock = new StockItem();
	        		  //split data fields
	        		  String[] dataFields = line.split(",");
	        		  //set the stock attributes
	        		  stock.setDate(dataFields[0]);
	        		  stock.setTicker(ticker);
	        		  stock.setOpen(Double.parseDouble(dataFields[1]));
	        		  stock.setHigh(Double.parseDouble(dataFields[2]));
	        		  stock.setLow(Double.parseDouble(dataFields[3]));
	        		  stock.setClose(Double.parseDouble(dataFields[4]));
	        		  stock.setVolume(Integer.parseInt(dataFields[5]));
	        		  stock.setAdjClose(Double.parseDouble(dataFields[6]));
	        		  //add stock to arraylist
	        		  list.add(0, stock);
	        	  }
	          }
	      } catch(Exception e) {
	    	  e.printStackTrace();
	      } finally {
	    	  try {
	    		  if (reader != null)
	    			  reader.close();
	    	  } catch(IOException e) {
	    		  e.printStackTrace();
	    	  }	    	  
	      }
	      return list;
	}
	
}
