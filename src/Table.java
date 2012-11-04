/*
 * database table
 */
import java.util.HashMap;

public class Table {
	
	//name of the table
	private String name;
	//hashmap: name & type
	HashMap tableRow;
	//header of the row
	//private String header = "date text, ticker string, open real, high real, low real, close real, volume integer, adjclose real";
	//numeric values of the row
	//date 
	private  String date;
	//name of the stock symbol
	private String ticker;
	//open of the day
	private double open;
	

}
