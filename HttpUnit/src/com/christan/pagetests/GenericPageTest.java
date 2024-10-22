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
import com.meterware.httpunit.Button;
import com.meterware.httpunit.HTMLElement;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;

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
		String pageTitle, filePrefix = "title";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageTitle = browser.getCurrentPageTitle();
		
		if ((pageTitle == null) || (pageTitle.length() == 0)) {
			fail("This page has no page title!");
		}
		
		Assert.assertTrue("The page title of this page does not match the previous page title", checkIfFileExistsAndCreateIfNot(filePrefix, pageTitle, p));
	}
	
	@Test
	public void testNavigation() throws IOException, SAXException {
		String pageNavigation, filePrefix = "navigation";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageNavigation = getTextFromElementId("nav-container").trim();
		
		if ((pageNavigation == null) || (pageNavigation.length() == 0)) {
			fail("This page has no navigation!");
		}
		
		Assert.assertTrue("The page navigation of this page does not match the previous page navigation", checkIfFileExistsAndCreateIfNot(filePrefix, pageNavigation, p));
	}
	
	@Test
	public void testFooter() throws IOException, SAXException {
		String pageFooter, filePrefix = "footer";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageFooter = getTextFromElementId("footer-container").trim();
		
		if ((pageFooter == null) || (pageFooter.length() == 0)) {
			fail("This page has no footer!");
		}
		
		Assert.assertTrue("The page footer of this page does not match the previous page navigation", checkIfFileExistsAndCreateIfNot(filePrefix, pageFooter, p));
	}
	
	@Test
	public void testLinksOnPage() throws IOException, SAXException {
		String pageLinks, filePrefix = "links";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageLinks = p.convertWebLinksToString(browser.getLinks());
		
		if ((pageLinks == null) || (pageLinks.length() == 0)) {
			fail("This page has no links!");
		}
		
		Assert.assertTrue("The page links on this page do not match the previous page links", checkIfFileExistsAndCreateIfNot(filePrefix, pageLinks, p));
	}
	
	@Test
	public void testColumnOneContent() throws IOException, SAXException {
		String pageMainContent, filePrefix = "col1";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageMainContent = getTextFromElementId("col1").trim();
		
		if ((pageMainContent == null) || (pageMainContent.length() == 0)) {
			fail("This page has no main content!");
		}
		
		Assert.assertTrue("The main content on this page does not match the previous page's main content", checkIfFileExistsAndCreateIfNot(filePrefix, pageMainContent, p));
	}
	
	/**
	 *  It's ignored for now because this column seems to be dynamically generated and will fail on subsequent test passes.
	 * @throws IOException
	 * @throws SAXException
	 */
	@Ignore
	@Test
	public void testColumnThreeContent() throws IOException, SAXException {
		String pageCol3, filePrefix = "col3";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageCol3 = getTextFromElementId("col3").trim();
		
		if ((pageCol3 == null) || (pageCol3.length() == 0)) {
			fail("This page has no col3 content!");
		}
		
		Assert.assertTrue("The right nav (col3) content on this page does not match the previous page's right nav content", checkIfFileExistsAndCreateIfNot(filePrefix, pageCol3, p));
	}
	
	/**
	 * This tests the meta tag content of the page.
	 * @throws IOException
	 * @throws SAXException
	 */
	
	@Test
	public void testMetaTagKeywordsContent() throws IOException, SAXException {
		String pageMetaKeywordsData, filePrefix = "metakeywords";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageMetaKeywordsData = p.convertStringArrayToString(browser.getMetaTagContent("name", "Keywords"));
		if ((pageMetaKeywordsData == null) || (pageMetaKeywordsData.length() == 0)) {
			fail("This page has no meta keywords data!");
		}
		Assert.assertTrue("The meta keywords data of this page does not match the previous page's meta keywords data", checkIfFileExistsAndCreateIfNot(filePrefix, pageMetaKeywordsData, p));
	}
	
	/**
	 * This tests the images on the page.
	 * @throws IOException
	 * @throws SAXException
	 */
	
	@Test
	public void testImagesOnPage() throws IOException, SAXException {
		String pageImages, filePrefix = "images";
		Parser p;
		
		browser.open(url);
		p = new Parser(browser.getCurrentDomain());
		pageImages = p.convertWebImagesToString(browser.getImages());
		Assert.assertTrue("The image sources of this page does not match the previous page's image sources", checkIfFileExistsAndCreateIfNot(filePrefix, pageImages, p));
	}

	/**
	 * This method takes in the file prefix and compares the contents of pageContents with the file on disk. If none exists then a new file will be created and populated with pageContents and an Assert.Fail() will be called.
	 * @param filePrefix {String} the prefix of the file to test/write to. The rest of the file name will be the URL path (i.e. non-domain) converted to text + the file extension (i.e. ".txt")
	 * @param pageContents {String} the calling method must pass in the contents that needs to be tested/written to file.
	 * @param p {Parser} an instantiated parser in order to read/write files from/to disk
	 * @return true if the contents of the file matches the pageContents
	 * @throws IOException on conversion error or file read/write error
	 */
	public boolean checkIfFileExistsAndCreateIfNot(String filePrefix, String pageContents, Parser p) throws IOException {
		String fileContents;
		String fileName = filePrefix + "_" + p.convertUrlToFileNameWithExtension(browser.getCurrentPagePath());
		
		if (!p.isFileAlreadyExists(fileName)) {
			p.writeToFile(fileName, pageContents);
			fail("The file for storing the "+filePrefix+" of this page ("+fileName+") did not exist previously, creating it on this run to serve as a template to test against on future runs.");
		}
		// the file being read seems to come back with carriage returns in the String so I ignored them until a better solution is written
		fileContents = removeCarriageReturns(p.getFileContents(fileName));
		
		return fileContents.equals(pageContents);
	}
	
	@Test
	public void testLogin() throws IOException, SAXException {
		final String sUserName = "ctan.pingidentity@gmail.com", sPassword = "password",
			loginId = "login-link", userNameId = "userID", passwordId = "password", 
			loginLinkText = "Log In";
		HTMLElement userName, password, submit;
		String navTextBefore, navTextAfter;
		//Parser p = new Parser(browser.getCurrentDomain());
		
		// open the page
		browser.open(url);
		
		// check the nav text before
		navTextBefore = browser.getElementWithID(loginId).getText();
		
		// click login
		browser.clickLinkWithText(loginLinkText);
		
		// enter the user name
		userName = browser.getElementWithID(userNameId);
		userName.setAttribute("value", sUserName);
		
		// enter the password
		password = browser.getElementWithID(passwordId);
		password.setAttribute("value", sPassword);
		
		// click submit
		submit = browser.getElementWithID("loginSubmitButton");
		if(!browser.clickHTMLElement(submit)) {
			fail("Clicking on the submit button seems to have failed.");
		}
		
		// check the nav text after
		navTextAfter = browser.getElementWithID(loginId).getText();
		
		// if the nav text from before and after are the same, complain!
		assertFalse("The nav did not change after signing in", navTextAfter.equals(navTextBefore));
	}
}
