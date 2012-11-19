/*
 * date utility class. Methods to set the previous and current
 * update dates.
 */

import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class DateUtilities {
	
	private static final String CONFIG_FILE = "config.txt";
	
    //current date
    private String currentDate;
    //previous date when update happened
    private String prevDate;
    
    /*
     * get the previous update date (read from config file)
     */
    public String getPrevDate() {
    	Properties prop = new Properties();

    	try {
    		prop.load(new FileInputStream(CONFIG_FILE));
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
       
    	 //get the date of the previous update
         prevDate = prop.getProperty("PREV_DATE");
         return prevDate;
    }
    
    /*
     * set the previous update date (write to config file) 
     */
    public void setPrevDate(String PrevDate) {
    	Properties prop = new Properties();
    	 
    	try {
    		prop.setProperty("PREV_DATE", PrevDate);
    		
    		prop.store(new FileOutputStream(CONFIG_FILE), null);
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
    }

    /*
     * set the current date (=current date - 1)
     */
    public void setCurrentDate() {
    	Calendar now = Calendar.getInstance();
        //get current date - 1 day
    	now.add(Calendar.DAY_OF_WEEK, -1);
        String Day = Integer.toString(now.get(Calendar.DATE));        
        String Month = Integer.toString(now.get(Calendar.MONTH));         
        String Year = Integer.toString(now.get(Calendar.YEAR)); 
    	//format YYYY-MM-DD
    	currentDate = Year + "-" + Month + "-" + Day;
    }
    
    /*
     * get the current date
     */
    public String getCurrentDate() {
    	return currentDate;
    }
    
}
