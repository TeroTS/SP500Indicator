/*
 /* stock class
 */

public class StockItem implements StockIf {
	
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
	//adjusted close of the day (dividend and split adjusted)
	private double adjClose;

	//implement the interface
	/*
	 * get value (=adjusted close)
	 */
	public double getValue() {
		return (this.getAdjClose());
	}
	
	/*
	 * set value to adjusted close
	 */
	public void setValue(double value) {
		this.setAdjClose(value);
	}
	
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
	//interface implementation ends

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
		return "Table [date=" + date + ", ticker=" + ticker
				+ ", open=" + open + ", high=" + high + ", low=" + low
				+ ", close=" + close + ", volume=" + volume + ", adjClose="
				+ adjClose + "]";
	}
	
}
