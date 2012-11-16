/*
 * utility class, includes some misc. utility methods
 */

import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class Utility {
	
    //current date
    private String currentDate;
    //previous date when update happened
    private String prevDate;
    //is update needed or not
   // private boolean doUpdate;
    
    /*
     * get the previous update date (read from config file)
     */
    public String getPrevDate() {
    	Properties prop = new Properties();

    	try {
    		prop.load(new FileInputStream("config.txt"));
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
    		
    		prop.store(new FileOutputStream("config.txt"), null);
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }   	
    }

    /*
     * set the current date (=current date - 1)
     */
    public void setCurrentDate() {
    	String Day;
    	String Month;
    	String Year;
    	Calendar now = Calendar.getInstance();
        //get current date - 1 day
    	now.add(Calendar.DAY_OF_WEEK, -1); // fix this (-1) !!!!!!!!!!!!!
    	//setDay(Integer.toString(now.get(Calendar.DATE)));
        Day = Integer.toString(now.get(Calendar.DATE));        
        Month = Integer.toString(now.get(Calendar.MONTH));         
        Year = Integer.toString(now.get(Calendar.YEAR)); 
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
