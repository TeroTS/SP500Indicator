/*
 /* stock class
 */

public class StockItem {
	
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
	//moving average value
	private double ma;
	//close under ma
	private int underMa;

	/**
	 * get date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * set date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * get ticker
	 */
	public String getTicker() {
		return ticker;
	}

	/**
	 * set ticker
	 */
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	/**
	 * get open
	 */
	public double getOpen() {
		return open;
	}

	/**
	 * set open
	 */
	public void setOpen(double open) {
		this.open = open;
	}

	/**
	 * get high
	 */
	public double getHigh() {
		return high;
	}

	/**
	 * set high
	 */
	public void setHigh(double high) {
		this.high = high;
	}

	/**
	 * get low
	 */
	public double getLow() {
		return low;
	}

	/**
	 * set low
	 */
	public void setLow(double low) {
		this.low = low;
	}

	/**
	 * get close
	 */
	public double getClose() {
		return close;
	}

	/**
	 * set close
	 */
	public void setClose(double close) {
		this.close = close;
	}

	/**
	 * get volume
	 */
	public int getVolume() {
		return volume;
	}

	/**
	 * set volume
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}

	/**
	 * get adjClose
	 */
	public double getAdjClose() {
		return adjClose;
	}

	/**
	 * set adjClose
	 */
	public void setAdjClose(double adjClose) {
		this.adjClose = adjClose;
	}

	/**
	 * get ma
	 */
	public double getMa() {
		return ma;
	}

	/**
	 * set ma
	 */
	public void setMa(double ma) {
		this.ma = ma;
	}

	/**
	 * get underMa
	 */
	public int getUnderMa() {
		return underMa;
	}

	/**
	 * set underMa
	 */
	public void setUnderMa(int underMa) {
		this.underMa = underMa;
	}

	/* 
	 * toString
	 */
	@Override
	public String toString() {
		return "Table [date=" + date + ", ticker=" + ticker
				+ ", open=" + open + ", high=" + high + ", low=" + low
				+ ", close=" + close + ", volume=" + volume + ", adjClose="
				+ adjClose + "]";
	}
	
}
