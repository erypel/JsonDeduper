package com.evan.dedup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A Utility Class with some functionality I wanted to separate from Dedup.java 
 * in order to test as well as compartmentalize functionality
 * @author Evan
 *
 */
public class Utils
{
	/**
	 * Method to read the supplied file
	 * 
	 * @param filename: the filename supplied
	 * @return return a String representation of the file's text
	 */
	public static String readFile(String filename)
	{
		// Read in the file
		BufferedReader br = null;
		String json = "";
		try
		{
			br = new BufferedReader(new FileReader(filename));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while(line != null)
			{
				sb.append(line);
				line = br.readLine();
			}
			json = sb.toString();
		}
		catch(Exception e)
		{
			System.out.println("Error reading in file: " + e.getMessage());
			return null;
		}
		finally
		{
			try
			{
				br.close();
			}
			catch(Exception e)
			{
				System.out.println("Error closing Buffered Reader: " + 
						e.getMessage());
				return null;
			}
		}
		return json;
	}
	
	/**
	 * Helper method to get the file name from args passed to the program
	 * 
	 * @param args: the arguments passed to the program
	 * @return the first string in the array. We'll assume that it is a filename
	 */
	public static String getFilenameFromArgs(String args[])
	{
		// check null
		if(args == null)
		{
			System.out.println("No filename provided.");
			return null;
		}
			
		// Check that there is an argument provided. Assume it's a filename
		String filename = "";
		if(0 < args.length)
			filename = args[0];
		else
		{
			System.out.println("No filename provided.");
			return null;
		}
		return filename;
	}
	
	/**
	 * Helper method to check if an ISO_OFFSET_DATE_TIME a is before 
	 * ISO_OFFSET_DATE_TIME b
	 * 
	 * @param a: string representing an ISO_OFFSET_DATE_TIME
	 * @param b: string representing an ISO_OFFSET_DATE_TIME
	 * @return true if a is before b, else false
	 */
	public static boolean isABeforeB(String a, String b)
	{
		ZonedDateTime aDT = ZonedDateTime.parse(a, DateTimeFormatter
				.ISO_OFFSET_DATE_TIME);
		ZonedDateTime bDT = ZonedDateTime.parse(b, DateTimeFormatter
				.ISO_OFFSET_DATE_TIME);
		return aDT.isBefore(bDT);
	}
}
