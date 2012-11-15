import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

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
	//private DBHandlerMa dBHandlerMa;
	//misc. utilities
	private Utility utility;
	//Yahoo data download
	private YahooIf yahooIf;
	//calculate moving average (ma)
	private MAverage mAverage;
	//stock 
	//Stock stock;
	//private ArrayList<StockItem> oldStockList;
	
	public SP500Indicator() {
		dBHandler = new DBHandler();
		//dBHandlerMa = new DBHandlerMa();
		utility = new Utility();
		yahooIf = new YahooIf();
		mAverage = new MAverage(3);
		//stock = new Stock();
	}
	
	public void go() {
		// create a database connection
	    Connection connection = dBHandler.openDBConnection(NAME_DB);
	    //buffered file reader
	    BufferedReader buffReader = null;
	    //List<Stock> list = null;
	    //old stock samples
	    ArrayList<StockItem> oldStockList; //= new ArrayList<StockItem>();
	
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
	        	//dBHandler.createTable(connection, TABLE_NAME_0);
	        	//create ma table
	        	//dBHandlerMa.createTable(connection, TABLE_NAME_1);
	    	//}	    	
	    	//set the current date
	    	utility.setCurrentDate();
	    	//set the download dates (previous date & current date)
	    	String prevDate = utility.getPrevDate();
	    	String currDate = utility.getCurrentDate();
	    	System.out.println("prev: " + prevDate + " curr: " + currDate);
	    	yahooIf.setDateProp(prevDate, currDate);	    	
	    	if (!currDate.equals(prevDate)) {
	    		String ticker = null;
	    		while ((ticker = buffReader.readLine()) != null) {	
	    			//fill the moving average (ma) window with old data from database,
	    			//this data is used to calculate the new ma values
	    			//read the last n samples from the database
	    			oldStockList = dBHandler.readDB(connection, TABLE_NAME_0, ticker, 3);
	    			//fill the ma window with old data
	    			loadData(oldStockList);	    	    
	    			//System.out.println(mAverage.getCount() + " " + mAverage.getLength());
	    			//connect Yahoo and download the new data (n lines of data)
	    			ArrayList<StockItem> newStockList = yahooIf.openConnection(ticker);
	    			//System.out.println(newStockList.size());
	    			//calculate ma & underMa values for new samples
	    			ArrayList<StockItem> newSamples= calcNewSamples(newStockList);    	
	    			//write ma items to database
	    			//dBHandlerMa.writeDB(connection, maList, TABLE_NAME_1);    	
	    			//write new samples to database
	    			dBHandler.writeDB(connection, newSamples, TABLE_NAME_0);
	    		}
	    	}
	    	buffReader.close();
	    } catch(IOException e) {
	    	e.printStackTrace();
	    //} catch(SQLException e) {
	    //	e.printStackTrace();
	    } finally {
	    	//close file read stream
	    	yahooIf.closeConnection(buffReader);
	    	dBHandler.closeDBConnection(connection);
	    	utility.setPrevDate(utility.getCurrentDate());
	    	//set the previous download date to current date
	    	//utility.setPrevDate(utility.getCurrentDate());	
	    }
	    
    }
	
	/*
	 * fill the ma window with old data from database,
	 * used to calculate the new ma values
	 */
	private void loadData(ArrayList<StockItem> list) {
		for (StockItem index : list) {
			//System.out.println("old:" + index.getDate() + " " + index.getAdjClose());
			mAverage.calcSum(index.getAdjClose());
			//break the loop when ma window full of data
			if (mAverage.getCount() == mAverage.getLength()) {
				break;
			}
		}		
	}

	/*
	 * calculate new values for ma and underMa
	 */
	private ArrayList<StockItem> calcNewSamples(ArrayList<StockItem> list) {
		for (StockItem index : list) {
			mAverage.calcSum(index.getAdjClose());
			//calculate the moving average when all data available
			if (mAverage.getCount() == mAverage.getLength()) {
				//set new ma value
				index.setMa(mAverage.getAverage());//maList.add(calcMa.getAverage());	    				
				//if ma > close set value to 1 else 0,
				//used to calculate the total number of stocks under ma
				if (index.getMa() > index.getAdjClose()) {
					index.setUnderMa(1);
				}	
			}
			System.out.println("ma: " + index.getUnderMa() + " " + index.getDate() + " " + index.getMa() + " " + index.getAdjClose());
		}		
		return list;
	}


	public static void main(String[] args) throws ClassNotFoundException {
		
	    // load the sqlite-JDBC driver
	    Class.forName("org.sqlite.JDBC");
	    
	    SP500Indicator sp500Indicator = new SP500Indicator();
	    sp500Indicator.go();
	    		
	}

}
