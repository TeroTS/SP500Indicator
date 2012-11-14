import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
//import java.util.HashMap;

public class SP500Indicator {
	
	static final String TICKER_FILE = "SP500.txt";
	static final String DB_NAME = "sp500.db";
	static final String NAME_DB = "jdbc:sqlite:sp500.db"; 
	static final String TABLE_NAME_0 = "stock";
	static final String TABLE_STRING_0 = "CREATE TABLE " + TABLE_NAME_0 + " (date TEXT not null, ticker STRING not null, " +
							 								                "value REAL, primary key (date, ticker))";
	static final String TABLE_NAME_1 = "ma";
	static final String TABLE_STRING_1 = "CREATE TABLE " + TABLE_NAME_1 + " (date TEXT not null, ticker STRING not null, " +
																		 "value REAL, primary key (date, ticker))";
	//database read/write if
	private DBHandler dBHandler;
	//misc. utilities
	private Utility utility;
	//Yahoo data download
	private YahooIf yahooIf;
	//calculate moving average (ma)
	private MAverage mAverage;
	//stock 
	//Stock stock;
	
	public SP500Indicator() {
		dBHandler = new DBHandler();
		utility = new Utility();
		yahooIf = new YahooIf();
		mAverage = new MAverage(10);
		//stock = new Stock();
	}
	
	public void go() {
		// create a database connection
	    Connection connection = dBHandler.openDBConnection(NAME_DB);
	    //buffered file reader
	    BufferedReader buffReader = null;
	    //List<Stock> list = null;
	    //old stock samples
	    ArrayList<StockIf> oldStockList; //= new ArrayList<StockItem>();
	    //new moving average samples 
	    ArrayList<StockIf> maList = new ArrayList<StockIf>();
	    //moving average dates
	    //ArrayList<String> dateList = new ArrayList<String>();
	
	    try {
	    	//read stock tickers from file,
	    	//one ticker per line
	    	File file = new File(TICKER_FILE);
	    	FileReader fileReader = new FileReader(file);
	    	buffReader = new BufferedReader(fileReader);
	    	//if database doesn't exist, create table
	    	//if (!(new File(DB_NAME).exists())) {
	    		// create a database connection
	    	    //create stock table
	        	dBHandler.createTable(connection, TABLE_STRING_0, TABLE_NAME_0);
	        	//create ma table
	        	dBHandler.createTable(connection, TABLE_STRING_1, TABLE_NAME_1);
	    	//}	    	
	    	//set the current date
	    	utility.setCurrentDate();
	    	//set the download dates (previous date & current date)
	    	yahooIf.setDateProp(utility.getPrevDate(), utility.getCurrentDate());    	
	    	String ticker = null;
	    	while ((ticker = buffReader.readLine()) != null) {	
	    		//fill the moving average (ma) window with old data from database,
	    		//this data is used to calculate the new ma values
	    		String command = "select * from stock where ticker = '" + ticker + "' order by date limit 10";
	    		oldStockList = dBHandler.readDB(connection, command, new StockItem());
	    		//while (resultSet.next()) {
	    		for (StockIf index : oldStockList) {
	    			mAverage.calculateMa(index.getValue());
	    			//break the loop when ma window full of data
	    			if (mAverage.getCount() == mAverage.getLength()) {
	    				break;
	    			}
	    		}
	    		System.out.println(mAverage.getCount() + " " + mAverage.getLength());
	    		//connect Yahoo and download the new data (n lines of data)
	    		ArrayList<StockIf> newStockList = yahooIf.openConnection(ticker);
	    		System.out.println(newStockList.size());
	    		//calculate new ma values
	    		maList.clear();
	    		for (StockIf index : newStockList) {
	    			mAverage.calculateMa(index.getValue());
	    			//calculate the moving average when all data available
	    			if (mAverage.getCount() == mAverage.getLength()) {
	    				//System.out.println(mAverage.getCount() + " " + mAverage.getLength());
	    				System.out.println(index.getDate() + " " + ticker);
	    				double ma = mAverage.getAverage();//maList.add(calcMa.getAverage());	    				
	    				//generate a new ma item and store it to arraylist
	    				MaItem maItem = new MaItem();
	    				maItem.setDate(index.getDate());
	    				maItem.setTicker(ticker);	
	    				maItem.setValue(0.0);
	    				//if ma > close set value to 1.0 else 0.0,
	    				//used to calculate the total number of stocks above ma
		    			if (ma > index.getValue()) {
		    				maItem.setValue(1.0);
		    			}
	    				maList.add(maItem);	
	    			}
	    		}
	    		//write ma items to database
	    		dBHandler.writeDB(connection, maList, TABLE_NAME_1);    	
		        //write new stock items to database
		        dBHandler.writeDB(connection, newStockList, TABLE_NAME_0);
		        
		        //test
		        String command2 = "select * from ma where ticker = '" + ticker + "' order by date";
		        ArrayList<StockIf> test = dBHandler.readDB(connection, command2, new MaItem());
		        System.out.println(test.size());
		        
		        //close Yahoo read stream
		        //yahooIf.closeConnection(bufferedReader);	
		        //read database (n lines of data)
		        //dBIf.readDB(connection, ticker);
		        //utility.setPrevDate(utility.getCurrentDate());
	    	}
	    	buffReader.close();
	    	utility.setPrevDate(utility.getCurrentDate());
	    } catch(IOException e) {
	    	e.printStackTrace();
	   // } catch(SQLException e) {
	   // 	e.printStackTrace();
	    } finally {
	    	//close file read stream
	    	//yahooIf.closeConnection(buffReader);
	    	//set the previous download date to current date
	    	//utility.setPrevDate(utility.getCurrentDate());	
	    }
	    
    }

	public static void main(String[] args) throws ClassNotFoundException {
		
	    // load the sqlite-JDBC driver
	    Class.forName("org.sqlite.JDBC");
	    
	    SP500Indicator sp500Indicator = new SP500Indicator();
	    sp500Indicator.go();
	    		
	}

}
