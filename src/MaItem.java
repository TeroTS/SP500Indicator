/*
 * moving average item. Implements stock interface
 */

public class MaItem implements StockIf{
	
	//date 
	private String date;
	//name of the stock symbol
	private String ticker;
	//is today's closing price under moving average
	private double underMa;
	
	//implement the interface
	/*
	 * get value (=adjusted close)
	 */
	public double getValue() {
		return underMa;
	}
	
	/*
	 * set value to adjusted close
	 */
	public void setValue(double value) {
		this.underMa = value;
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

}
