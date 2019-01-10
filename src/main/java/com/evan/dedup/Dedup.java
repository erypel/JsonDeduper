package com.evan.dedup;

import java.util.Arrays;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

//to run:
//mvn clean compile
//mvn exec:java -D exec.mainClass=com.evan.dedup.Dedup -Dexec.args="leads.json"
public class Dedup
{
	public static void main(String[] args)
	{
		// Check that there is an argument provided. Assume it's a filename
		String filename = Utils.getFilenameFromArgs(args);
		if(filename == null || filename == "")
		{
			System.out.println("Please supply a file argument.");
			return;
		}
		
		// Read in the file
		String json = Utils.readFile(filename);
		
		/*
		 * Unmarshall leads from the JSON and then map to Lead objects to fill an 
		 * array.
		 * 
		 * Using an array because accessing an array is O(1) and I will be using it
		 * for accessing Leads.
		 */
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = createJsonNode(mapper, json);
		if(jsonNode == null)
			return;
		
		Lead[] leads = createLeadsArrayFromJsonNode(mapper, jsonNode);
		if(leads == null)
			return;
		
		// run the depulication algorithm
		leads = dedup(leads);
		
		// Add the deduplicated array back to the JSON
		ArrayNode array = mapper.valueToTree(Arrays.asList(leads));
		((ObjectNode)jsonNode).putArray("leads").addAll(array);
		String finalJsonObj = "";
		System.out.println("Final JSON Object:");
		try
		{
			finalJsonObj = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(jsonNode);
		}
		catch(JsonProcessingException e)
		{
			System.out.println("Error writing to JSON file: " + e.getMessage());
		}
		System.out.println(finalJsonObj);
	}
	
	/**
	 * Deduplicate an array of leads. Leads are considered duplicates if they 
	 * share an id or an email. Dups are reconciled according to these rules:
	 * 
	 * 1) the data from the newest date should be preferred
	 * 2) duplicate IDs and emails count as dups. Other fields don't matter
	 * 3) if the dates are identical, the data from the record provided last 
	 *	  in the list should be preferred
	 *
	 * @param leads
	 * @return
	 */
	private static Lead[] dedup(Lead[] leads)
	{
		/*
		 * Using HashMaps to map ids and emails to a Lead because assuming
		 * an adequate hashing algorithm, Seach, Insert, and Delete should be
		 * performed in O(1) time.
		 * 
		 * These are how I will keep track of lead records to check for dups
		 */
		HashMap<String, Lead> idHash = new HashMap<String, Lead>();
		HashMap<String, Lead> emailHash = new HashMap<String, Lead>();
		
		/*
		 * Starting from the bottom of the list since records at the bottom
		 * take preference
		 */
		for(int i = leads.length - 1; i >= 0; i--)
		{
			Lead curr = leads[i]; // curr, the current lead
			
			// will be checking for dups based off of ID and email
			String currID = curr.get_id(); 
			String currEmail = curr.getEmail(); 
			
			// check if there is a duplicate id further down the list
			if(idHash.containsKey(currID))
			{
				// get the tmp lead which has the same ID as curr
				Lead tmp = idHash.get(currID);
				
				// If the lead further down the list's entryDate is older than curr's 
				if(Utils.isABeforeB(tmp.getEntryDate(), curr.getEntryDate()))
				{
					System.out.println("Removing");
					System.out.println(tmp.toString());
					System.out.println("from the list and replacing it with");
					System.out.println(curr.toString());
					System.out.println("\n");
					
					/*
					 * replace older lead with newer one or if the dates are the
					 * same, the one further down the list
					 */
					idHash.replace(currID, tmp, curr);
					leads[tmp.getListIdx()] = null;
					
					// Be sure to clean up the email hash as well
					String tmpEmail = tmp.getEmail();
					if(emailHash.containsKey(tmpEmail)) emailHash.remove(tmpEmail);
				}
				/*
				 *  Else, the lead further down the list is newer than or the
				 *  same as curr, so it gets preference
				 */
				else
				{
					System.out.println("Removing");
					System.out.println(curr.toString());
					System.out.println("from the list and replacing it with");
					System.out.println(tmp.toString());
					System.out.println("\n");
					leads[i] = null;
					
					// Be sure to clean up the email hash as well
					if(emailHash.containsKey(currEmail)) 
						emailHash.remove(currEmail);
				}
			}
			/*
			 * if we haven't seen a Lead with this id, make note of where this
			 * record exists in the array and add the (id, record) pair to the 
			 * hashmap
			 */
			else {
				curr.setListIdx(i);
				idHash.put(currID, curr);
			}
			
			// check if there is a duplicate email further down the list
			if(emailHash.containsKey(currEmail))
			{
				// get the tmp lead which has the same email as curr
				Lead tmp = emailHash.get(currEmail);
				
				/*
				 *  If the lead further down the list's entryDate is older
				 *  than lead/curr's 
				 */
				if(Utils.isABeforeB(tmp.getEntryDate(), curr.getEntryDate()))
				{
					System.out.println("Removing");
					System.out.println(tmp.toString());
					System.out.println("from the list and replacing it with");
					System.out.println(curr.toString());
					System.out.println("\n");
					emailHash.replace(currEmail, tmp, curr);
					leads[tmp.getListIdx()] = null;
					
					// Clean up ID hash too
					String tmpID = tmp.get_id();
					if(idHash.containsKey(tmpID)) idHash.remove(tmpID);
				}
				else {
					System.out.println("Removing");
					System.out.println(curr.toString());
					System.out.println("from the list and replacing it with");
					System.out.println(tmp.toString());
					System.out.println("\n");
					leads[i] = null;
					
					// Clean up ID hash too
					if(idHash.containsKey(currID)) idHash.remove(currID);
				}
			}
			/*
			 * if we haven't seen a Lead with this email, make note of where this
			 * record exists in the array and add the (email, record) pair to the 
			 * hashmap
			 */
			else {
				curr.setListIdx(i);
				emailHash.put(currEmail, curr);
			}
		}
		
		//remove null values from the array by creating a new array
		leads = Arrays.stream(leads).filter(lead -> (lead != null)).toArray(Lead[]::new);
		
		return leads;
	}

	/**
	 * Internal method to specifically create the Leads Array
	 * 
	 * @param mapper
	 * @param jsonNode
	 * @return
	 */
	private static Lead[] createLeadsArrayFromJsonNode(ObjectMapper mapper, JsonNode jsonNode)
	{
		Lead[] leads = null;
		String leadsJson = "";
		try
		{
			// Unmarshall to an array
			leadsJson = mapper.writeValueAsString(jsonNode.get("leads"));
			leads = mapper.readValue(leadsJson, Lead[].class);
			return leads;
		}
		catch(Exception e)
		{
			System.out.println("Error unmarshalling Leads: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Internal method to create JSON nodes. Will print out the JSON object
	 * @param mapper
	 * @param json
	 * @return A JsonNode constucted from the provided JSON string
	 */
	private static JsonNode createJsonNode(ObjectMapper mapper, String json)
	{
		try
		{
			JsonNode jsonNode = mapper.readTree(json);
			System.out.println("Original JSON Object:");
			System.out.println(mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(jsonNode));
			System.out.println("\n");
			return jsonNode;
		}
		catch(Exception e)
		{
			System.out.println("Error parsing JSON file: " + e.getMessage());
			return null;
		}
	}
}
