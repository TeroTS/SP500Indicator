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
	 * @return the underMa
	 */
	public double getUnderMa() {
		return underMa;
	}
	
	/**
	 * @param underMa the underMa to set
	 */
	public void setUnderMa(double underMa) {
		this.underMa = underMa;
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
	
}
