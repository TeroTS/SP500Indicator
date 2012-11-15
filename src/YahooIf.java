/*
 * open connection to Yahoo finance and download the stock data
 */

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
//import java.util.List;
import java.util.ArrayList;
//import java.util.Properties;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
 

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
	 * set download dates 
	 */
	public void setDateProp(String prevDate, String currentDate) {
		//download start day is always previous day + 1
		Calendar cal = new GregorianCalendar();
		try {
			//String pDate = prevDate;
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(prevDate);
			//System.out.println(date);
			cal.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//add one day
		cal.add(Calendar.DAY_OF_WEEK, 1);
        String Day = Integer.toString(cal.get(Calendar.DATE));        
        String Month = Integer.toString(cal.get(Calendar.MONTH)+1); //first month 0         
        String Year = Integer.toString(cal.get(Calendar.YEAR)); 
    	//format YYYY-MM-DD
    	String pDate = Year + "-" + Month + "-" + Day;
    	
    	//System.out.println(pDate);
		//int fromDay = Integer.parseInt(prevDate.split("-")[2]) + 1;
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
	        	  //don't read the first line
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
	        		  //stock.setValue(Double.parseDouble(dataFields[6]));
	        		  //add stock to arraylist
	        		  list.add(0, stock);
	        		  //System.out.println(stock.getTicker() + " " + stock.getDate());
	        	  }
	          }
	          //for (int j=0; j < 20; j++) {
	        	//  System.out.println(list.get(j).getDate());
	          //}
	          for (StockItem index : list) {
	        	  System.out.println("from yahoo:" + index.getDate() + " " + index.getAdjClose()); 
	          }
	      } catch(Exception e) {
	    	  e.printStackTrace();
	      }
	      return list;
	}
	
	/*
	 * close data reader
	 */
	public void closeConnection(BufferedReader reader) {
		try {
			if (reader != null)
				reader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
