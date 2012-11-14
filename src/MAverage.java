/*
 /* n-length moving average
 */

//import java.util.List;
//import java.util.ArrayList;
import java.util.ArrayDeque;
//import java.sql.ResultSet;

public class MAverage {
	
	//n-length moving average
	//private double ma;
	//length
	private int length;
	//sum
	private double sum;
	//count
	private int count;
	//moving average implemented as FIFO
	private ArrayDeque<Double> maQueue = new ArrayDeque<Double>();
	
	public MAverage(int length) {
		this.length = length;
	}

	/*
	 * calculate moving average, list parameter
	 */	
	/*public void calculateMa(ArrayList<Stock> list) {	
		for (Stock index : list) {
			calculateSum(index.getAdjClose());
			//calculate the moving average when all data available
			if (count == length) {
				getAverage();
			}
		}	
	}
	
	/*
	 * calculate moving average, resultset parameter
	 */	
	/*public void calculateMa(ResultSet rs) {	
		while (rs.next()) {
			calculateSum(index.getAdjClose());
			//calculate the moving average when all data available
			if (count == length) {
				getAverage();
			}
		}	
	}*/
	
	/*
	 * calculate total sum and count
	 */
	public void calculateMa(double inp) {
		//calculate total sum
		sum += inp;
		//add input value into FIFO
		maQueue.addFirst(inp);
		//if FIFO size > length, remove the last element from the total sum
		if (maQueue.size() > length) {
			sum -= maQueue.removeLast();
		} else {
			count++;
		}
	}
	
	/*
	 * calculate the average
	 */
	public double getAverage() {
		return (sum/count);
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	
}
