package com.evan.dedup;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test the Util class
 * @author Evan
 *
 */
public class UtilsTest
{
	// Test values for readFile()
	final String emptyFileLocation = "./src/test/java/testfiles/emptyFile.json";
	final String canNotFindFileLocation = "./I/Dont/Think/There/Should/Be/A/File/Here.json";
	final String fileWithTextLocation = "./src/test/java/testfiles/fileWithText.txt";
	final String fileWithTextText = "Here is some text!";
	
	// Test values for getFilenameFromArgs()
	final String filename = "leads.json";
	final String[] argsWithOneValue = new String[] { filename };
	final String[] argsWithMultipleValues = new String[] { filename, "37", "test" };
	final String[] emptyArgs = new String[] {};
	
	// Strings for testing isABeforeB()
	final String a_before = "2014-05-07T17:30:20+00:00";
	final String b_after = "2014-06-07T17:30:20+00:00";
	
	final String a_after = "2014-06-07T17:30:20+00:00";
	final String b_before = "2014-05-07T17:30:20+00:00";
	
	final String a_same = "2014-06-07T17:30:20+00:00";
	final String b_same = "2014-06-07T17:30:20+00:00";
	
	// UNIT TESTS FOR readFile()
	/**
	 * Test readFile() such that empty files are read and "" is returned
	 */
	@Test
	public void testReadFile_emptyFile()
	{
		String fileText = Utils.readFile(emptyFileLocation);
		assertTrue("".equals(fileText));
	}
	
	/**
	 * Test readFile() for file locations that don't exist. Should return null
	 */
	@Test
	public void testReadFile_fileDoesNotExist()
	{
		String fileText = Utils.readFile(canNotFindFileLocation);
		assertNull(fileText);
	}
	
	/**
	 * Test readFile() on a file containing text
	 */
	@Test
	public void testReadFile_text()
	{
		String fileText = Utils.readFile(fileWithTextLocation);
		assertTrue(fileWithTextText.equals(fileText));
	}
	
	// UNIT TESTS FOR getFilenameFromArgs()
	/**
	 * Test getFilenameFromArgs under ideal circumstances. Pass it and array with
	 * one string argument
	 */
	@Test
	public void testGetFilenameFromArgs_argsWithOneString()
	{
		String testFilename = Utils.getFilenameFromArgs(argsWithOneValue);
		assertTrue(testFilename.equals(filename));
	}
	
	/**
	 * Test getFilenameFromArgs when passed multiple args
	 */
	@Test
	public void testGetFilenameFromArgs_argsWithMultipleStrings()
	{
		String testFilename = Utils.getFilenameFromArgs(argsWithOneValue);
		assertTrue(testFilename.equals(filename));
	}
	
	/**
	 * Test getFilenameFromArgs when passed an empty array of args
	 */
	@Test
	public void testGetFilenameFromArgs_emptyArgs()
	{
		String testFilename = Utils.getFilenameFromArgs(emptyArgs);
		assertNull(testFilename);
	}
	
	/**
	 * Test getFilenameFromArgs when passed null
	 */
	@Test
	public void testGetFilenameFromArgs_null()
	{
		String testFilename = Utils.getFilenameFromArgs(null);
		assertNull(testFilename);
	}
	
	// UNIT TESTS FOR isABeforeB()
	/**
	 * Test isABeforeB() with an a that is before b isABeforeB() should return true
	 */
	@Test
	public void testIsABeforeB_aIsBeforeB()
	{
		assertTrue(Utils.isABeforeB(a_before, b_after));
	}
	
	/**
	 * Test isABeforeB() with an a that is after b isABeforeB() should return false
	 */
	@Test
	public void testIsABeforeB_aIsAfterB()
	{
		assertFalse(Utils.isABeforeB(a_after, b_before));
	}
	
	/**
	 * Test isABeforeB() with an a that is the same as b isABeforeB() should return
	 * false
	 */
	@Test
	public void testIsABeforeB_aSameAsB()
	{
		assertFalse(Utils.isABeforeB(a_same, b_same));
	}
}
