/*
 /* database table
 */
import java.util.HashMap;

public class Table {
	
	//name of the table
	private String name;
	//hashmap: name & type
	HashMap<String, String> tableRow;
	//header of the row
	//private String header = "date text, ticker string, open real, high real, low real, close real, volume integer, adjclose real";
	//numeric values of the row
	//date 
	private String date;
	//name of the stock symbol
	private String ticker;
	//open of the day
	private double open;
	//high of the day
	private double high;
	//low of the day
	private double low;
	//close of the day
	private double close;
	//volume of the day
	private int volume;
	//adjusted close of the day (dividend adjusted)
	private double adjClose;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the tableRow
	 */
	//public HashMap getTableRow() {
	//	return tableRow;
	//}

	/**
	 * @param tableRow the tableRow to set
	 */
	//public void setTableRow(HashMap tableRow) {
	//	this.tableRow = tableRow;
	//}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the ticker
	 */
	public String getTicker() {
		return ticker;
	}

	/**
	 * @param ticker the ticker to set
	 */
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	/**
	 * @return the open
	 */
	public double getOpen() {
		return open;
	}

	/**
	 * @param open the open to set
	 */
	public void setOpen(double open) {
		this.open = open;
	}

	/**
	 * @return the high
	 */
	public double getHigh() {
		return high;
	}

	/**
	 * @param high the high to set
	 */
	public void setHigh(double high) {
		this.high = high;
	}

	/**
	 * @return the low
	 */
	public double getLow() {
		return low;
	}

	/**
	 * @param low the low to set
	 */
	public void setLow(double low) {
		this.low = low;
	}

	/**
	 * @return the close
	 */
	public double getClose() {
		return close;
	}

	/**
	 * @param close the close to set
	 */
	public void setClose(double close) {
		this.close = close;
	}

	/**
	 * @return the volume
	 */
	public int getVolume() {
		return volume;
	}

	/**
	 * @param volume the volume to set
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}

	/**
	 * @return the adjClose
	 */
	public double getAdjClose() {
		return adjClose;
	}

	/**
	 * @param adjClose the adjClose to set
	 */
	public void setAdjClose(double adjClose) {
		this.adjClose = adjClose;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Table [name=" + name + ", date=" + date + ", ticker=" + ticker
				+ ", open=" + open + ", high=" + high + ", low=" + low
				+ ", close=" + close + ", volume=" + volume + ", adjClose="
				+ adjClose + "]";
	}
	
}
