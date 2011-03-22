package com.christan.pagetests;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.christan.parser.Parser;
import com.christan.webwrapper.HttpUnitBrowser;
import com.meterware.httpunit.HTMLElement;

public abstract class GenericPageTest {
	protected String url = null;
	protected HttpUnitBrowser browser;
	
	protected void initialize(){
		browser = new HttpUnitBrowser();
	}
	
	public String removeCarriageReturns(String stringToModify) {
		return stringToModify.replaceAll("\r", "");
	}
	
	public String trimAllLines(String textToTrim) {
		return trimAllLines(textToTrim.split("\n")); 
	}
	
	public String trimAllLines(String[] textToTrim) {
		String trimmedLine, result = "";
		for (String line : textToTrim) {
			trimmedLine = line.trim();
			if (trimmedLine.length() > 0) {
				result += line.trim() + "\n";
			}
		}
		result.substring(0, result.length() - 1);
		return result;
	}
	
	public String getTextFromElementId(String elementId) {
		String rawText = browser.getElementWithID(elementId).getText();
		return trimAllLines(rawText);
	}
	
	@Test
	public void testTitle() throws IOException, SAXException {
		String fileName, fileContents, pageTitle, filePrefix = "title_";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageTitle = browser.getCurrentPageTitle();
		fileName = filePrefix + p.convertUrlToFileNameWithExtension(browser.getCurrentPagePath());
		
		if ((pageTitle == null) || (pageTitle.length() == 0)) {
			fail("This page has no page title!");
		}
		
		if (!p.isFileAlreadyExists(fileName)) {
			p.writeToFile(fileName, pageTitle);
			fail("The file for storing the title of this page ("+fileName+") did not exist previously, creating it on this run.");
		}
		fileContents = p.getFileContents(fileName);
		Assert.assertTrue("The page title of this page does not match the previous page title", fileContents.equals(pageTitle));
	}
	
	@Test
	public void testNavigation() throws IOException, SAXException {
		String fileName, fileContents, pageNavigation, filePrefix = "navigation_";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageNavigation = getTextFromElementId("nav-container").trim();
		fileName = filePrefix + p.convertUrlToFileNameWithExtension(browser.getCurrentPagePath());
		
		if ((pageNavigation == null) || (pageNavigation.length() == 0)) {
			fail("This page has no navigation!");
		}
		
		if (!p.isFileAlreadyExists(fileName)) {
			p.writeToFile(fileName, pageNavigation);
			fail("The file for storing the navigation of this page ("+fileName+") did not exist previously, creating it on this run.");
		}
		// the file being read seems to come back with carriage returns in the String so I ignored them until a better solution is written
		fileContents = removeCarriageReturns(p.getFileContents(fileName));
		
		Assert.assertTrue("The page navigation of this page does not match the previous page navigation", fileContents.equals(pageNavigation));
	}
	
	@Test
	public void testFooter() throws IOException, SAXException {
		String fileName, fileContents, pageFooter, filePrefix = "footer_";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageFooter = getTextFromElementId("footer-container").trim();
		fileName = filePrefix + p.convertUrlToFileNameWithExtension(browser.getCurrentPagePath());
		
		if ((pageFooter == null) || (pageFooter.length() == 0)) {
			fail("This page has no footer!");
		}
		
		if (!p.isFileAlreadyExists(fileName)) {
			p.writeToFile(fileName, pageFooter);
			fail("The file for storing the footer of this page ("+fileName+") did not exist previously, creating it on this run.");
		}
		// the file being read seems to come back with carriage returns in the String so I ignored them until a better solution is written
		fileContents = removeCarriageReturns(p.getFileContents(fileName));
		
		Assert.assertTrue("The page footer of this page does not match the previous page navigation", fileContents.equals(pageFooter));
	}
	
	@Test
	public void testLinksOnPage() throws IOException, SAXException {
		String fileName, fileContents, pageLinks, filePrefix = "links_";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageLinks = p.convertWebLinksToString(browser.getLinks());
		fileName = filePrefix + p.convertUrlToFileNameWithExtension(browser.getCurrentPagePath());
		
		if ((pageLinks == null) || (pageLinks.length() == 0)) {
			fail("This page has no links!");
		}
		
		if (!p.isFileAlreadyExists(fileName)) {
			p.writeToFile(fileName, pageLinks);
			fail("The file for storing the links on this page ("+fileName+") did not exist previously, creating it on this run.");
		}
		// the file being read seems to come back with carriage returns in the String so I ignored them until a better solution is written
		fileContents = removeCarriageReturns(p.getFileContents(fileName));
		
		Assert.assertTrue("The page links on this page do not match the previous page links", fileContents.equals(pageLinks));
	}
	
	@Test
	public void testColumnOneContent() throws IOException, SAXException {
		String fileName, fileContents, pageMainContent, filePrefix = "col1_";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageMainContent = getTextFromElementId("col1").trim();
		fileName = filePrefix + p.convertUrlToFileNameWithExtension(browser.getCurrentPagePath());
		
		if ((pageMainContent == null) || (pageMainContent.length() == 0)) {
			fail("This page has no main content!");
		}
		
		if (!p.isFileAlreadyExists(fileName)) {
			p.writeToFile(fileName, pageMainContent);
			fail("The file for storing the main content on this page ("+fileName+") did not exist previously, creating it on this run.");
		}
		// the file being read seems to come back with carriage returns in the String so I ignored them until a better solution is written
		fileContents = removeCarriageReturns(p.getFileContents(fileName));
		
		Assert.assertTrue("The main content on this page does not match the previous page's main content", fileContents.equals(pageMainContent));
	}
	
	/**
	 * This tests the right nav of the page. It's ignored for now because this column seems to be dynamic.
	 * @throws IOException
	 * @throws SAXException
	 */
	@Ignore
	@Test
	public void testColumnThreeContent() throws IOException, SAXException {
		String fileName, fileContents, pageCol3, filePrefix = "col3_";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageCol3 = getTextFromElementId("col3").trim();
		fileName = filePrefix + p.convertUrlToFileNameWithExtension(browser.getCurrentPagePath());
		
		if ((pageCol3 == null) || (pageCol3.length() == 0)) {
			fail("This page has no col3 content!");
		}
		
		if (!p.isFileAlreadyExists(fileName)) {
			p.writeToFile(fileName, pageCol3);
			fail("The file for storing the right nav (col3) content on this page ("+fileName+") did not exist previously, creating it on this run.");
		}
		// the file being read seems to come back with carriage returns in the String so I ignored them until a better solution is written
		fileContents = removeCarriageReturns(p.getFileContents(fileName));
		
		Assert.assertTrue("The right nav (col3) content on this page does not match the previous page's right nav content", fileContents.equals(pageCol3));
	}
	
	/**
	 * This tests the meta tag content of the page.
	 * @throws IOException
	 * @throws SAXException
	 */
	@Test
	public void testMetaTagKeywordsContent() throws IOException, SAXException {
		String fileName, fileContents, pageMetaKeywordsData, filePrefix = "metakeywords_";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageMetaKeywordsData = p.convertStringArrayToString(browser.getMetaTagContent("name", "Keywords"));
		fileName = filePrefix + p.convertUrlToFileNameWithExtension(browser.getCurrentPagePath());
		
		if ((pageMetaKeywordsData == null) || (pageMetaKeywordsData.length() == 0)) {
			fail("This page has no meta keywords data!");
		}
		
		if (!p.isFileAlreadyExists(fileName)) {
			p.writeToFile(fileName, pageMetaKeywordsData);
			fail("The file for storing the meta keywords data of this page ("+fileName+") did not exist previously, creating it on this run.");
		}
		// the file being read seems to come back with carriage returns in the String so I ignored them until a better solution is written
		fileContents = removeCarriageReturns(p.getFileContents(fileName));
		
		Assert.assertTrue("The meta keywords data of this page does not match the previous page's meta keywords data", fileContents.equals(pageMetaKeywordsData));
	}
	
	/**
	 * This tests the images on the page.
	 * @throws IOException
	 * @throws SAXException
	 */
	@Test
	public void testImagesOnPage() throws IOException, SAXException {
		String fileName, fileContents, pageImages, filePrefix = "images_";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageImages = p.convertWebImagesToString(browser.getImages());
		fileName = filePrefix + p.convertUrlToFileNameWithExtension(browser.getCurrentPagePath());
		
		if (!p.isFileAlreadyExists(fileName)) {
			p.writeToFile(fileName, pageImages);
			fail("The file for storing the image sources of this page ("+fileName+") did not exist previously, creating it on this run.");
		}
		// the file being read seems to come back with carriage returns in the String so I ignored them until a better solution is written
		fileContents = removeCarriageReturns(p.getFileContents(fileName));
		
		Assert.assertTrue("The image sources of this page does not match the previous page's image sources", fileContents.equals(pageImages));
	}
}
