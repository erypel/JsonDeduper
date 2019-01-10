# JsonDeduper
This program will remove duplicates for a JSON file according to these rules:
1) the data from the newest date should be preferred
2) duplicate IDs and emails count as dups. Other fields don't matter
3) if the dates are identical, the data from the record provided last in the list should be preferred

I assume the JSON data is structured as:
{"leads": [{
	"_id": "...",
	"email": "...",
	"firstName":  "...",
	"lastName": "...",
	"address": "...",
	"entryDate": "..."
	},
	.
	.
	.
]}

Although it is not typical of JSON, my deduper allows for null values in a Lead, allowing at most 1 null id and email. This is because of the JSON parser chosen. Empty id or email values will behave as a nonempty value as well allowing one Lead per.

I have assumed that entryDates are in the form of ISO_OFFSET_DATE_TIME.

I used Maven to manage my build. To run:

	mvn clean install compile
	mvn exec:java -D exec.mainClass=com.evan.dedup.Dedup -Dexec.args="<TheRelativeLocationOfYourJsonFile>"
	
