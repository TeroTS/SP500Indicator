import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

public class SP500Indicator {
	
	static final String TICKER_FILE = "SP500.txt";
	static final String DB_FILE = "sp500.db";
	static final String DB_NAME = "jdbc:sqlite:sp500.db"; 
	static final String TABLE_NAME = "stock";
	//moving average length
	static final int MA_LENGTH = 10;
	static final int NUMBER_OF_STOCKS = 500;

	//database read/write if
	private DBHandler dBHandler;
	//misc. utilities
	private DateUtilities dateUtilities;
	//Yahoo data download
	private YahooIf yahooIf;
    //% of the stocks under ma
    ArrayList<MaItem> maList;
	
	public SP500Indicator() {
		dBHandler = new DBHandler();
		dateUtilities = new DateUtilities();
		yahooIf = new YahooIf();
		//new draw panel
		//DrawGraph drawPanel = new DrawGraph(MaList);
	}
	
	public void go() {
		//create a database connection
	    Connection connection = null;
	    //buffered file reader
	    BufferedReader buffReader = null;
	    //old stock samples
	    ArrayList<StockItem> oldStockList = null;
	    //new stock samples from yahoo
	    ArrayList<StockItem> newStockList = null;
	    //new stock samples with updated ma & underMa values
	    ArrayList<StockItem> newSamples = null;
	    //moving average
	    MAverage mAverage = null;
	    //database read command
	    String command = "";
	    //
	    ArrayList<StockItem> stockList = null;
	    
	    try {
	    	//read stock tickers from file,
	    	//one ticker per line
	    	File file = new File(TICKER_FILE);
	    	FileReader fileReader = new FileReader(file);
	    	buffReader = new BufferedReader(fileReader);
	    	//if database doesn't exist, create table
	    	//if (!(new File(DB_FILE).exists())) {
	    		// create a database connection
	    	    connection = dBHandler.openDBConnection(DB_NAME);
	    	    //create stock table
	        	dBHandler.createTable(connection, TABLE_NAME);
	    	//} else {
	    	//	connection = dBHandler.openDBConnection(DB_NAME);
	    	//}
	    	//set the current date
	    	dateUtilities.setCurrentDate();
	    	//set the download dates (previous date & current date)
	    	String prevDate = dateUtilities.getPrevDate();
	    	String currDate = dateUtilities.getCurrentDate();
	    	System.out.println("prev: " + prevDate + " curr: " + currDate);
	    	yahooIf.setDateProp(prevDate, currDate);
	    	//if stock data not updated today, do the update
	    	if (!currDate.equals(prevDate)) {
	    		String ticker = null;
	    		while ((ticker = buffReader.readLine()) != null) {	
	    			//every stock has its own average calculator object
	    			mAverage = new MAverage(MA_LENGTH);
	    			//fill the moving average (ma) window with old samples from database,
	    			//this data is used to calculate the new ma values
	    			//read the last n samples from the database
	    			command = "select * from " + TABLE_NAME + " where ticker = '" + ticker + "' order by date desc limit " + MA_LENGTH;
	    			oldStockList = dBHandler.readDB(connection, command);
	    			//fill the ma window with old samples
	    			MaUtilities.loadData(oldStockList, mAverage);	    	    
	    			//connect Yahoo and download the new samples
	    			newStockList = yahooIf.openConnection(ticker);
	    			//calculate ma & underMa values for new samples
	    			newSamples= MaUtilities.calcNewSamples(newStockList, mAverage);	
	    			//write new samples to database
	    			dBHandler.writeDB(connection, newSamples, TABLE_NAME);
	    		}
	    	}
			//load samples from database, list contains (number_of_stocks * number_of_dates) objects
			command = "select * from " + TABLE_NAME + " order by date desc limit 30000";
			stockList = dBHandler.readDB(connection, command);
			//calculate the % of the stocks under ma
			maList = MaUtilities.calcUnderMa(stockList, NUMBER_OF_STOCKS);
			//draw the graph
			DrawGraph drawPanel = new DrawGraph(maList);
			drawPanel.init(drawPanel);
	    	buffReader.close();
	    } catch(IOException e) {
	    	e.printStackTrace();
	    //} catch(SQLException e) {
	    //	e.printStackTrace();
	    } finally {
            //close database connection
	    	dBHandler.closeDBConnection(connection);
	    	//update the previous download date to current date
	    	dateUtilities.setPrevDate(dateUtilities.getCurrentDate());
	    }
	    
    }

	public static void main(String[] args) throws ClassNotFoundException {
		
	    // load the sqlite-JDBC driver
	    Class.forName("org.sqlite.JDBC");
	    
	    SP500Indicator sp500Indicator = new SP500Indicator();
	    sp500Indicator.go();
	    		
	}

}
