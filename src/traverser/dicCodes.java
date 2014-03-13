package traverser;

import java.util.HashMap;

public class dicCodes {
	HashMap<String, Integer> titleCodes = new HashMap<String, Integer>();
	HashMap<String, Integer> disciplineCodes = new HashMap<String, Integer>();
	
	public void setTitleCodes(){
		titleCodes.put("software engineer", 1);
		titleCodes.put("programmer", 1);
		titleCodes.put("Applications Engineer", 1);
		titleCodes.put("Chief Technology Officer", 2);
		titleCodes.put("Chief Information Officer", 2);
		titleCodes.put("Computer and Information Systems Manager", 3);
		titleCodes.put("Database Administrator", 3);
		titleCodes.put("Help Desk Technician", 4);
		titleCodes.put("Information Technology Director", 5);
		titleCodes.put("Information Technology Manager", 5);
		titleCodes.put("Management Information Systems Director", 5);
		titleCodes.put("Network Architect", 6);
		titleCodes.put("Network Engineer", 6);
		titleCodes.put("Network System Administrator", 6);
		titleCodes.put("Programmer Analyst", 7);
		titleCodes.put("Security Specialist", 8);
		titleCodes.put("Senior Applications Engineer", 1);
		titleCodes.put("Senior Database Administrator", 3);
		titleCodes.put("Senior Network Architect", 6);
		titleCodes.put("Senior Network Engineer", 6);
		titleCodes.put("Senior Network System Administrator", 6);
		titleCodes.put("Senior Programmer", 1);
		titleCodes.put("Senior Programmer Analyst", 7);
		titleCodes.put(" Senior Security Specialist", 8);
		titleCodes.put("Senior Software Engineer", 1);
		titleCodes.put("Senior Support Specialist", 9);
		titleCodes.put(" Senior System Administrator", 10);
		titleCodes.put("Senior System Analyst", 10);
		titleCodes.put("Senior System Architect", 10);
		titleCodes.put("Senior System Designer", 10);
		titleCodes.put("Senior Systems Analyst", 10);
		titleCodes.put("Senior Systems Software Engineer", 10);
		titleCodes.put("Senior Web Administrator", 11);
		titleCodes.put("Senior Web Developer", 11);
		titleCodes.put("Software Quality Assurance Analyst", 12);
		titleCodes.put("Support Specialist", 9);
		titleCodes.put("System Administrator", 10);
		titleCodes.put("System Analyst", 10);
		titleCodes.put("System Architect", 10);
		titleCodes.put("System Designer", 10);
		titleCodes.put("Technical Specialist", 13);
		titleCodes.put("Telecommunications Specialist", 14);
		titleCodes.put("Web Administrator", 11);
		titleCodes.put("Web Developer", 11);
		titleCodes.put("Webmaster", 11);
	}
	
	public void setTypeCodes(){
		disciplineCodes.put("Information Technology", 1);
		disciplineCodes.put("Network", 1);
		disciplineCodes.put("Bioinformatics", 2);
		disciplineCodes.put("Medical", 3);
		disciplineCodes.put("Business", 4);
		disciplineCodes.put("Accounting", 4);
		disciplineCodes.put("Transportation", 5);
		disciplineCodes.put("Banking", 6);
		disciplineCodes.put("Art", 7);
		disciplineCodes.put("Agriculture", 8);
	}
}
