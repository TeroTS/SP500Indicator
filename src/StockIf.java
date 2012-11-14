/*
 * With this interface a stock item can be implemented.
 */
public interface StockIf {

	String getDate();
	String getTicker();
	double getValue();
	void setDate(String date);
	void setTicker(String ticker);
	void setValue(double value);
	
}
