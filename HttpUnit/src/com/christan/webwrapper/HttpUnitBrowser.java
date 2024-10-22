package com.christan.webwrapper;

import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.SAXException;

import com.meterware.httpunit.*;

public class HttpUnitBrowser {
	WebConversation browser;
	WebResponse response;

	public HttpUnitBrowser() {
		init();
	}

	public void init() {
		// very important to keep these 2 lines or 3rd party errors will start occurring.
		HttpUnitOptions.setScriptingEnabled(true);
		HttpUnitOptions.setExceptionsThrownOnScriptError(false);

		browser = new WebConversation();
	}

	public void open(String url) throws IOException, SAXException {
		response = browser.getResponse(url);
	}
	
	public WebResponse getCurrentPage() {
		return browser.getCurrentPage();
	}
	
	public boolean clickHTMLElement(HTMLElement elementToClick) {
		boolean result = true;
		Button b;
		
		try {
			b = (Button)elementToClick;
			b.click();
			response = getCurrentPage();
		} catch (Exception e) {
			result = false;
			System.err.println(e.getStackTrace());
		}
		
		return result;
	}

	public boolean clickLinkWithText(String linkText) {
		boolean result = true;
		WebLink link;

		try {
			link = getLinkWith(linkText);
			link.click();
			response = getCurrentPage();
		} catch (Exception e) {
			result = false;
			System.err.println(e.getStackTrace());
		}

		return result;
	}
	
	public boolean clickLinkWithID(String linkId) {
		boolean result = true;
		WebLink link;

		try {
			link = getLinkWithID(linkId);
			link.click();
			response = getCurrentPage();
		} catch (Exception e) {
			result = false;
			System.err.println(e.getStackTrace());
		}

		return result;
	}

	public WebLink getLinkWith(String text) throws SAXException {
		return response.getLinkWith(text);
	}
	
	public WebLink getLinkWithID(String linkId) throws SAXException {
		return response.getLinkWithID(linkId);
	}
	
	public String getCurrentPageUrl() {
		return getCurrentPage().getURL().toString();
	}
	
	private int[] findOccurrenceIndexes (String stringToCheck, char itemToFind) {
		
		ArrayList<Integer> occurrenceIndices = new ArrayList<Integer>();
		int occurrenceIndex = stringToCheck.length();
		int arraySize;
		int[] result;
		
		while(occurrenceIndex >= 0) {
			occurrenceIndex = stringToCheck.lastIndexOf(itemToFind, occurrenceIndex);
			if (occurrenceIndex >= 0) {
				occurrenceIndices.add(occurrenceIndex);
				// need to decrement the end point or it will be an infinite loop
				occurrenceIndex--;
			}
		} 
		arraySize = occurrenceIndices.size();
		result = new int[arraySize];
		// reverse the order of the list
		for (int i = 0; i < arraySize; i++) {
			result[i] = occurrenceIndices.get(arraySize - i - 1);
		}
		return result;
	}
	
	public String getCurrentDomain() {
		// Note: there could be MUCH better error checking done here...
		String domain, url;
		char slash = '/';
		// i.e. http://www.pingidentity.com/ would be 5, 6 and 27
		int[] slashOccurrences;
		
		url = getCurrentPageUrl();
		
		slashOccurrences = findOccurrenceIndexes(url, slash);
		
		// if there are at least three slash occurrences i.e. http://a.com/
		if (slashOccurrences.length >= 3) {
			domain = url.substring(slashOccurrences[1] + 1, slashOccurrences[2]);
		// else if there's only two slash occurrences i.e. http://a.com
		} else if (slashOccurrences.length == 2) {
			domain = url.substring(slashOccurrences[1] + 1);
		// else someone is using this method wrong
		} else {
			domain = null;
		}
		return domain;
	}
	
	public String getCurrentPagePath() {
		// Note: there could be MUCH better error checking done here...
		String domain, url;
		String defaultHomepageName = "homepage";
		char slash = '/';
		// i.e. http://www.pingidentity.com/ would be 5, 6 and 27
		int[] slashOccurrences;
		
		url = getCurrentPageUrl();
		slashOccurrences = findOccurrenceIndexes(url, slash);
		
		// if there are at least three slash occurrences i.e. http://a.com/aboutus.html
		if (slashOccurrences.length >= 3) {
			domain = url.substring(slashOccurrences[2] + 1);
			// if this is a homepage i.e. http://a.com/
			if (domain.length() == 0) {
				domain = defaultHomepageName;
			}
		// else if there's only two slash occurrences i.e. http://a.com
		} else if (slashOccurrences.length == 2) {
			domain = defaultHomepageName;
		// else someone is using this method wrong
		} else {
			domain = null;
		}
		return domain;
	}


	public String getCurrentPageTitle() throws SAXException {
		return getCurrentPage().getTitle();
	}
	
	public HTMLElement getElementWithID(String id) {
		try {
			return response.getElementWithID(id);
		} catch (SAXException e) {
			return null;
		}
	}
	public HTMLElement[] getElementsWithAttribute(String name, String value) {
		try {
			return response.getElementsWithAttribute(name, value);
		} catch (SAXException e) {
			return null;
		}
	}
	public HTMLElement[] getElementsWithName(String name) {
		try {
			return response.getElementsWithName(name);
		} catch (SAXException e) {
			return null;
		}
	}
	public String[] getElementNames(){
		try {
			return response.getElementNames();
		} catch (SAXException e) {
			return null;
		}
	}
	
	public WebLink[] getLinks() {
		try {
			return response.getLinks();
		} catch (SAXException e) {
			return null;
		}
	}
	
	public String[] getMetaTagContent(String attribute, String attributeValue) {
		try {
			return response.getMetaTagContent(attribute, attributeValue);
		} catch (SAXException e) {
			return null;
		}
	}
	
	public WebImage[] getImages()  {
		try {
			return response.getImages();
		} catch (SAXException e) {
			return null;
		}
	}
	
	public WebForm[] getForms() throws SAXException {
		return response.getForms();
	}
	/*
	public WebForm getFormWithID(String ID) throws SAXException {
		return response.getFormWithID(ID);
	}*/
}
