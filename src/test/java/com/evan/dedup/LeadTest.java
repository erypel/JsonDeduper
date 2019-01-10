package com.evan.dedup;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LeadTest
{
	final String check = "{\n\t" + 
			"\"_id\": \"jkj238238jdsnfsj23\",\n\t" + 
			"\"email\": \"bill@bar.com\",\n\t" + 
			"\"firstName\":  \"John\",\n\t" + 
			"\"lastName\": \"Smith\",\n\t" + 
			"\"address\": \"888 Mayberry St\",\n\t" + 
			"\"entryDate\": \"2014-05-07T17:33:20+00:00\"\n" + 
			"}";
	
	@Test
	public void testToStringFormatsCorrectly()
	{
		System.out.println("Begin testing Lead.toString()");
		Lead lead = new Lead("jkj238238jdsnfsj23", "bill@bar.com", "John", "Smith",
				"888 Mayberry St", "2014-05-07T17:33:20+00:00");
		String leadStr = lead.toString();
		
		System.out.println("Created lead object:");
		System.out.println(leadStr);
		
		System.out.println("Checking lead against:");
		System.out.println(check);
		
		assertTrue(leadStr.equals(check));
	}
}
