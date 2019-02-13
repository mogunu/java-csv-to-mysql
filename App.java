//Initialise commands
//javac App.java
//java -cp .;jdbc.jar App

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;
import java.io.*;
import java.util.regex.*;
import java.util.*;
import au.com.bytecode.opencsv.*;

public class App {

	public static final String csvFile = "C:\\data.csv";
	
    public static void main(String[] args) {

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://127.0.0.1:3306/datafeed";
        String user = "root";
        String password = "password";
		int count = 0;
		int totalcount = 0;
		String values = "(0,0,0,0,0,0,0,0,0,0,0,0)";
		String query = "";
        try {
		
			CSVReader reader = new CSVReader(new FileReader(csvFile));
			String[] nextLine;
			String[] firstLine = reader.readNext();
			
			con = DriverManager.getConnection(url, user, password);            
			st = con.createStatement();
			
			while ((nextLine = reader.readNext()) != null) {
				
				values += ",('" + nextLine[0]+"','"+nextLine[1]+"','"+nextLine[2]+"','"+nextLine[3]"')";
				count++;
				totalcount++;
				
				if (count == 100) {
					query  = "INSERT INTO  'datafeed'.'raw_data' ("
						+"'linenumber' ,"
						+"'operation' ,"
						+"'applicaion' ,"
						+"'reportdate') 
						/*+"`END_DT` ,"
						+"`END_X` ,"
						+"`END_Y` ,"
						+"`DISTANCE` ,"
						+"`AMOUNT_PAID` ,"
						+"`JOB_NO` ,"
						+"`VEHICLE_ID` ,"
						+"`DRIVER_ID`)"*/
						+"VALUES " + values + ";";
					
					st.executeUpdate(query);
					count = 0;
					values = "(0,0,0,0)";
				}
			}
			query  = "DELETE FROM `rawdata` WHERE `TRIP_NO` = '0'";
			st.executeUpdate(query);
			
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(App.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
		
        } catch(Exception e){
			e.printStackTrace();
		} finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(App.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
		System.out.println(totalcount);
    }
}
