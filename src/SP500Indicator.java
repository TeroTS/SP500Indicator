import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

public class SP500Indicator {
	
	static final String TICKER_FILE = "SP500.txt";
	static final String DB_NAME = "sp500.db";
	static final String NAME_DB = "jdbc:sqlite:sp500.db"; 
	
	//database read/write if
	private DBIf dBIf;
	//misc. utilities
	private Utility utility;
	//Yahoo data download
	private YahooIf yahooIf;
	
	public SP500Indicator() {
		dBIf = new DBIf();
		utility = new Utility();
		yahooIf = new YahooIf();
	}
	
	public void go() {
		// create a database connection
	    Connection connection = dBIf.openDBConnection(NAME_DB);
	
	    try {
	    	//read stock tickers from file,
	    	//one ticker per line
	    	File file = new File(TICKER_FILE);
	    	FileReader fileReader = new FileReader(file);
	    	BufferedReader buffReader = new BufferedReader(fileReader);
	    	//if database doesn't exist, create table
	    	if (!(new File(DB_NAME).exists())) {
	        	dBIf.createTable(connection);
	    	}	    	
	    	//set the current date
	    	utility.setCurrentDate();
	    	//set the download dates (previous date -> current date)
	    	yahooIf.setDateProp(utility.getPrevDate(), utility.getCurrentDate());
	    	//set the previous download date to current date
	    	utility.setPrevDate(utility.getCurrentDate());	    	
	    	String ticker = null;
	    	while ((ticker = buffReader.readLine()) != null) {	    	
	    		//connect Yahoo and read data (n lines of data)
	    		BufferedReader bufferedReader = yahooIf.openYahooConnection(ticker);
		        //write database (n lines of data)
		        dBIf.writeDB(connection, bufferedReader, ticker);
		        //close Yahoo read stream
		        yahooIf.closeYahooConnection(bufferedReader);	    		
	    	}
	    	buffReader.close();
	    } catch(IOException e) {
	    	e.printStackTrace();
	    }		
	    
    }

	public static void main(String[] args) throws ClassNotFoundException {
		
	    // load the sqlite-JDBC driver
	    Class.forName("org.sqlite.JDBC");
	    
	    SP500Indicator sp500Indicator = new SP500Indicator();
	    sp500Indicator.go();
	    		
	}

}
