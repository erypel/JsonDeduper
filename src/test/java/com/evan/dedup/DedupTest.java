package com.evan.dedup;

import org.junit.Test;

/**
 * Unit tests to make sure main() runs
 */
public class DedupTest 
{
	final String providedFileLocation = "./src/test/java/testfiles/leads.json";
	final String emptyFileLocation = "./src/test/java/testfiles/emptyFile.json";
	final String textFileLocation = "./src/test/java/testfiles/fileWithText.txt";

	/**
	 * test that everything runs okay
	 */
	@Test 
	public void testDedupWithProvidedJson()
	{
		Dedup.main(new String[] {providedFileLocation});
	}
	
	/**
	 * test that everything runs okay when a file isn't provided
	 */
	@Test 
	public void testDedupWithNoFile()
	{
		Dedup.main(new String[] {});
	}
	
	/**
	 * test that everything runs okay with an empty file
	 */
	@Test 
	public void testDedupWithEmptyFile()
	{
		Dedup.main(new String[] {emptyFileLocation});
	}
	
	/**
	 * test that everything runs okay with an non JSON file
	 */
	@Test 
	public void testDedupWithNonJsonFile()
	{
		Dedup.main(new String[] {textFileLocation});
	}
	
	/**
	 * test that everything runs okay when a file isn't provided
	 */
	@Test 
	public void testDedupWithNullArgs()
	{
		Dedup.main(null);
	}
}
