/*
 * maItem class, includes % number of the stocks under ma
 * and the corresponding date
 */

public class MaItem {
	
	//the % number of stocks under ma
	private double underMa;
	//date
	private String date;

	/**
	 * get underMa
	 */
	public double getUnderMa() {
		return underMa;
	}
	
	/**
	 * set underMa
	 */
	public void setUnderMa(double underMa) {
		this.underMa = underMa;
	}
	
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
	
}
