package com.evan.dedup;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This is a POJO class based off of Lead objects in the provided leads.json
 * @author Evan
 *
 */
public class Lead
{
	// These are the fields a Lead will have in the JSON file
	String _id;
	String email;
	String firstName;
	String lastName;
	String address;
	String entryDate;
	
	// This is a field used to help remove duplicates
	@JsonIgnore
	int listIdx = -1; // an index in an array
	
	// dummy constructor necessary for ObjectMapper
	public Lead() {}
	
	// using this constructor for unit tests
	public Lead(String _id, String email, String firstName, String lastName,
			String address, String entryDate)
	{
		this._id = _id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.entryDate = entryDate;
	}
	
	public String get_id()
	{
		return _id;
	}
	
	public void set_id(String _id)
	{
		this._id = _id;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public String getEntryDate()
	{
		return entryDate;
	}
	
	public void setEntryDate(String entryDate)
	{
		this.entryDate = entryDate;
	}
	
	public int getListIdx()
	{
		return listIdx;
	}

	public void setListIdx(int listIdx)
	{
		this.listIdx = listIdx;
	}

	@Override
	public String toString()
	{
		return "{\n\t"
				+ "\"_id\": \"" + _id + "\",\n\t"
				+ "\"email\": \"" + email + "\",\n\t"
				// There are two spaces between the key and value here because
				// that is how it was formatted in the leads.json file I was sent. 
				+ "\"firstName\":  \"" + firstName + "\",\n\t"
				+ "\"lastName\": \"" + lastName + "\",\n\t"
				+ "\"address\": \"" + address + "\",\n\t"
				+ "\"entryDate\": \"" + entryDate + "\"\n"
				+ "}";
	}
}
