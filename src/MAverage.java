/*
 /* n-length moving average
 */

import java.util.ArrayDeque;

public class MAverage {
	
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
	 * calculate total sum and count
	 */
	public void calcSum(double inp) {
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
