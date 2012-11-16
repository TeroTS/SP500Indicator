import java.util.ArrayList;

/*
 * MaUtilities class, includes static methods to process ma values
 */
public class MaUtilities {

	/*
	 * fill the ma window with old data from database,
	 * used to calculate the new ma values
	 */
	public static void loadData(ArrayList<StockItem> list, MAverage mAverage) {
		for (StockItem index : list) {
			//System.out.println("old:" + index.getDate() + " " + index.getAdjClose());
			mAverage.calcSum(index.getAdjClose());
			//break the loop when ma window full of data
			if (mAverage.getCount() == mAverage.getLength()) {
				break;
			}
		}	
		//System.out.println("count: " + mAverage.getCount() + " length: " + mAverage.getLength());
	}

	/*
	 * calculate new values for ma and underMa
	 */
	public static ArrayList<StockItem> calcNewSamples(ArrayList<StockItem> list, MAverage mAverage) {
		for (StockItem index : list) {
			mAverage.calcSum(index.getAdjClose());
			//calculate the moving average when all data available
			if (mAverage.getCount() == mAverage.getLength()) {
				//set new ma value
				index.setMa(mAverage.getAverage());//maList.add(calcMa.getAverage());	    				
				//if ma > close set value to 1 else 0,
				//used to calculate the total number of stocks under ma
				if (index.getMa() > index.getAdjClose()) {
					index.setUnderMa(1);
				}	
			}
			//System.out.println(index.getTicker() + " " + index.getUnderMa() + " " + index.getDate() + " ma " + index.getMa() + " close " + index.getAdjClose());
		}		
		return list;
	}
	
	/*
	 * calculate the % number of stocks under ma.
	 * Parameter list includes (number_of_stocks * number_of_dates) objects.
	 * Returns a list with number_of_dates objects
	 */
	public static ArrayList<MaItem> calcUnderMa(ArrayList<StockItem> list, int numberOfStocks) {
		int sum = 0;
		int i = 0;
		MaItem maItem = null;
		ArrayList<MaItem> maList = new ArrayList<MaItem>();
		//calculate the number of stocks under ma
		for(StockItem index : list) {
			i++;
			sum += index.getUnderMa();
			//single date calculation done,
			//reset state and store data to list
			if (i == numberOfStocks) {
				maItem = new MaItem();
				maItem.setDate(index.getDate());
				maItem.setUnderMa(100*sum/numberOfStocks);
				maList.add(maItem);
				//System.out.println(index.getDate() + " " + 100*sum/numberOfStocks);
				i = 0;
				sum = 0;					
			}
		}
		return (maList);
	}
	
}
